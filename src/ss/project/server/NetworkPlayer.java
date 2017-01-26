package ss.project.server;


import lombok.Getter;
import lombok.Setter;
import ss.project.shared.Protocol;
import ss.project.shared.exceptions.InvalidInputException;
import ss.project.shared.game.Engine;
import ss.project.shared.game.Player;
import ss.project.shared.game.Vector2;
import ss.project.shared.game.World;

import java.io.IOException;
import java.util.Scanner;

public class NetworkPlayer extends Player {

    private final Object moveLock;
    @Getter
    private ClientHandler clientHandler;
    private World world;
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

    public NetworkPlayer(ClientHandler clientHandler) throws IOException {
        super();
        this.clientHandler = clientHandler;
        inGame = false;
        moveLock = new Object();
    }

    @Override
    public void doTurn(Engine engine) {
        this.world = engine.getWorld();
        move = null;
        expectingMove = true;
        sendMoveNotification();
        synchronized (moveLock) {
            try {
                moveLock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                // TODO: Undecided
            }
        }
        engine.getWorld().addGameItem(move, this);
    }

    private void sendMoveNotification() {

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
    }
}
