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
     * TODO: update this value correctly.
     */
    @Getter
    private boolean connected;
    private List<LeaderboardEntry> leaderBoard;

    /**
     * True if this is a server controller, false if it's a client.
     */

    private Controller() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
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
        return chatMessages.subList(amount < chatMessages.size() ? chatMessages.size() - amount : 0, chatMessages.size());
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
        }
        EventQueue.invokeLater(() -> {
            if (gui) {
                frame = new FRMMain();
            } else {
                frame = new TUI();
            }
            frame.init();
            switchTo(Panel.MAIN_MENU);
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
     *
     * @param room
     */
    public void createRoom(Room room) {
        network.sendMessage(Protocol.createMessage(Protocol.Client.CREATEROOM, room.serializeCreation()));
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
            e.printStackTrace();
            //TODO: Display ErrorDialog
        }
    }

    /**
     * Add a server to the list of servers in the config.
     *
     * @param serverName
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
                //Input was wrong.
                //TODO: Display error message
            }
        }
    }

    /**
     * Add a server to the list of servers.
     *
     * @param ip
     * @param port
     */
    public void addServer(String ip, int port) {
        ClientConfig.getInstance().KnownServers.add(new Connection("Added server", ip, port));
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

    public void showError(String message, StackTraceElement[] stackTrace) {
        System.err.println(message);
        for (StackTraceElement stackTraceElement : stackTrace) {
            System.err.println("    " + stackTraceElement);
        }
    }

    public void sendChatMessage(String message) {
        if (isConnected() && getCurrentServer().isChatSupport()) {
            network.sendMessage(Protocol.createMessage(Protocol.Client.SENDMESSAGE, message));
        } else {
            addMessage(new ChatMessage("Game", "That didn't go anywhere, since you aren't connected to any server at the moment."));
            updateChatMessages();
        }
    }

    private void updateChatMessages() {
        if (doGui) {
            ((FRMMain) frame).getChatPanel().update();
        }
    }

    public void setConnected(boolean connected) {
        if (this.connected != connected) {
            this.connected = connected;
            if (connected) {
                frame.setConnected(getCurrentServer().isChatSupport());
            } else {
                frame.setConnected(false);
                switchTo(Controller.Panel.SERVER_BROWSER);
            }
        }
    }

    public List<ServerInfo> pingServers() {
        List<Connection> connections = ClientConfig.getInstance().KnownServers;
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

    public void setLeaderBoard(List<LeaderboardEntry> leaderBoard) {
        this.leaderBoard = leaderBoard;
    }

    public enum Panel {
        MAIN_MENU,
        SINGLE_PLAYER_SETTINGS,
        SERVER_BROWSER,
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
