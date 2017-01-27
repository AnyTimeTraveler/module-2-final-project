package ss.project.client.networking;

import lombok.Getter;
import ss.project.client.Config;
import ss.project.client.Controller;
import ss.project.client.HumanPlayer;
import ss.project.server.Room;
import ss.project.shared.ChatMessage;
import ss.project.shared.Protocol;
import ss.project.shared.exceptions.ProtocolException;
import ss.project.shared.game.ClientEngine;

import java.io.*;
import java.net.Socket;

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
            return ServerInfo.fromString(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void run() {
        System.out.println("Waiting for Server response...");
        String line;
        while (!closed) {
            try {
                // read server capabilities
                line = in.readLine();
                // parse them
                serverInfo = ServerInfo.fromString(line);
                if (!serverInfo.getStatus().equals(ServerInfo.Status.ONLINE)) {
                    socket.close();
                    // something fucked up!
                    return;
                }

                // send your own capabilities
                Config config = Config.getInstance();
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
                e.printStackTrace();
            }
        }
    }

    private void interpretLine(String line) {
        String[] parts = line.split(" ");
        if (Protocol.Server.ASSIGNID.equals(parts[0])) {
            humanPlayer.setId(Integer.parseInt(parts[1]));
        } else if (Protocol.Server.NOTIFYMESSAGE.equals(parts[0])) {
            controller.addMessage(ChatMessage.fromString(line));
        }

//                    engine.setTurn(12);
//                    engine.notifyMove(3, 2, 4);
//                    engine.notifyEnd(3, 12);
    }

    /**
     * send a message to a ClientHandler.
     */

    public void sendMessage(String msg) {
        try {
//            log.fine("Sent message: " + msg);
            out.write(msg);
            out.newLine();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * close the socket connection.
     */
    public void shutdown() {
        System.out.println("Closing socket connection...");
        try {
            out.write("end");
            out.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        closed = true;
    }

    private String getCapabilityString(int maxPlayers, String name, boolean roomSupport, int maxX, int maxY, int maxZ, int winLength, boolean chat, boolean autoRefresh) {
        return Protocol.createMessage(Protocol.Client.SENDCAPABILITIES, maxPlayers, name, roomSupport, maxX, maxY, maxZ, winLength, chat, autoRefresh);
    }
}
