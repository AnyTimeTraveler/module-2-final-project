package ss.project.client;

import lombok.Getter;
import lombok.Setter;
import ss.project.client.networking.Connection;
import ss.project.client.networking.Network;
import ss.project.client.networking.ServerInfo;
import ss.project.client.ui.UIFrame;
import ss.project.client.ui.UIPanel;
import ss.project.client.ui.gui.*;
import ss.project.client.ui.tui.*;
import ss.project.server.Room;
import ss.project.shared.ChatMessage;
import ss.project.shared.Protocol;
import ss.project.shared.game.ClientEngine;
import ss.project.shared.game.Engine;
import ss.project.shared.game.Vector3;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by simon on 21.01.17.
 */
public class Controller {
    @Getter
    private static Controller controller;

    static {
        controller = new Controller();
    }

    /**
     * If true it shows the gui, if false the tui.
     */
    @Getter
    private boolean doGui;
    @Setter
    @Getter
    private Engine engine;
    @Setter
    private List<Room> rooms;
    private UIFrame frame;
    @Getter
    private Network network;
    @Getter
    @Setter
    private Room currentRoom;
    private List<ChatMessage> chatMessages;
    /**
     * True if we are online and connected. False if not.
     * TODO: update this value correctly.
     */
    @Getter
    private boolean isConnected;

    private Controller() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        chatMessages = new ArrayList<>();
    }

    public static void main(String[] args) {
        controller.start((args.length == 0 || !args[0].equals("tui")));
    }

    public void addMessage(ChatMessage message) {
        chatMessages.add(message);
    }

    public List<ChatMessage> getRecentChatMessages(int amount) {
        return chatMessages.subList(amount < chatMessages.size() ? chatMessages.size() - amount : 0, chatMessages.size());
    }

    /**
     * Start the gui or tui.
     *
     * @param gui If true, show the gui. If false show the tui.
     */
    private void start(boolean gui) {
        doGui = gui;
        setConnected(false);

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
            Panel.LEADERBOARD.setPanel(new PNLLeaderboard(controller));
            Panel.GAMEEND.setPanel(new PNLGameEnd(controller));
        } else {
            Panel.MAIN_MENU.setPanel(new TUIMainMenu());
            Panel.SINGLE_PLAYER_SETTINGS.setPanel(new TUISinglePlayerSettings());
            Panel.GAME.setPanel(new TUIGame());
            Panel.MULTI_PLAYER_LOBBY.setPanel(new TUIMultiPlayerLobby());
            Panel.MULTI_PLAYER_ROOM.setPanel(new TUIMultiPlayerRoom());
            Panel.MULTI_PLAYER_ROOM_CREATION.setPanel(new TUIMultiPlayerRoomCreation());
            Panel.SERVER_BRWOSER.setPanel(new TUIServerBrowser());
            Panel.OPTIONS.setPanel(new TUIOptions());
            Panel.LEADERBOARD.setPanel(new TUILeaderboard());
            Panel.GAMEEND.setPanel(new TUIGameEnd());
        }
        EventQueue.invokeLater(() -> {
            if (gui) {
                frame = new FRMMain();
                frame.init();
                switchTo(Panel.MAIN_MENU);
            } else {
                frame = new TUI();
                frame.init();
                switchTo(Panel.MAIN_MENU);
            }
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
     * Send a request to the server to join a room.
     *
     * @param room Room to join
     */
    public void joinRoom(Room room) {
        currentRoom = room;
        network.sendMessage(Protocol.createMessage(Protocol.Client.JOINROOM, room.getId()));
    }

    /**
     * Get the list of rooms of the current server.
     *
     * @return A list of rooms.
     */
    public List<Room> getRooms() {
        if (getCurrentServer().isRoomSupport()) {
            //Get all rooms from this server, it supports lobbies.
            Room[] rooms = new Room[3];
            for (int i = 0; i < rooms.length; i++) {
                rooms[i] = new Room(5 - i, new Vector3(i, i + 1, i + 2), 4);
            }
            return null;
        } else {
            //This server does not support lobbies, show only one room with the server settings.
            ServerInfo curServer = getCurrentServer();

        }
        return null;
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
     * Create a new room on the server.
     *
     * @param room
     */
    public void createRoom(Room room) {
        network.sendMessage(Protocol.createMessage(Protocol.Client.CREATEROOM, room));

    }

    /**
     * Get the server we are currently connected to.
     *
     * @return
     */
    public ServerInfo getCurrentServer() {
        return network.getServerInfo();
    }

    /**
     * Join the specified server.
     *
     * @param serverInfo
     */
    public void joinServer(ServerInfo serverInfo) {
        System.out.println("Join " + serverInfo.toString());
        controller.switchTo(Controller.Panel.MULTI_PLAYER_LOBBY);
        //First join, then update the connected.
        setConnected(true);
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
                    int port = Integer.parseInt(data[1]);
                    if (Connection.validIP(data[0])) {
                        Config.getInstance().KnownServers.add(new Connection("Added server", data[0], port));
                        Config.getInstance().toFile();
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

    public void showError(String message, StackTraceElement[] stackTrace) {
        System.err.println(message);
        for (StackTraceElement stackTraceElement : stackTrace) {
            System.err.println("    " + stackTraceElement);
        }
    }

    public void sendChatMessage(String input) {
        if (isConnected()) {
            if (getEngine() instanceof ClientEngine) {
                System.out.println("Send message: " + input);
                Network network = ((ClientEngine) getEngine()).getNetwork();
                network.sendMessage(Protocol.createMessage(Protocol.Client.SENDMESSAGE, ((ClientEngine) getEngine()).getPlayerID(), input));
            }
        }
    }

    public void setConnected(boolean connected) {
        this.isConnected = connected;
        if (connected) {
            frame.setChatEnabled(getCurrentServer().isChatSupport());
        } else {
            frame.setChatEnabled(false);
        }
    }

    public enum Panel {
        MAIN_MENU,
        SINGLE_PLAYER_SETTINGS,
        SERVER_BRWOSER,
        OPTIONS,
        MULTI_PLAYER_LOBBY,
        MULTI_PLAYER_ROOM,
        MULTI_PLAYER_ROOM_CREATION,
        GAME,
        LEADERBOARD,
        GAMEEND;

        @Getter
        @Setter
        private UIPanel panel;
    }
}
