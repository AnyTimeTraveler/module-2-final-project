package ss.project.client;

import lombok.Getter;
import lombok.Setter;
import ss.project.client.networking.Connection;
import ss.project.client.networking.ServerInfo;
import ss.project.client.ui.UIFrame;
import ss.project.client.ui.UIPanel;
import ss.project.client.ui.gui.*;
import ss.project.client.ui.tui.*;
import ss.project.server.Room;
import ss.project.shared.game.Engine;
import ss.project.shared.game.Vector3;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Created by simon on 21.01.17.
 */
public class Controller {
    public static Controller controller;
    /**
     * If true it shows the gui, if false the tui.
     */
    private static boolean doGui;
    @Setter
    @Getter
    private Engine engine;
    @Setter
    @Getter
    private List<Room> rooms;
    private UIFrame frame;

    private Controller() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        controller = new Controller();
        doGui = true;
        if (args.length >= 1) {
            if (args[0].equals("tui")) {
                doGui = false;
            }
        }
        controller.start(doGui);
    }

    /**
     * Start the gui or tui.
     *
     * @param gui If true, show the gui. If false show the tui.
     */
    private void start(boolean gui) {
        EventQueue.invokeLater(() -> {
            if (gui) {
                controller.frame = new FRMMain(controller);
            } else {
                controller.frame = new TUI();
            }

            Thread.currentThread().setName("GUI");


            if (gui) {
                Panel.MAIN_MENU.setPanel(new PNLMainMenu(controller));
                Panel.SINGLE_PLAYER_SETTINGS.setPanel(new PNLSinglePlayerSettings(controller));
                Panel.GAME.setPanel(new PNLGame(controller));
                Panel.MULTI_PLAYER_LOBBY.setPanel(new PNLMultiPlayerLobby(controller));
                Panel.MULTI_PLAYER_ROOM.setPanel(new PNLMultiPlayerRoom(controller));
                Panel.MULTI_PLAYER_ROOM_CREATION.setPanel(new PNLMultiPlayerRoomCreation(controller));
                Panel.SERVER_BRWOSER.setPanel(new PNLServerBrowser(controller));
                Panel.OPTIONS.setPanel(new PNLOptions(controller));
            } else {
                Panel.MAIN_MENU.setPanel(new TUIMainMenu());
                Panel.SINGLE_PLAYER_SETTINGS.setPanel(new TUISinglePlayerSettings());
                Panel.GAME.setPanel(new TUIGame());
                Panel.MULTI_PLAYER_LOBBY.setPanel(new TUIMultiPlayerLobby());
                Panel.MULTI_PLAYER_ROOM.setPanel(new TUIMultiPlayerRoom());
                Panel.MULTI_PLAYER_ROOM_CREATION.setPanel(new TUIMultiPlayerRoomCreation());
                Panel.SERVER_BRWOSER.setPanel(new TUIServerBrowser());
                Panel.OPTIONS.setPanel(new TUIOptions());
            }

            controller.switchTo(Panel.MAIN_MENU);
            controller.frame.init();
        });
    }

    /**
     * Restart the complete frame and go back to the default panel.
     * Used when changing fullscreen mode.
     */
    public void restartFrame() {
        frame.dispose();
        start(doGui);
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
        //TODO: implement
        System.out.println("Join " + room.toString());
        controller.switchTo(Panel.MULTI_PLAYER_ROOM);
    }

    /**
     * Get the room we are currently connected to.
     *
     * @return A room instancen of the room we are connected to.
     */
    public Room getCurrentRoom() {
        //TODO: implement
        return new Room(5, Vector3.ONE, 3);
    }

    /**
     * Get the list of rooms of the current server.
     *
     * @return An array of rooms.
     */
    public Room[] getRooms() {
        if (getCurrentServer().isRoomSupport()) {
            //Get all rooms from this server, it supports lobbies.
            Room[] rooms = new Room[3];
            for (int i = 0; i < rooms.length; i++) {
                rooms[i] = new Room(5 - i, new Vector3(i, i + 1, i + 2), 4);
            }
            return rooms;
        } else {
            //This server does not support lobbies, show only one room with the server settings.
            ServerInfo curServer = getCurrentServer();
            return new Room[]{
                    new Room(curServer.getMaxPlayers(), curServer.getWorldSize(), curServer.getMaxWinLength())
            };
        }
    }

    /**
     * Leave the current room we joined.
     */
    public void leaveRoom() {
        System.out.println("Leaving room");
        //TODO: implement
        controller.switchTo(Panel.MULTI_PLAYER_LOBBY);
    }

    /**
     * Get the server we are currently connected to.
     *
     * @return
     */
    public ServerInfo getCurrentServer() {
        //TODO: Get the current server.
        return null;
    }

    /**
     * Join the specified server.
     *
     * @param serverInfo
     */
    public void joinServer(ServerInfo serverInfo) {
        System.out.println("Join " + serverInfo.toString());
        controller.switchTo(Controller.Panel.MULTI_PLAYER_LOBBY);
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

    /**
     * Go to a specific panel for the GUI.
     *
     * @param p panel that needs to be shown.
     */
    public void switchTo(Panel p) {
        frame.switchTo(p.getPanel());
    }

    public void setFrameSize(int width, int height) {
        frame.setSize(width, height);
    }

    public enum Panel {
        MAIN_MENU(),
        SINGLE_PLAYER_SETTINGS(),
        SERVER_BRWOSER(),
        OPTIONS(),
        MULTI_PLAYER_LOBBY(),
        MULTI_PLAYER_ROOM(),
        MULTI_PLAYER_ROOM_CREATION(),
        GAME();

        private UIPanel panel;

        private UIPanel getPanel() {
            return panel;
        }

        private void setPanel(UIPanel panel) {
            this.panel = panel;
        }
    }
}
