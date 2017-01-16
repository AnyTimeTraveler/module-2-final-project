package ss.project.client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;


public class Client extends Thread {
    private static final String USAGE
            = "usage: java week7.cmdchat.Client <name> <address> <port>";
    private String clientName;
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private boolean closed;

    /**
     * Constructs a Client-object and tries to make a socket connection.
     */
    public Client(String name, InetAddress host, int port)
            throws IOException {
        clientName = name;
        socket = new Socket(host, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        closed = false;
        new ClientInputReader(this).start();
        this.setName("ServerInputReader");
    }

    /**
     * Start een Client-applicatie op.
     */
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println(USAGE);
            return;
        }

        try {
            InetAddress host = InetAddress.getByName(args[1]);
            int port = Integer.parseInt(args[2]);
            Client client = new Client(args[0], host, port);
            print("Connecting...");
            client.sendMessage(args[0]);
            client.start();
            print("Connection established");
        } catch (UnknownHostException e) {
            print("ERROR: no valid hostname!");
        } catch (IOException e) {
            e.printStackTrace();
            print("ERROR: couldn't construct a client object!");
        } catch (NumberFormatException e) {
            print("ERROR: no valid portnummer!");
        }

    }

    private static void print(String message) {
        System.out.println(message);
    }

    /**
     * Reads the messages in the socket connection. Each message will
     * be forwarded to the MessageUI
     */
    public void run() {
        print("Waiting for Server response...");
        String line;
        while (!closed) {
            try {
                line = in.readLine();
                if (line == null) {
                    shutdown();
                } else if (!line.startsWith(clientName)) {
                    print(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * send a message to a ClientHandler.
     */
    public void sendMessage(String msg) {
        try {
//            print("Sent message: " + msg);
            out.write(msg);
            out.newLine();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * close the socket connection.
     */
    public void shutdown() {
        print("Closing socket connection...");
        try {
            out.write("end");
            out.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        closed = true;
    }

    private class ClientInputReader extends Thread {

        private final Client client;
        private BufferedReader in;

        public ClientInputReader(Client client) {
            this.client = client;
            in = new BufferedReader(new InputStreamReader(System.in));
            this.setDaemon(true);
            this.setName("ConsoleInputReader");
        }

        @Override
        public void run() {
            try {
                String line;
                while (!client.closed) {
//                    print("Ready to read Client input:");
                    line = in.readLine();
                    if (line.equals("end")) {
                        client.shutdown();
                    }
                    client.sendMessage(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
                closed = true;
            }
        }
    }
}
