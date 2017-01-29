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
     */
    private static int nextId;
    private final Object moveLock = new Object();
    @Getter
    private ClientHandler clientHandler;
    @Getter
    private int maxPlayers;
    @Getter
    private boolean roomSupport;
    @Getter
    private int maxDimensionX;
    @Getter
    private int maxDimensionY;
    @Getter
    private int maxDimensionZ;
    @Getter
    private int maxWinLength;
    @Getter
    private boolean chatSupport;
    @Getter
    private boolean autoRefresh;
    @Setter
    @Getter
    private Room currentRoom;
    @Setter
    @Getter
    private boolean inGame;
    private Vector2 move;
    @Getter
    private boolean expectingMove;
    @Getter
    private Color color;

    public NetworkPlayer(ClientHandler clientHandler) throws IOException {
        super();
        setId(getNextId());
        this.clientHandler = clientHandler;
        inGame = false;
        color = Color.getRandomColor();
    }

    public NetworkPlayer(int id, String name, Color color) {
        setId(id);
        setName(name);
        this.color = color;
    }

    @Synchronized
    private static int getNextId() {
        return nextId++;
    }

    public static NetworkPlayer fromString(String line) {
        String[] params = line.split(Protocol.PIPE_SYMBOL);
        return new NetworkPlayer(Integer.parseInt(params[0]), params[1], Color.fromString(params[2]));
    }

    @Override
    public void doTurn(Engine engine) {
        move = null;
        expectingMove = true;
        sendMoveNotification();
        synchronized (moveLock) {
            try {
                moveLock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }
        if (engine.getWorld().addGameItem(move, this)) {
            currentRoom.broadcast(Protocol.createMessage(Protocol.Server.NOTIFYMOVE, getId(), move.getX(), move.getY()));
            if (engine.getWorld().hasWon(new Vector2(move.getX(), move.getY()), this)) {
                currentRoom.endGame(Protocol.WinReason.WINLENGTHACHIEVED, getId());
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
