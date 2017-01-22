package ss.project.client.networking;

import lombok.extern.java.Log;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

@Log
public class Network extends Thread {
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private boolean closed;

    public Network(Connection connection)
            throws IOException {
        socket = new Socket(connection.getAddress(), connection.getPort());
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        closed = false;
        new ClientInputReader(this).start();
        this.setName("ServerInputReader");
    }

    public static void main(String[] args) {
        try {
            String host = "localhost";
            int port = 1234;
            Network client = new Network(new Connection("ServerConnection", host, port));
            log.fine("Connecting...");
            client.sendMessage(args[0]);
            client.start();
            log.fine("Connection established");
        } catch (UnknownHostException e) {
            log.severe("Noo valid hostname!");
        } catch (IOException e) {
            e.printStackTrace();
            log.severe("Couldn't construct a client object!");
        }

    }

    public void run() {
        log.finer("Waiting for Server response...");
        String line;
        while (!closed) {
            try {
                line = in.readLine();

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
//            log.fine("Sent message: " + msg);
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
        log.fine("Closing socket connection...");
        try {
            out.write("end");
            out.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        closed = true;
    }

    private class ClientInputReader extends Thread {

        private final Network client;
        private BufferedReader in;

        public ClientInputReader(Network client) {
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
//                    log.fine("Ready to read Network input:");
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
