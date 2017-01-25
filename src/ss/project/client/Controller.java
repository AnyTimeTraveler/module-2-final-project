package ss.project.client;

import lombok.Getter;
import lombok.Setter;
import ss.project.client.networking.Connection;
import ss.project.client.networking.ServerInfo;
import ss.project.client.ui.gui.*;
import ss.project.server.Room;
import ss.project.shared.game.Engine;

import javax.swing.*;
import java.awt.*;

/**
 * Created by simon on 21.01.17.
 */
public class Controller {
    public static Controller controller;
    @Setter
    @Getter
    private Engine engine;
    private FRMMain frame;

    private Controller() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        controller = new Controller();
        controller.start();
    }

    private void start() {
        // Start GUI
        EventQueue.invokeLater(() -> {
            controller.frame = new FRMMain(controller);
            Thread.currentThread().setName("GUI");
            controller.frame.init();
            controller.switchTo(Panel.SERVER_BRWOSER);
        });
    }

    public void restartFrame() {
        frame.dispose();
        start();
    }

    /**
     * Start the game on a seperate thread.
     */
    public void startGame() {
        Thread thread = new Thread(() -> engine.startGame());
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * Join a room.
     *
     * @param room
     */
    public void joinRoom(Room room) {
        System.out.println("Join " + room.toString());
    }

    /**
     * Join the specified server.
     *
     * @param serverInfo
     */
    public void joinServer(ServerInfo serverInfo) {
        System.out.println("Join " + serverInfo.toString());
    }

    /**
     * Add a server to the list of servers in the config.
     *
     * @param serverName
     */
    public void addServer(String serverName) {
        if (serverName != null) {
            if (serverName.contains(":")) {
                String[] data = serverName.split(":");
                try {
                    int port = new Integer(data[1]);
                    if (Connection.validIP(data[0])) {
                        Config.getInstance().KnownServers.add(new Connection("Added server", data[0], port));
                    }
                } catch (NumberFormatException e) {
                    //Input was wrong.
                    return;
                }
            }
        }
    }

    /**
     * Stop the client.
     */
    public void shutdown() {
        System.exit(0);
    }

    public void switchTo(Panel p) {
        frame.switchTo(p.getPanel());
    }

    public void setFrameSize(int width, int height) {
        frame.setSize(width, height);
    }

    public FRMMain getFrame() {
        return frame;
    }

    public enum Panel {
        MAIN_MENU(new PNLMainMenu(controller)),
        SINGLE_PLAYER_SETTINGS(new PNLSinglePlayerSettings(controller)),
        SERVER_BRWOSER(new PNLServerBrowser(controller)),
        OPTIONS(new PNLOptions(controller)),
        MULTI_PLAYER_LOBBY(new PNLMultiPlayerLobby(controller)),
        MULTI_PLAYER_ROOM(new PNLMultiPlayerRoom(controller)),
        MULTI_PLAYER_ROOM_CREATION(new PNLMultiPlayerRoomCreation(controller)),
        GAME(new PNLGame(controller));

        private final GUIPanel panel;

        Panel(GUIPanel panel) {
            this.panel = panel;
        }

        private GUIPanel getPanel() {
            return panel;
        }
    }

}
