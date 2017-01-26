package ss.project.client.networking;

import lombok.Getter;
import ss.project.client.Config;
import ss.project.client.Controller;
import ss.project.server.Room;
import ss.project.shared.Protocol;
import ss.project.shared.exceptions.ProtocolException;

import java.io.*;
import java.net.Socket;

public class Network extends Thread {
    private Controller controller;
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private boolean closed;
    private ServerInfo serverInfo;
    @Getter
    private boolean ready;

    public Network(Controller controller, Connection connection)
            throws IOException {
        this.controller = controller;
        socket = new Socket(connection.getAddress(), connection.getPort());
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        closed = false;
        ready = false;
        //new ClientInputReader(this).start();
        this.setName("ServerInputReader");
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
                sendMessage(getCapabilityString(Config.getInstance().MaxPlayers, Config.getInstance().PlayerName, Config.getInstance().RoomSupport, Config.getInstance().MaxDimensionX, Config.getInstance().MaxDimensionY, Config.getInstance().MaxDimensionZ, Config.getInstance().MaxWinLength, Config.getInstance().ChatSupport, Config.getInstance().AutoRefresh));

                // await list of rooms
                line = in.readLine();
                try {
                    controller.setRooms(Room.parseRoomListString(line));
                } catch (ProtocolException e) {
                    // Unhandled, yet.
                    e.printStackTrace();
                    // Badly handled
                    return;
                }
                ready = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

    private class ClientInputReader extends Thread {

        private final Network client;
        private BufferedReader in;

        public ClientInputReader(Network client) {
            this.client = client;
            in = new BufferedReader(new InputStreamReader(System.in));
            this.setDaemon(true);
            this.setName("ConsoleInputReader");
        }

        @Override
        public void run() {
            try {
                String line;
                while (!client.closed) {
//                    log.fine("Ready to read Network input:");
                    line = in.readLine();
                    if (line.equals("end")) {
                        client.shutdown();
                    }
                    client.sendMessage(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
                closed = true;
            }
        }
    }
}
