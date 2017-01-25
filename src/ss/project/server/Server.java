package ss.project.server;

import ss.project.shared.Protocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Server {
    private String ip;
    private int port;
    private List<ClientHandler> threads;
    private boolean closed;
    private boolean ready;
    private List<Room> rooms;

    public Server(String ip, int portArg) {
        this.ip = ip;
        port = portArg;
        closed = false;
        ready = false;
        threads = new ArrayList<>();
        rooms = new ArrayList<>();
    }

    public static void main(String[] args) {

        Server server = new Server(ServerConfig.getInstance().Host, ServerConfig.getInstance().Port);
//        server.determineWifiAddress();
        server.run();
    }

    public void run() {
        Thread.currentThread().setName("Server");
        try {
            ServerSocket serverSocket = new ServerSocket(port, 255, InetAddress.getByName(ip));
            System.out.println("Now listening on: " + ip + ":" + port);
            while (!closed) {
                System.out.println("Waiting for incoming connections...");
                ready = true;
                Socket client = serverSocket.accept();
                ready = false;
                System.out.println("Connection accepted!");
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
        return Protocol.createMessage(Protocol.Server.SENDLISTROOMS, rooms);
    }

    public Room getRoomByID(int roomId) {
        for (Room room : rooms) {
            if (room.getId() == roomId) {
                return room;
            }
        }
        return null;
    }

    public String getLeaderboardMessage() {
        return Protocol.createMessage(Protocol.Server.SENDLEADERBOARD, ServerConfig.getInstance().Leaderboard);
    }

    private String determineWifiAddress() {
        System.out.println(System.getProperty("os.name"));
        Runtime ifconfig = Runtime.getRuntime();
        Process p;
        try {
            if (System.getProperty("os.name").contains("win")) {
                p = ifconfig.exec("ipconfig");
            } else {
                p = ifconfig.exec("ifconfig");
            }
            p.waitFor();
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append(br.readLine());
            while (br.ready()) {
                sb.append('\n');
                sb.append(br.readLine());
            }

            String output = sb.toString();
            System.out.println(output);

            int w = output.indexOf("wlo1");
            Pattern re = Pattern.compile(".+wlo1.+inet addr:([0-9|\\.]+).+");
            Matcher m = re.matcher(output);
            return m.group(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
