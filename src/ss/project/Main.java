package ss.project;

import ss.project.client.Controller;
import ss.project.client.Network;
import ss.project.server.Server;
import ss.project.shared.model.Connection;
import ss.project.shared.model.ServerConfig;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Scanner;

/**
 * The class that starts the application and reads arguments.
 * <p>
 * First argument: 'client' or 'server'.
 * 'client': start a client.
 * 'server': start a server and automatically start waiting for connections.
 * <p>
 * Second argument: 'gui' or 'tui'.
 * 'gui': start the JFrame and show the gui.
 * 'tui': start the tui.
 * <p>
 * Created by simon on 28.01.17.
 */
public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            Scanner sc = new Scanner(System.in);
            String type;
            boolean isValid;
            do {
                System.out.print("Type: [server|client|both|debug]");
                type = sc.nextLine();
            }
            while (!type.matches("(client)|(server)|(both)|(debug)"));

            String address;
            isValid = false;
            do {
                System.out.print("Address: ");
                address = sc.nextLine();
                try {
                    if (type.equalsIgnoreCase("server") || type.equalsIgnoreCase("both")) {
                        isValid = InetAddress.getByName(address).isAnyLocalAddress();
                    } else if (type.equalsIgnoreCase("client")) {
                        isValid = InetAddress.getByName(address).isReachable(1000);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    isValid = false;
                }
            } while (!isValid);

            int port = 1234;
            do {
                try {
                    System.out.print("Port: ");
                    port = Integer.parseInt(sc.nextLine());
                    isValid = port > 1024 && port < 65535;
                } catch (NumberFormatException e) {
                    isValid = false;
                }
            } while (!isValid);
            run(type, address, port);
        } else if (args[0].equalsIgnoreCase("server")) {
            runServer();
        } else if (args[0].equalsIgnoreCase("client")) {
            runClient(args.length < 2 || !args[1].equals("tui"));
        } else if (args[0].equalsIgnoreCase("both")) {
            Thread client = new Thread(() -> Controller.getController().start(true));
            client.setName("ClientMain");
            client.start();
            Thread server = new Thread(() -> new Server(ServerConfig.getInstance().Host, ServerConfig.getInstance().Port).run());
            server.setName("ServerMain");
            server.start();
        } else if (args[0].equalsIgnoreCase("debug")) {
            try {
                Server server = new Server(ServerConfig.getInstance().Host, ServerConfig.getInstance().Port);
                if (args.length > 1) {
                    Thread serverThread = new Thread(server::run);
                    serverThread.setName("ServerMain");
                    serverThread.start();
                    while (!server.isReady()) {
                        Thread.sleep(100);
                    }
                }
                Thread client = new Thread(() -> Controller.getController().start(true));
                client.setName("ClientMain");
                client.start();
                Controller.getController().joinServer(new Network(new Connection("Test", "localhost", 1234)).ping());
                while (!Controller.getController().isConnected()) {
                    Thread.sleep(100);
                }
                Controller.getController().joinRoom(Controller.getController().getRooms().get(0));
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void run(String type, String address, int port) {
        switch (type.toLowerCase()) {
            case "server":
                runServer();
            case "client":
            case "both":
            case "debug":
                break;
        }
    }

    public static void runClient(boolean gui) {
        Controller.getController().start(gui);
    }

    public static void runServer() {
        Server server = new Server(ServerConfig.getInstance().Host, ServerConfig.getInstance().Port);
        server.run();
    }

    /**
     * Print a help message to the console to show how to start the application.
     */
    private static void printUsage() {
        System.out.println("Usage:");
        System.out.print("java -jar ");
        System.out.print(new java.io.File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getName());
        System.out.println(" [server|client] [gui|tui]");
    }
}
