package ss.project.client;

import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;
import ss.project.client.ui.UIFrame;
import ss.project.client.ui.UIPanel;
import ss.project.client.ui.gui.*;
import ss.project.client.ui.tui.*;
import ss.project.server.Room;
import ss.project.shared.Protocol;
import ss.project.shared.game.Engine;
import ss.project.shared.model.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Created by simon on 21.01.17.
 */
public class Controller extends Observable {
    private static final Object CHAT_LOCK = new Object();
    /**
     * A singleton reference to the controller.
     */
    @Getter
    private static Controller controller;

    static {
        controller = new Controller();
    }

    private final Object roomsLock = new Object();
    /**
     * If true it shows the gui, if false the tui.
     */
    @Getter
    private boolean doGui;
    @Setter
    @Getter
    private Engine engine;
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
     */
    @Getter
    private boolean connected;
    @Getter
    private List<LeaderboardEntry> leaderBoard;

    /**
     * True if this is a server controller, false if it's a client.
     */

    private Controller() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException |
                IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        chatMessages = new ArrayList<>();
    }

    /**
     * Add a message to the chat and refresh the chat.
     *
     * @param message The chatmessage that needs to be shown.
     */
    @Synchronized("CHAT_LOCK")
    public void addMessage(ChatMessage message) {
        chatMessages.add(message);
        updateChatMessages();
    }

    /**
     * Get a list of chatmessages.
     *
     * @param amount The amount of chatmessages.
     * @return a list of 'amount' length if available.
     */
    @Synchronized("CHAT_LOCK")
    public List<ChatMessage> getRecentChatMessages(int amount) {
        return chatMessages.subList(
                amount < chatMessages.size() ? chatMessages.size() - amount : 0, chatMessages.size());
    }

    /**
     * Start the gui or tui.
     *
     * @param gui If true, show the gui. If false show the tui.
     */
    public void start(boolean gui) {
        doGui = gui;

        Thread.currentThread().setName("GUI");
        if (gui) {
            Panel.MAIN_MENU.setPanel(new PNLMainMenu(controller));
            Panel.SINGLE_PLAYER_SETTINGS.setPanel(new PNLSinglePlayerSettings(controller));
            Panel.GAME.setPanel(new PNLGame(controller));
            Panel.MULTI_PLAYER_LOBBY.setPanel(new PNLMultiPlayerLobby(controller));
            Panel.MULTI_PLAYER_ROOM.setPanel(new PNLMultiPlayerRoom(controller));
            Panel.MULTI_PLAYER_ROOM_CREATION.setPanel(new PNLMultiPlayerRoomCreation(controller));
            Panel.SERVER_BROWSER.setPanel(new PNLServerBrowser(controller));
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
            Panel.SERVER_BROWSER.setPanel(new TUIServerBrowser());
            Panel.OPTIONS.setPanel(new TUIOptions());
            Panel.LEADERBOARD.setPanel(new TUILeaderboard());
            Panel.GAMEEND.setPanel(new TUIGameEnd());
            frame = new TUI();
        }
    }

    /**
     * Start the client UI, will create a FRMMain if it's a GUI.
     */
    public void doUI() {
        EventQueue.invokeLater(() -> {
            if (doGui) {
                frame = new FRMMain();
            }
            frame.init();
            switchTo(Panel.MAIN_MENU);
        });
    }

    /**
     * Start the server UI.
     */
    public void doServerUI() {
        EventQueue.invokeLater(() -> {
            if (doGui) {
                frame = new FRMMain();
            }
            frame.init();
            switchTo(Panel.GAME);
        });
    }

    /**
     * Restart the complete frame and go back to the default panel.
     * Used when changing fullscreen mode.
     */
    public void restartFrame() {
        frame.dispose();
        start(doGui);
        if (doGui) {
            doUI();
        }
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
     * Refresh the list of rooms.
     */
    public void refreshRoomList() {
        network.sendMessage(Protocol.createMessage(Protocol.Client.GETROOMLIST));
    }

    /**
     * Get the list of rooms of the current server.
     * If the server does not support rooms, we just send a list of 1 room.
     *
     * @return A list of rooms.
     */
    public List<Room> getRooms() {
        if (getCurrentServer().isRoomSupport()) {
            synchronized (roomsLock) {
                return rooms;
            }
        }
        List<Room> fakeRooms = new ArrayList<>();
        ServerInfo info = getCurrentServer();
        fakeRooms.add(new Room(0, info.getMaxPlayers(),
                info.getMaxDimensionX(),
                info.getMaxDimensionY(),
                info.getMaxDimensionZ(),
                info.getMaxWinLength()));
        return fakeRooms;
    }

    /**
     * Set the roomlist of the controller.
     *
     * @param rooms A list of rooms.
     */
    public void setRooms(List<Room> rooms) {
        synchronized (roomsLock) {
            this.rooms = rooms;
        }
        setChanged();
        notifyObservers("UpdateRoom");
    }

    /**
     * Leave the current room we joined.
     */
    public void leaveRoom() {
        if (isConnected()) {
            network.sendMessage(Protocol.createMessage(Protocol.Client.LEAVEROOM));
            controller.switchTo(Panel.MULTI_PLAYER_LOBBY);
        } else {
            controller.switchTo(Panel.SINGLE_PLAYER_SETTINGS);
        }
    }

    /**
     * Create a new room on the server.
     */
    public void createRoom(Room room) {
        network.sendMessage(Protocol.createMessage(Protocol.Client.CREATEROOM, room.serializeCreation()));
    }

    /**
     * Get the server we are currently connected to.
     */
    public ServerInfo getCurrentServer() {
        return network.getServerInfo();
    }

    /**
     * Join the specified server.
     */
    public void joinServer(ServerInfo serverInfo) {
        addMessage(new ChatMessage("Game", "Connecting..."));
        updateChatMessages();
        try {
            if (network != null) {
                network.shutdown();
            }
            network = new Network(serverInfo.getConnection());
            network.start();
            // Exchanging data with server
            while (!network.isReady()) {
                Thread.sleep(10);
            }
            if (serverInfo.isRoomSupport()) {
                controller.switchTo(Panel.MULTI_PLAYER_LOBBY);
            } else {
                controller.switchTo(Panel.MULTI_PLAYER_ROOM);
            }
        } catch (IOException | InterruptedException e) {
            showError("Could not connect to server: " + serverInfo.getConnection().getAddress() +
                    ":" + serverInfo.getConnection().getPort(), e.getStackTrace());
        }
    }

    /**
     * Add a server to the list of servers in the config.
     */
    public void addServer(String serverName) {
        if (serverName != null && serverName.contains(":")) {
            String[] data = serverName.split(":");
            try {
                int port = Integer.parseInt(data[1]);
                if (Connection.validIP(data[0])) {
                    addServer(data[0], port);
                }
            } catch (NumberFormatException e) {
                showError("Adding server failed:" + serverName +
                        " is in the wrong format or not a validIP.", e.getStackTrace());
            }
        }
    }

    /**
     * Add a server to the list of servers.
     */
    public void addServer(String ip, int port) {
        ClientConfig.getInstance().knownServers.add(new Connection("Added server", ip, port));
        ClientConfig.getInstance().toFile();
    }

    /**
     * Remove a server from the config and save.
     *
     * @param serverInfo
     */
    public void removeServer(ServerInfo serverInfo) {
        ClientConfig.getInstance().knownServers.remove(serverInfo.getConnection());
        ClientConfig.getInstance().toFile();
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

    /**
     * Show an error on the UI and print stackTrace.
     *
     * @param message
     * @param stackTrace
     */
    public void showError(String message, StackTraceElement[] stackTrace) {
        frame.showError(message);

        System.err.println(message);
        for (StackTraceElement stackTraceElement : stackTrace) {
            System.err.println("    " + stackTraceElement);
        }
    }

    /**
     * Show an error on the UI.
     *
     * @param message message to be shown as information.
     */
    public void showError(String message) {
        frame.showError(message);
    }

    /**
     * Send a chat message if we are connected and the server supports chat.
     *
     * @param message The message that needs to be send.
     */
    public void sendChatMessage(String message) {
        if (isConnected() && getCurrentServer().isChatSupport()) {
            network.sendMessage(Protocol.createMessage(
                    Protocol.Client.SENDMESSAGE, ClientConfig.getInstance().playerName, message));
        } else {
            addMessage(new ChatMessage("Game",
                    "That didn't go anywhere, since you aren't connected to any server at the moment."));
            updateChatMessages();
        }
    }

    /**
     * Update the chat messages at the current frame.
     */
    private void updateChatMessages() {
        if (doGui) {
            ((FRMMain) frame).getChatPanel().update();
        }
    }

    /**
     * Set whether we are connected.
     * Will update the frame if we have one and switch to the server browser if we disconnect.
     *
     * @param connected
     */
    void setConnected(boolean connected) {
        if (this.connected != connected) {
            this.connected = connected;
            if (frame != null) {
                if (connected) {
                    frame.setConnected(getCurrentServer().isChatSupport());
                } else {
                    frame.setConnected(false);
                    switchTo(Controller.Panel.SERVER_BROWSER);
                }
            }
        }
    }

    /***
     * Ping all servers from the list of server and receive their ServerInfo.
     * @return A new list containing information about all servers.
     * (whether it's reachable and specifications).
     */
    public List<ServerInfo> pingServers() {
        List<Connection> connections = ClientConfig.getInstance().knownServers;
        List<ServerInfo> serverInfos = new ArrayList<>();
        for (Connection connection : connections) {
            try {
                serverInfos.add(new Network(connection).ping());
            } catch (IOException e) {
                serverInfos.add(new ServerInfo(ServerInfo.Status.OFFLINE, connection, 0, false, 0, 0, 0, 0, false));
            }
        }
        return serverInfos;
    }

    /**
     * Set the leaderboard data and notify all observers.
     *
     * @param leaderBoard
     */
    void setLeaderBoard(List<LeaderboardEntry> leaderBoard) {
        this.leaderBoard = leaderBoard;
        setChanged();
        notifyObservers("UpdateLeaderBoard");
    }

    /**
     * Request a leaderboard update from the server.
     */
    public void requestLeaderBoard() {
        network.sendMessage(Protocol.createMessage(Protocol.Client.REQUESTLEADERBOARD));
    }

    /**
     * Close the current frame.
     */
    public void closeFrame() {
        frame.dispose();
    }

    /**
     * The several screens possible in the UI.
     *
     * @see Controller#switchTo(Panel)
     */
    public enum Panel {
        /**
         * The start of the program, select different menus from here.
         */
        MAIN_MENU,
        /**
         * The creation screen of a singleplayer game.
         */
        SINGLE_PLAYER_SETTINGS,
        /**
         * The server browser. Will ping all servers and show them in the menu.
         */
        SERVER_BROWSER,
        /**
         * Change various properties of both the game as the menu.
         */
        OPTIONS,
        /**
         * Show the rooms of the server if the server supports rooms.
         */
        MULTI_PLAYER_LOBBY,
        /**
         * Waiting for a room to start.
         */
        MULTI_PLAYER_ROOM,
        /**
         * Create a new room at the server if it supports rooms.
         */
        MULTI_PLAYER_ROOM_CREATION,
        /**
         * The panel of the game, will allow you to give input if you're a humanplayer.
         */
        GAME,
        /**
         * If supported by the server, gets and hows all leaderboard entries.
         */
        LEADERBOARD,
        /**
         * The screen at the end of the game. Shows why it ended and allows you to go back to the lobby.
         */
        GAMEEND;

        @Getter
        @Setter
        private UIPanel panel;
    }
}
