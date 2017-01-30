package ss.project.server;

import lombok.Getter;
import ss.project.shared.Protocol;
import ss.project.shared.game.Vector3;
import ss.project.shared.model.ServerConfig;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Receives all new connections and handles the threads.
 */
public class Server {
    /**
     * A singleton reference to the Server.
     */
    @Getter
    private static Server instance;
    /**
     * A list of rooms of this server.
     */
    private final List<Room> rooms;
    /**
     * The ip of this server.
     */
    private String ip;
    /**
     * The port of this server.
     */
    @Getter
    private int port;
    /**
     * A list of threads for every client.
     */
    private List<ClientHandler> threads;
    /**
     * If true this server is closed and should not do anything.
     */
    private boolean closed;
    /**
     * If true the server is ready to accept new incoming clients.
     */
    private boolean ready;
    /**
     * A reference to the default room that should always be there on the server to join.
     */
    private Room defaultRoom;

    /**
     * Create a new server with specified ip and port.
     *
     * @param ip      The ip of this server.
     * @param portArg The port of this server.
     */
    public Server(String ip, int portArg) {
        this.ip = ip;
        port = portArg;
        closed = false;
        ready = false;
        threads = new ArrayList<>();
        rooms = new ArrayList<>();
        instance = this;
        createDefaultRoom();
    }

    /**
     * Create a default room with the simplest settings possible and add it to the list of rooms.
     */
    private void createDefaultRoom() {
        defaultRoom = new Room(2, new Vector3(4, 4, 4), 4);
        rooms.add(defaultRoom);
    }

    /**
     * Remove a room from the list of rooms of this server.
     *
     * @param room
     */
    public void removeRoom(Room room) {
        synchronized (rooms) {
            rooms.remove(room);
            if (defaultRoom == room) {
                defaultRoom = null;
            }
            createDefaultRoom();
        }
    }

    /**
     * Wait for clients to connect and accept them if ready.
     *
     * @see Server#ready
     */
    public void run() {
        Thread.currentThread().setName("Server");
        try {
            ServerSocket serverSocket = new ServerSocket(port, 255, InetAddress.getByName(ip));
            System.out.println("Now listening on: " + ip + ":" + port);
            while (!closed) {
                ready = true;
                Socket client = serverSocket.accept();
                ready = false;
                addHandler(new ClientHandler(this, client));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Send a message to all clients (even those not in rooms).
     *
     * @param msg
     */
    public void broadcast(String msg) {
        System.out.println("Message to all clients: " + msg);
        for (ClientHandler clientHandler : threads) {
            clientHandler.sendMessage(msg);
        }
    }

    /**
     * Add a new handler to the server (when first connected).
     *
     * @param handler
     */
    public void addHandler(ClientHandler handler) {
        threads.add(handler);
        handler.start();
        System.out.println("Started ClientHandler!");
    }

    /**
     * remove a clienthandler from the server (for disconnection).
     *
     * @param handler
     */
    public void removeHandler(ClientHandler handler) {
        threads.remove(handler);
        System.out.println("Removed ClientHandler!");
    }

    /**
     * Get the list of clienthandlers from this server.
     *
     * @return
     */
    public List<ClientHandler> getClientHandlers() {
        return threads;
    }

    /**
     * Get the capabilities message string from the server.
     *
     * @return
     */
    public String getCapabilitiesMessage() {
        ServerConfig sc = ServerConfig.getInstance();
        return Protocol.createMessage(Protocol.Server.SERVERCAPABILITIES,
                sc.MaxPlayers,
                sc.RoomSupport,
                sc.MaxDimensionX,
                sc.MaxDimensionY,
                sc.MaxDimensionZ,
                sc.MaxWinLength,
                sc.ChatSupport);
    }

    /**
     * @return
     * @see Server#ready
     */
    public boolean isReady() {
        return ready;
    }

    /**
     * Get the string that shows all rooms of this server.
     *
     * @return
     */
    public String getRoomListString() {
        synchronized (rooms) {
            Room[] roomCopy = new Room[rooms.size()];
            rooms.toArray(roomCopy);
            return Protocol.createMessage(Protocol.Server.SENDLISTROOMS, (Object[]) roomCopy);
        }
    }

    /**
     * Get a room instance by its id.
     *
     * @param roomId The id of the room.
     * @return
     */
    public Room getRoomByID(int roomId) {
        synchronized (rooms) {
            for (Room room : rooms) {
                if (room.getId() == roomId) {
                    return room;
                }
            }
        }
        return null;
    }

    /**
     * Create the leaderboard message that contains data about ranking.
     *
     * @return
     */
    public String getLeaderboardMessage() {
        return Protocol.createMessage(Protocol.Server.SENDLEADERBOARD, ServerConfig.getInstance().Leaderboard);
    }

    /**
     * Get the default room if created,
     * if it's not created it will be created.
     *
     * @return the default room instance.
     * @see Server#defaultRoom
     */
    public Room getDefaultRoom() {
        if (defaultRoom == null || defaultRoom.isFull()) {
            createDefaultRoom();
        }
        return defaultRoom;
    }

    /**
     * Add a new room to the list of rooms on this server.
     * If the client has autorefresh, automatically send the new roomlist to them.
     *
     * @param room The new room.
     * @see ss.project.shared.NetworkPlayer#autoRefresh
     * @see Server#getRoomListString()
     */
    public void addRoom(Room room) {
        rooms.add(room);
        for (ClientHandler client : threads) {
            if (client.getPlayer().getCurrentRoom() == null && client.getPlayer().isAutoRefresh()) {
                client.sendMessage(getRoomListString());
            }
        }
    }
}
