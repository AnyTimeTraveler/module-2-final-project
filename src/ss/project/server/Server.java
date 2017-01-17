package ss.project.server;

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

    /**
     * Constructs a new Server object.
     */
    public Server(String ip, int portArg) {
        this.ip = ip;
        port = portArg;
        threads = new ArrayList<>();
        closed = false;
        Thread.currentThread().setName("Server");
    }

    /**
     * Start een Server-applicatie op.
     */
    public static void main(String[] args) {

        Server server = new Server(Config.getInstance().Host, Config.getInstance().Port);
        server.run();
    }

    /**
     * Listens to a port of this Server if there are any Clients that
     * would like to connect. For every new socket connection a new
     * ClientHandler thread is started that takes care of the further
     * communication with the Client.
     */
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(port, 255, InetAddress.getByName(ip));
            print("Now listening on: " + ip + ":" + port);
            while (!closed) {
//                print("Waiting for incoming connections...");
                Socket client = serverSocket.accept();
//                print("Connection accepted!");
                addHandler(new ClientHandler(this, client));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void print(String message) {
        System.out.println(message);
    }

    /**
     * Sends a message using the collection of connected ClientHandlers
     * to all connected Clients.
     *
     * @param msg message that is send
     */
    public void broadcast(String msg) {
        print(msg);
        for (ClientHandler clientHandler : threads) {
            clientHandler.sendMessage(msg);
        }
    }

    /**
     * Add a ClientHandler to the collection of ClientHandlers.
     *
     * @param handler ClientHandler that will be added
     */
    public void addHandler(ClientHandler handler) {
        threads.add(handler);
        handler.start();
//        print("Started ClientHandler!");
    }

    /**
     * Remove a ClientHandler from the collection of ClientHanlders.
     *
     * @param handler ClientHandler that will be removed
     */
    public void removeHandler(ClientHandler handler) {
        threads.remove(handler);
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
}
