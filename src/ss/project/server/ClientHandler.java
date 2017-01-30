package ss.project.server;

import ss.project.shared.NetworkPlayer;
import ss.project.shared.Protocol;
import ss.project.shared.exceptions.AlreadyJoinedException;
import ss.project.shared.exceptions.NotInRoomException;
import ss.project.shared.exceptions.ProtocolException;
import ss.project.shared.exceptions.RoomFullException;
import ss.project.shared.model.ChatMessage;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

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
                shutdown();
                return;
            }
            System.out.println(line);
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
            sendMessage(Protocol.createMessage(Protocol.Server.NOTIFYMESSAGE, new ChatMessage("Server", "Welcome!")));
            sendMessage(Protocol.createMessage(Protocol.Server.NOTIFYMESSAGE, new ChatMessage("Server", "There are currently " + server.getClientHandlers().size() + " People online.")));
            sendMessage(Protocol.createMessage(Protocol.Server.NOTIFYMESSAGE, new ChatMessage("Server", "For help type \"/help\".")));
        } catch (IOException e) {
            e.printStackTrace();
            shutdown();
            return;
        }

        try {
            while (!closed) {
                line = in.readLine();
                if (line == null) {
                    if (player.getCurrentRoom() != null) {
                        player.getCurrentRoom().endGame(Protocol.WinReason.PLAYERDISCONNECTED, player.getId());
                    }
                    shutdown();
                }
                interpretLine(line);
            }
        } catch (IOException e) {
            //e.printStackTrace();
            shutdown();
        }
    }

    private void interpretLine(String line) {
        if (line == null) {
            return;
        }
        String[] parts = line.split(" ");
        // determine if a game is running
        if (getPlayer().isInGame()) {
            if (Protocol.Client.MAKEMOVE.equals(parts[0])) {
                if (player.isExpectingMove()) {
                    player.setMove(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
                } else {
                    // Unexpected command
                    sendError(4);
                }
            } else if (Protocol.Client.SENDMESSAGE.equals(parts[0])) {
                player.getCurrentRoom().broadcast(Protocol.createMessage(Protocol.Server.NOTIFYMESSAGE, new ChatMessage(player.getName(), line.substring(line.indexOf(' ') + 1))));
            } else if (Protocol.Client.LEAVEROOM.equals(parts[0])) {
                // Can't leave if the game has started
                sendError(6);
            } else {
                // Unexpected command
                sendError(4);
            }
        } else {
            if (Protocol.Client.GETROOMLIST.equals(parts[0])) {
                sendMessage(server.getRoomListString());
            } else if (Protocol.Client.CREATEROOM.equals(parts[0])) {
                try {
                    server.addRoom(Room.fromString(line));
                } catch (ProtocolException e) {
                    e.printStackTrace();

                }
            } else if (Protocol.Client.JOINROOM.equals(parts[0])) {
                try {
                    int roomId = Integer.parseInt(parts[1]);
                    Room room = server.getRoomByID(roomId);
                    if (room == null) {
                        // room not available
                        sendError(2);
                        return;
                    }
                    try {
                        room.join(player);
                        // reply with id-assignment
                        sendMessage(Protocol.createMessage(Protocol.Server.ASSIGNID, player.getId()));
                        if (room.isFull()) {
                            room.startGame();
                            room.broadcast(Protocol.createMessage(Protocol.Server.NOTIFYMESSAGE, new ChatMessage("Server", "Room is full. Starting game...")));
                        }
                    } catch (AlreadyJoinedException | RoomFullException e) {
                        // Notify about joinerror
                        sendError(3);
                        e.printStackTrace();
                    }
                } catch (NumberFormatException e) {
                    // Unexpected arguments
                    e.printStackTrace();
                    sendError(4);
                }
            } else if (Protocol.Client.LEAVEROOM.equals(parts[0])) {
                if (player.getCurrentRoom() == null) {
                    // Can't leave if not in a room
                    sendError(6);
                }
                try {
                    player.getCurrentRoom().leave(player);
                } catch (NotInRoomException e) {
                    // Can't leave room due to error
                    sendError(6);
                }
            } else if (Protocol.Client.REQUESTLEADERBOARD.equals(parts[0])) {
                // send Leaderboard
                sendMessage(server.getLeaderboardMessage());
            } else if (Protocol.Client.SENDMESSAGE.equals(parts[0])) {
                server.broadcast(Protocol.createMessage(Protocol.Server.NOTIFYMESSAGE, new ChatMessage(player.getName(), line.substring(line.indexOf(' ') + 1))));
            } else if (Protocol.Server.ERROR.equals(parts[0])) {

            } else {
                System.out.println(line);
                throw new NotImplementedException();
            }
        }
    }

    private void sendError(int errorcode) {
        sendMessage(Protocol.createMessage(Protocol.Server.ERROR, errorcode));
    }

    public void sendMessage(String msg) {
        System.out.println(Thread.currentThread().getName() + "\nSent message: " + msg);
        try {
            out.write(msg);
            out.newLine();
            out.flush();
        } catch (IOException e) {
            //e.printStackTrace();
            shutdown();
        }
    }

    public NetworkPlayer getPlayer() {
        synchronized (player) {
            return player;
        }
    }

    public void shutdown() {
        if (player.getCurrentRoom() != null) {
            player.getCurrentRoom().lostConnection(player);
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.removeHandler(this);
        closed = true;
    }
}
