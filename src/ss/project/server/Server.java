package ss.project.server;

import lombok.extern.java.Log;
import ss.project.shared.Protocol;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

@Log
public class Server {
    private String ip;
    private int port;
    private List<ClientHandler> threads;
    private boolean closed;

    public Server(String ip, int portArg) {
        this.ip = ip;
        port = portArg;
        threads = new ArrayList<>();
        closed = false;
        Thread.currentThread().setName("Server");
    }

    public static void main(String[] args) {
        Server server = new Server(ServerConfig.getInstance().Host, ServerConfig.getInstance().Port);
//        server.run();
//        Room room = new Room(1, Integer.MAX_VALUE);
        System.out.println(server.getCapabilitiesMessage());
    }

    public void run() {
        log.setLevel(Level.ALL);
        try {
            ServerSocket serverSocket = new ServerSocket(port, 255, InetAddress.getByName(ip));
            log.info("Now listening on: " + ip + ":" + port);
            while (!closed) {
                log.fine("Waiting for incoming connections...");
                Socket client = serverSocket.accept();
                log.fine("Connection accepted!");
                addHandler(new ClientHandler(this, client));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void broadcast(String msg) {
        log.info("Message to all clients: " + msg);
        for (ClientHandler clientHandler : threads) {
            clientHandler.sendMessage(msg);
        }
    }

    public void addHandler(ClientHandler handler) {
        threads.add(handler);
        handler.start();
        log.fine("Started ClientHandler!");
    }

    public void removeHandler(ClientHandler handler) {
        threads.remove(handler);
        log.fine("Removed ClientHandler!");
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
}
