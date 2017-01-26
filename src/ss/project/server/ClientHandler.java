package ss.project.server;

import ss.project.shared.Protocol;
import ss.project.shared.exceptions.AlreadyJoinedException;
import ss.project.shared.exceptions.NotInRoomException;
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
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()), 8192);
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        closed = false;
        player = new NetworkPlayer(this);
        this.setName("ClientHandler: Unknown Player");
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

            this.setName("ClientHandler: " + player.getName());

            if (player.isRoomSupport()) {
                sendMessage(server.getRoomListString());
            } else {
                try {
                    server.getDefaultRoom().join(player);
                } catch (AlreadyJoinedException | RoomFullException e) {
                    e.printStackTrace();
                }
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
        // determine if a game is running
        if (getPlayer().isInGame()) {
            if (Protocol.Client.MAKEMOVE.equals(parts[0])) {
                if (player.isExpectingMove()) {
                    player.setMove(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
                } else {
                    // Unexpected command
                    sendMessage(Protocol.createMessage(Protocol.Server.ERROR, 4));
                }
            } else if (Protocol.Client.SENDMESSAGE.equals(parts[0])) {
                // Redirect it to the room
                player.getCurrentRoom().sendMessage(line.substring(line.indexOf(' ') + 1));
            } else if (Protocol.Client.LEAVEROOM.equals(parts[0])) {
                // Can't leave if the game has started
                sendMessage(Protocol.createMessage(Protocol.Server.ERROR, 6));
            } else {
                // Unexpected command
                sendMessage(Protocol.createMessage(Protocol.Server.ERROR, 4));
            }
        } else {
            if (Protocol.Client.GETROOMLIST.equals(parts[0])) {
                sendMessage(server.getRoomListString());
            } else if (Protocol.Client.CREATEROOM.equals(parts[0])) {
                // TODO: Implement once the new protocol is out.
            } else if (Protocol.Client.JOINROOM.equals(parts[0])) {
                try {
                    int roomId = Integer.parseInt(parts[1]);
                    Room toJoin = server.getRoomByID(roomId);
                    if (toJoin == null) {
                        // room not available
                        sendMessage(Protocol.createMessage(Protocol.Server.ERROR, 2));
                        return;
                    }
                    try {
                        toJoin.join(player);
                        // reply with id-assignment
                        sendMessage(Protocol.createMessage(Protocol.Server.ASSIGNID, toJoin.getId()));
                    } catch (AlreadyJoinedException | RoomFullException e) {
                        // Notify about joinerror
                        sendMessage(Protocol.createMessage(Protocol.Server.ERROR, 3));
                        e.printStackTrace();
                    }
                } catch (NumberFormatException e) {
                    // Just log it for now.
                    e.printStackTrace();
                }
            } else if (Protocol.Client.LEAVEROOM.equals(parts[0])) {
                if (player.getCurrentRoom() == null) {
                    // Can't leave if not in a room
                    sendMessage(Protocol.createMessage(Protocol.Server.ERROR, 6));
                }
                try {
                    player.getCurrentRoom().leave(player);
                } catch (NotInRoomException e) {
                    // Can't leave room due to error
                    sendMessage(Protocol.createMessage(Protocol.Server.ERROR, 6));
                }
            } else if (Protocol.Client.REQUESTLEADERBOARD.equals(parts[0])) {
                // send Leaderboard
                sendMessage(server.getLeaderboardMessage());
            }
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

    public NetworkPlayer getPlayer() {
        synchronized (player) {
            return player;
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
