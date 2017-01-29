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
    private final HumanPlayer humanPlayer;
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
    private boolean inGame;

    public Network(Controller controller, Connection connection)
            throws IOException {
        this.controller = controller;
        socket = new Socket(connection.getAddress(), connection.getPort());
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        closed = false;
        ready = false;
        inGame = false;
        this.setName("ServerInputReader");
        humanPlayer = new HumanPlayer();
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
                try {
                    controller.setRooms(Room.parseRoomListString(line));
                } catch (ProtocolException e) {
                    controller.showError("Expected Rooms", e.getStackTrace());
                    // Unhandled, yet.
                    e.printStackTrace();
                    // Badly handled
                    return;
                }
                ready = true;

                while (!closed) {
                    line = in.readLine();
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
            humanPlayer.setId(Integer.parseInt(parts[1]));
            humanPlayer.setName(ClientConfig.getInstance().PlayerName);
            controller.switchTo(Controller.Panel.MULTI_PLAYER_ROOM);
        } else if (Protocol.Server.NOTIFYMESSAGE.equals(parts[0])) {
            controller.addMessage(ChatMessage.fromString(line));
        } else if (Protocol.Server.STARTGAME.equals(parts[0])) {
            List<Player> players = new ArrayList<>();
            for (int i = 2; i < parts.length; i++) {
                NetworkPlayer np = NetworkPlayer.fromString(parts[i]);
                if (np.getId() == humanPlayer.getId()) {
                    players.add(humanPlayer);
                } else {
                    players.add(np);
                }
            }
            engine = new ClientEngine(GameParameters.fromString(parts[1]), players, this, humanPlayer.getId());
            controller.setEngine(engine);
            controller.startGame();
            controller.switchTo(Controller.Panel.GAME);
        } else if (Protocol.Server.TURNOFPLAYER.equals(parts[0])) {
            engine.setTurn(Integer.parseInt(parts[1]));
        } else if (Protocol.Server.NOTIFYMOVE.equals(parts[0])) {
            engine.notifyMove(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
        } else if (Protocol.Server.NOTIFYEND.equals(parts[0])) {
            int winReason = 0;
            int playerid = 0;
            try {
                winReason = Integer.parseInt(parts[1]);
                playerid = Integer.parseInt(parts[2]);
            } catch (NumberFormatException e) {
                //TODO: send right error message back.
                return;
            }
            engine.notifyEnd(winReason, playerid);
        } else if (Protocol.Server.SENDLISTROOMS.equals(parts[0])) {
            try {
                controller.setRooms(Room.parseRoomListString(line));
            } catch (ProtocolException e) {
                //TODO: send right error message back.
                return;
            }
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
}
