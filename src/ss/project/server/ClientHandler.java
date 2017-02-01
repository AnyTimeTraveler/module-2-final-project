package ss.project.server;

import ss.project.shared.NetworkPlayer;
import ss.project.shared.Protocol;
import ss.project.shared.exceptions.AlreadyJoinedException;
import ss.project.shared.exceptions.NotInRoomException;
import ss.project.shared.exceptions.ProtocolException;
import ss.project.shared.exceptions.RoomFullException;
import ss.project.shared.model.ChatMessage;

import java.io.*;
import java.net.Socket;

public class ClientHandler extends Thread {
    /**
     * The network player associated with this clienthandler.
     * Will be used in the commands from the protocol.
     */
    private Socket socket;
    private Server server;
    /**
     * Used to recieve things from the client.
     */
    private BufferedReader in;
    /**
     * Used to send things to the client.
     */
    private BufferedWriter out;
    /**
     * If true this clientHandler does not read input anymore.
     */
    private boolean closed;
    private NetworkPlayer player;

    /**
     * Create a new ClientHandler and initialize it.
     *
     * @param server the server that the clientHandler is bound to
     * @param socket the socket representing the new connection
     * @throws IOException if IOStreams are already bound or closed
     */
    //@ requires server != null && socket != null;
    //@ ensures getPlayer() != null;
    public ClientHandler(Server server, Socket socket) throws IOException {
        this.socket = socket;
        this.server = server;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()), 8192);
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        closed = false;
        player = new NetworkPlayer(this);
        this.setName("ClientHandler: Unknown Player\n" +
                "Local: " + socket.getLocalAddress() + ":" + socket.getLocalPort() +
                "\nRemote: " + socket.getRemoteSocketAddress());
    }

    /**
     * First do the basic routine:
     * <p>
     * this -- capabilities --> client
     * <p>
     * client -- capabilities --> this
     * <p>
     * if roomsupport: this --rooms--> client
     * <p>
     * else: make the player join the default room.
     * <p>
     * this -- default welcome messages --> client
     * <p>
     * read incoming messages from client until closed.
     */
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
                    Room room = server.getDefaultRoom(player);
                    room.join(player);
                    // reply with id-assignment
                    sendMessage(Protocol.createMessage(Protocol.Server.ASSIGNID, player.getId()));
                    if (room.isFull()) {
                        room.startGame();
                        room.broadcast(Protocol.createMessage(Protocol.Server.NOTIFYMESSAGE, new ChatMessage("Server", "Room is full. Starting game...")));
                    }
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
                } else {
                    interpretLine(line);
                }
            }
        } catch (IOException e) {
            //e.printStackTrace();
            shutdown();
        }
    }

    /**
     * Read an incoming line with the format of the protocol.
     */
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
                player.getCurrentRoom().broadcast(Protocol.createMessage(Protocol.Server.NOTIFYMESSAGE,
                        new ChatMessage(player.getName(), line.substring(line.indexOf(' ', line.indexOf(' ') + 1) + 1))));
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
                    Room room = Room.fromString(line, Protocol.SPACE_SYMBOL);
                    server.addRoom(room);
                    sendMessage(Protocol.createMessage(Protocol.Server.ROOMCREATED, room.getId()));
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
                String args = line.substring(line.indexOf(' ') + 1);
                server.broadcast(Protocol.createMessage(Protocol.Server.NOTIFYMESSAGE, new ChatMessage(player.getName(), args.substring(args.indexOf(' ') + 1))));
            } else {
                System.err.println("Not implemented: " + line);
            }
        }
    }

    private void sendError(int errorcode) {
        sendMessage(Protocol.createMessage(Protocol.Server.ERROR, errorcode));
    }

    public void sendMessage(String msg) {
        if (!player.isChatSupport() && Protocol.Server.NOTIFYMESSAGE.equals(msg.split(" ")[0])) {
            return;
        }
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
        return player;
    }

    private void shutdown() {
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
