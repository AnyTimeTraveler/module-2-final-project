package ss.project.server;

import ss.project.shared.Protocol;
import ss.project.shared.exceptions.AlreadyJoinedException;
import ss.project.shared.exceptions.RoomFullException;

import java.io.*;
import java.net.Socket;

public class ClientHandler extends Thread {
    private Socket socket;
    private Server server;
    private BufferedReader in;
    private BufferedWriter out;
    private boolean closed;
    private NetworkPlayer player;

    //@ requires server != null && socket != null;
    public ClientHandler(Server server, Socket socket) throws IOException {
        this.socket = socket;
        this.server = server;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        closed = false;
        player = new NetworkPlayer(this);
        this.setName("ClientHandler: Unknown");
    }

    public void run() {
        String line;
        try {
            sendMessage(server.getCapabilitiesMessage());
            line = in.readLine();
            if (line == null) {
                closed = true;
                return;
            }
            player.setCapabilitiesFromString(line);

            if (player.isRoomSupport()) {
                sendMessage(server.getRoomListString());
            }
        } catch (IOException e) {
            e.printStackTrace();
            closed = true;
        }

        try {
            while (!closed) {
                line = in.readLine();
                interpretLine(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            closed = true;
        }
    }

    private void interpretLine(String line) {
        String[] parts = line.split(" ");
        if (Protocol.Client.GETROOMLIST.equals(parts[0])) {
            sendMessage(server.getRoomListString());
        } else if (Protocol.Client.CREATEROOM.equals(parts[0])) {
            // TODO: Implement once the new protocol is out.
        } else if (Protocol.Client.JOINROOM.equals(parts[0])) {
            try {
                int roomId = Integer.parseInt(parts[1]);
                Room toJoin = server.getRoomByID(roomId);
                if (toJoin == null) {
                    sendMessage(Protocol.createMessage(Protocol.Server.ERROR, 2));
                    return;
                }
                try {
                    toJoin.join(player);
                } catch (AlreadyJoinedException | RoomFullException e) {
                    sendMessage(Protocol.createMessage(Protocol.Server.ERROR, 3));
                    e.printStackTrace();
                }
            } catch (NumberFormatException e) {
                // Just log it for now.
                e.printStackTrace();
            }
        } else if (Protocol.Client.LEAVEROOM.equals(parts[0])) {
            // TODO: Implement
        } else if (Protocol.Client.REQUESTLEADERBOARD.equals(parts[0])) {
            sendMessage(server.getLeaderboardMessage());
        }
    }

    public void sendMessage(String msg) {
        try {
            out.write(msg);
            out.newLine();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
            closed = true;
        }
    }

    public void shutdown() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.removeHandler(this);
        closed = true;
    }
}
