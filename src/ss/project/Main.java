package ss.project;

import ss.project.client.Controller;
import ss.project.server.Server;
import ss.project.shared.model.ServerConfig;

import java.io.IOException;
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
                System.out.print("Type: [server|client|both]");
                type = sc.nextLine();
            }
            while (!type.matches("(client)|(server)|(both)"));

            int port = 1234;
            if (type.equalsIgnoreCase("server")) {
                isValid = false;

                do {
                    try {
                        System.out.print("port: ");
                        port = Integer.parseInt(sc.nextLine());
                        isValid = port > 1024 && port < 65535;
                    } catch (NumberFormatException e) {
                        isValid = false;
                    }
                } while (!isValid);
            }
            run(type, port);
        } else if (args[0].equalsIgnoreCase("server")) {
            runServer();
        } else if (args[0].equalsIgnoreCase("client")) {
            runClient(args.length < 2 || !args[1].equals("tui"));
        } else if (args[0].equalsIgnoreCase("both")) {
            Thread client = new Thread(() -> {
                Controller.getController().start(true);
                Controller.getController().doUI();
            });
            client.setName("ClientMain");
            client.start();
            final Server instance;
            try {
                instance = new Server(ServerConfig.getInstance().port);
            } catch (IOException e) {
                System.out.println("Please chose a different port.");
                return;
            }
            Thread server = new Thread(instance::run);
            server.setName("ServerMain");
            server.start();
        } else {
            printUsage();
        }
    }

    public static void run(String type, int port) {
        switch (type.toLowerCase()) {
            case "server":
                runServer(port);
                break;
            case "client":
                runClient(true);
                break;
            case "both":
                runServer();
                runClient(true);
                break;
        }
    }

    private static void runClient(boolean gui) {
        Controller.getController().start(gui);
        Controller.getController().doUI();
    }

    private static void runServer(int port) {
        Server server;
        try {
            server = new Server(port);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        server.run();
    }


    private static void runServer() {
        runServer(ServerConfig.getInstance().port);
    }

    /**
     * Print a help message to the console to show how to start the application.
     */
    private static void printUsage() {
        System.out.println("Usage:");
        System.out.println("java -jar connectFour.jar [server|client] [gui|tui]");
    }
}
