package ss.project;

import ss.project.client.Controller;
import ss.project.server.Server;
import ss.project.shared.model.ServerConfig;

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
            printUsage();
            return;
        }
        if (args[0].equalsIgnoreCase("server")) {
            Server server = new Server(ServerConfig.getInstance().Host, ServerConfig.getInstance().Port);
            server.run();
        } else if (args[0].equalsIgnoreCase("client")) {
            Controller.getController().start(args.length < 2 || !args[1].equals("tui"));
        } else if (args[0].equalsIgnoreCase("both")) {
            Thread client = new Thread(() -> Controller.getController().start(true));
            client.setName("ClientMain");
            client.start();
            Thread server = new Thread(() -> new Server(ServerConfig.getInstance().Host, ServerConfig.getInstance().Port).run());
            server.setName("ServerMain");
            server.start();
        }
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
