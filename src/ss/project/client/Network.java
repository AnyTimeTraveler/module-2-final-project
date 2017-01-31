package ss.project.client;

import lombok.Getter;
import ss.project.server.Room;
import ss.project.shared.NetworkPlayer;
import ss.project.shared.Protocol;
import ss.project.shared.exceptions.ProtocolException;
import ss.project.shared.game.ClientEngine;
import ss.project.shared.game.Player;
import ss.project.shared.model.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Network extends Thread {
    private Player ownedPlayer;
    private Controller controller;
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private boolean closed;
    @Getter
    private ServerInfo serverInfo;
    @Getter
    private boolean ready;
    private ClientEngine engine;

    public Network(Connection connection)
            throws IOException {
        this.controller = Controller.getController();
        socket = new Socket(connection.getAddress(), connection.getPort());
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        closed = false;
        ready = false;
        this.setName("ServerInputReader");
        ownedPlayer = new HumanPlayer();
    }

    public ServerInfo ping() {
        try {
            String line = in.readLine();
            socket.close();
            return ServerInfo.fromString(line, socket.getInetAddress(), socket.getPort());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void run() {
        String line;
        while (!closed) {
            try {
                // read server capabilities
                line = in.readLine();
                // parse them
                serverInfo = ServerInfo.fromString(line, socket.getInetAddress(), socket.getPort());
                if (!serverInfo.getStatus().equals(ServerInfo.Status.ONLINE)) {
                    // something fucked up!
                    shutdown();
                    return;
                }
                controller.setConnected(true);

                // send your own capabilities
                ClientConfig config = ClientConfig.getInstance();
                sendMessage(getCapabilityString(
                        config.MaxPlayers,
                        config.PlayerName,
                        config.RoomSupport,
                        config.MaxDimensionX,
                        config.MaxDimensionY,
                        config.MaxDimensionZ,
                        config.MaxWinLength,
                        Controller.getController().isDoGui(),
                        config.AutoRefresh));

                // await list of rooms
                line = in.readLine();
                if (serverInfo.isRoomSupport()) {
                    try {
                        controller.setRooms(Room.parseRoomListString(line));
                    } catch (ProtocolException e) {
                        controller.showError("Expected Rooms", e.getStackTrace());
                        // Unhandled, yet.
                        e.printStackTrace();
                        // Badly handled
                        sendError(4);
                        return;
                    }
                }
                ready = true;

                while (!closed) {
                    line = in.readLine();
                    if (line == null) {
                        shutdown();
                    }
                    interpretLine(line);
                }
            } catch (IOException e) {
                //This is only an error if the network is not closed.
                if (!closed) {
                    e.printStackTrace();
                    shutdown();
                }
            }
        }
    }

    private void interpretLine(String line) {
        String[] parts = line.split(" ");
        if (Protocol.Server.ASSIGNID.equals(parts[0])) {
            ownedPlayer.setId(Integer.parseInt(parts[1]));
            ownedPlayer.setName(ClientConfig.getInstance().PlayerName);
            controller.switchTo(Controller.Panel.MULTI_PLAYER_ROOM);
        } else if (Protocol.Server.NOTIFYMESSAGE.equals(parts[0])) {
            controller.addMessage(ChatMessage.fromString(line));
        } else if (Protocol.Server.STARTGAME.equals(parts[0])) {
            List<Player> players = new ArrayList<>();
            for (int i = 2; i < parts.length; i++) {
                NetworkPlayer np = NetworkPlayer.fromString(parts[i]);
                if (np.getId() == ownedPlayer.getId()) {
                    players.add(ownedPlayer);
                } else {
                    players.add(np);
                }
            }
            engine = new ClientEngine(GameParameters.fromString(parts[1]), players, this, ownedPlayer.getId());
            controller.setEngine(engine);
            controller.startGame();
            controller.switchTo(Controller.Panel.GAME);
        } else if (Protocol.Server.TURNOFPLAYER.equals(parts[0])) {
            engine.setTurn(Integer.parseInt(parts[1]));
        } else if (Protocol.Server.NOTIFYMOVE.equals(parts[0])) {
            engine.notifyMove(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
        } else if (Protocol.Server.NOTIFYEND.equals(parts[0])) {
            try {
                engine.notifyEnd(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
            } catch (NumberFormatException e) {
                sendMessage(Protocol.createMessage(Protocol.Server.ERROR, 4));
            }
        } else if (Protocol.Server.SENDLISTROOMS.equals(parts[0])) {
            try {
                controller.setRooms(Room.parseRoomListString(line));
            } catch (ProtocolException e) {
                e.printStackTrace();

            }
        } else if (Protocol.Server.SENDLEADERBOARD.equals(parts[0])) {
            try {
                List<LeaderboardEntry> leaderboardEntries = new ArrayList<>();
                for (int i = 0; i < parts.length - 1; i++) {
                    leaderboardEntries.add(LeaderboardEntry.fromString(parts[i + 1]));
                }
                controller.setLeaderBoard(leaderboardEntries);
            } catch (ProtocolException e) {
                e.printStackTrace();
            }
        } else if (Protocol.Server.ROOMCREATED.equals(parts[0])) {
            controller.refreshRoomList();
            controller.switchTo(Controller.Panel.MULTI_PLAYER_LOBBY);
        } else {
            System.err.println(line);
            throw new NotImplementedException();
        }
    }

    /**
     * send a message to a ClientHandler.
     */
    public void sendMessage(String msg) {
        try {
            System.err.println(Thread.currentThread().getName() + "\nSent message: " + msg);
            out.write(msg);
            out.newLine();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
            shutdown();
        }
    }

    /**
     * close the socket connection.
     */
    public void shutdown() {
        controller.setConnected(false);
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        closed = true;
    }

    private String getCapabilityString(int maxPlayers, String name, boolean roomSupport, int maxX, int maxY, int maxZ, int winLength, boolean chat, boolean autoRefresh) {
        return Protocol.createMessage(Protocol.Client.SENDCAPABILITIES, maxPlayers, name, roomSupport, maxX, maxY, maxZ, winLength, chat, autoRefresh);
    }

    private void sendError(int errorcode) {
        sendMessage(Protocol.createMessage(Protocol.Server.ERROR, errorcode));
    }

}
