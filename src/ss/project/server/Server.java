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
    private String ip;
    @Getter
    private int port;
    private List<ClientHandler> threads;
    private boolean closed;
    private boolean ready;
    private Room defaultRoom;

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
     * Remove a list from the list of rooms of this server.
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

    public void broadcast(String msg) {
        System.out.println("Message to all clients: " + msg);
        for (ClientHandler clientHandler : threads) {
            clientHandler.sendMessage(msg);
        }
    }

    public void addHandler(ClientHandler handler) {
        threads.add(handler);
        handler.start();
        System.out.println("Started ClientHandler!");
    }

    public void removeHandler(ClientHandler handler) {
        threads.remove(handler);
        System.out.println("Removed ClientHandler!");
    }

    public List<ClientHandler> getClientHandlers() {
        return threads;
    }

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

    public boolean isReady() {
        return ready;
    }

    public String getRoomListString() {
        synchronized (rooms) {
            Room[] roomCopy = new Room[rooms.size()];
            rooms.toArray(roomCopy);
            return Protocol.createMessage(Protocol.Server.SENDLISTROOMS, (Object[]) roomCopy);
        }
    }

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

    public String getLeaderboardMessage() {
        return Protocol.createMessage(Protocol.Server.SENDLEADERBOARD, ServerConfig.getInstance().Leaderboard);
    }

    public Room getDefaultRoom() {
        if (defaultRoom == null || defaultRoom.isFull()) {
            createDefaultRoom();
        }
        return defaultRoom;
    }

    public void addRoom(Room room) {
        rooms.add(room);
        for (ClientHandler client : threads) {
            if (client.getPlayer().getCurrentRoom() == null && client.getPlayer().isAutoRefresh()) {
                client.sendMessage(getRoomListString());
            }
        }
    }
}
