package ss.project.server;

import ss.project.shared.Protocol;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private String ip;
    private int port;
    private List<ClientHandler> threads;
    private boolean closed;
    private boolean ready;

    public Server(String ip, int portArg) {
        this.ip = ip;
        port = portArg;
        threads = new ArrayList<>();
        closed = false;
        ready = false;
    }

    public static void main(String[] args) {
        Server server = new Server(ServerConfig.getInstance().Host, ServerConfig.getInstance().Port);
        server.run();
        System.out.println(server.getCapabilitiesMessage());
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

    public String getClientList() {
        StringBuilder sb = new StringBuilder();
        sb.append("Connected clients:");
        for (ClientHandler clientHandler : threads) {
            sb.append('\n');
            sb.append(clientHandler.getName());
        }
        return sb.toString();
    }

    public void shutdown() {
        closed = true;

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
}
