package ss.project.server;

import java.io.*;
import java.net.Socket;

public class ClientHandler extends Thread {
    private Socket socket;
    private Server server;
    private BufferedReader in;
    private BufferedWriter out;
    private String clientName;
    private boolean closed;

    /**
     * Constructs a ClientHandler object
     * Initialises both Data streams.
     */
    //@ requires server != null && socket != null;
    public ClientHandler(Server server, Socket socket) throws IOException {
        this.socket = socket;
        this.server = server;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        closed = false;
        this.setName("ClientHandler: null");
    }

    /**
     * Reads the name of a Client from the input stream and sends
     * a broadcast message to the Server to signal that the Client
     * is participating in the chat. Notice that this method should
     * be called immediately after the ClientHandler has been constructed.
     */
    public void announce() throws IOException {
        clientName = in.readLine();
        server.broadcast("[" + clientName + " has entered]");
        this.setName("ClientHandler: " + clientName);
    }

    /**
     * This method takes care of sending messages from the Client.
     * Every message that is received, is preprended with the name
     * of the Client, and the new message is offered to the Server
     * for broadcasting. If an IOException is thrown while reading
     * the message, the method concludes that the socket connection is
     * broken and shutdown() will be called.
     */
    public void run() {
//        server.print("Handling Client Input now!");
        String line;
        try {
//            server.print("Expecting username:");
            announce();
            while (!closed) {
//                server.print("Ready to read chat from client!");
                line = in.readLine();
                if (line == null || line.equals("end")) {
                    shutdown();
                } else if (line.equals("/list")) {
                    sendMessage(server.getClientList());
                } else {
                    server.broadcast(getClientName() + ": " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            closed = true;
        }

    }

    private String getClientName() {
        StringBuilder sb = new StringBuilder();
        sb.append(clientName);
        for (int i = 0; i < 10 - clientName.length(); i++) {
            sb.append(' ');
        }
        return sb.toString();
    }

    /**
     * This method can be used to send a message over the socket
     * connection to the Client. If the writing of a message fails,
     * the method concludes that the socket connection has been lost
     * and shutdown() is called.
     */
    public void sendMessage(String msg) {
        try {
            out.write(msg);
            out.newLine();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
            closed = true;
        }
    }

    /**
     * This ClientHandler signs off from the Server and subsequently
     * sends a last broadcast to the Server to inform that the Client
     * is no longer participating in the chat.
     */
    private void shutdown() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.removeHandler(this);
        server.broadcast("[" + clientName + " has left]");
        closed = true;
    }
}
