package ss.project.shared;


import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;
import ss.project.server.ClientHandler;
import ss.project.server.Room;
import ss.project.shared.exceptions.InvalidInputException;
import ss.project.shared.game.Engine;
import ss.project.shared.game.Player;
import ss.project.shared.game.Vector2;
import ss.project.shared.model.Color;

import java.io.IOException;
import java.util.Scanner;

public class NetworkPlayer extends Player implements Serializable {


    /**
     * DO NOT USE THIS DIRECTLY!
     * Use getNextid().
     */
    private static int nextId;
    private final Object moveLock = new Object();
    @Getter
    private ClientHandler clientHandler;
    /**
     * The max players this networkplayer can handle.
     */
    @Getter
    private int maxPlayers;
    /**
     * If true, this client can handle rooms.
     */
    @Getter
    private boolean roomSupport;
    /**
     * The max X size of the world this client can handle.
     */
    @Getter
    private int maxDimensionX;
    /**
     * The max Y size of the world this client can handle.
     */
    @Getter
    private int maxDimensionY;
    /**
     * The max Z size of the world this client can handle.
     */
    @Getter
    private int maxDimensionZ;
    /**
     * The maximum win length this client can handle.
     */
    @Getter
    private int maxWinLength;
    /**
     * If true this client has chat support.
     */
    @Getter
    private boolean chatSupport;
    /**
     * This client wants to be notified when the room list changes on the server.
     */
    @Getter
    private boolean autoRefresh;
    /**
     * The current room this networkplayer is in.
     */
    @Setter
    @Getter
    private Room currentRoom;
    /**
     * If true, this networkplayer is playing a game right now.
     */
    @Setter
    @Getter
    private boolean inGame;
    private Vector2 move;
    @Getter
    private boolean expectingMove;
    /**
     * The color of this networkplayer.
     */
    @Getter
    private Color color;

    /**
     * Create a networkplayer from a clienthandler.
     *
     * @param clientHandler
     * @throws IOException
     */
    public NetworkPlayer(ClientHandler clientHandler) throws IOException {
        super();
        setId(getNextId());
        this.clientHandler = clientHandler;
        inGame = false;
        color = Color.getRandomColor();
    }

    /**
     * Create a new networkplayer with a specified ID, name and color.
     *
     * @param id
     * @param name
     * @param color
     */
    public NetworkPlayer(int id, String name, Color color) {
        setId(id);
        setName(name);
        this.color = color;
    }

    /**
     * Get the next ID that is unique.
     *
     * @return
     */
    @Synchronized
    private static int getNextId() {
        return nextId++;
    }

    /**
     * Create a networkPlayer from a string line followed from protocol.
     *
     * @param line
     * @return
     */
    public static NetworkPlayer fromString(String line) {
        String[] params = line.split(Protocol.PIPE_SYMBOL);
        return new NetworkPlayer(Integer.parseInt(params[0]), params[1], Color.fromString(params[2]));
    }

    /**
     * Called everytime this player should set a gameitem.
     * In this case the networkplayer waits from input from a client, verifies it and
     * sends it to all clients.
     *
     * @param engine
     */
    @Override
    public void doTurn(Engine engine) {
        move = null;
        expectingMove = true;
        sendMoveNotification();
        synchronized (moveLock) {
            try {
                moveLock.wait();
            } catch (InterruptedException e) {
                //e.printStackTrace();
                //The game stopped for an unexpected reason (a player disconnects).
                return;
            }
        }
        if (engine.getWorld().addGameItem(move, this)) {
            currentRoom.broadcast(Protocol.createMessage(Protocol.Server.NOTIFYMOVE, getId(), move.getX(), move.getY()));
            if (engine.getWorld().hasWon(new Vector2(move.getX(), move.getY()), this)) {
                currentRoom.endGame(Protocol.WinReason.WINLENGTHACHIEVED, getId());
            } else {
                if (engine.getWorld().isFull()) {
                    currentRoom.endGame(Protocol.WinReason.BOARDISFULL, -1);
                }
            }
        } else {
            clientHandler.sendMessage(Protocol.createMessage(Protocol.Server.ERROR, 5));
            sendMoveNotification();
            doTurn(engine);
        }
    }

    private void sendMoveNotification() {
        currentRoom.broadcast(Protocol.createMessage(Protocol.Server.TURNOFPLAYER, getId()));
    }

    /**
     * Input will be “makeMove 2 3”
     *
     * @param rawInput
     * @return
     */
    private Vector2 getMoveCoordinates(String rawInput) throws InvalidInputException {
        String[] parts = rawInput.split(" ");
        if (parts.length < 3) {
            throw new InvalidInputException(rawInput);
        }

        try {
            int x = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);

            return new Vector2(x, y);
        } catch (NumberFormatException e) {
            throw new InvalidInputException(rawInput);
        }
    }

    /**
     * Set the capabilities of this networkplayer from a string from the protocol.
     *
     * @param message
     * @throws NumberFormatException If an integer is expected, but found something inconvertable to numbers.
     */
    public void setCapabilitiesFromString(String message) throws NumberFormatException {
        Scanner sc = new Scanner(message);
        if (message.split(" ")[0].equals(Protocol.Client.SENDCAPABILITIES.getMessage())) {
            sc.next();
        }
        maxPlayers = Integer.parseInt(sc.next());
        setName(sc.next());
        roomSupport = sc.next().equals("1");
        maxDimensionX = Integer.parseInt(sc.next());
        maxDimensionY = Integer.parseInt(sc.next());
        maxDimensionZ = Integer.parseInt(sc.next());
        maxWinLength = Integer.parseInt(sc.next());
        chatSupport = sc.next().equals("1");
        autoRefresh = sc.next().equals("1");
        sc.close();
    }

    public void setMove(int x, int y) {
        synchronized (moveLock) {
            this.move = new Vector2(x, y);
            moveLock.notify();
        }
        expectingMove = false;
    }

    @Override
    public String serialize() {
        return String.join("|", String.valueOf(getId()), getName(), getColor().serialize());
    }
}
