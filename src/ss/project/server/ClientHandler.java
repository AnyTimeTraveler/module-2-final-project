package ss.project.server;

import java.io.*;
import java.net.Socket;

public class ClientHandler extends Thread {
    private Socket socket;
    private Server server;
    private BufferedReader in;
    private BufferedWriter out;
    private boolean closed;

    //@ requires server != null && socket != null;
    public ClientHandler(Server server, Socket socket) throws IOException {
        this.socket = socket;
        this.server = server;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        closed = false;
        this.setName("ClientHandler: Unknown");
    }

    public void run() {
        String line;
//        try {
        sendMessage(server.getCapabilitiesMessage());
//        } catch (IOException e) {
//            e.printStackTrace();
//            closed = true;
//        }

        try {
            while (!closed) {

                line = in.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            closed = true;
        }

    }

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

    public void shutdown() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.removeHandler(this);
        closed = true;
    }
}
