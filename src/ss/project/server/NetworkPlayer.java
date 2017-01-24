package ss.project.server;


import lombok.Getter;
import ss.project.server.exceptions.InvalidInputException;
import ss.project.shared.Protocol;
import ss.project.shared.game.Engine;
import ss.project.shared.game.Player;
import ss.project.shared.game.Vector2;
import ss.project.shared.game.World;

import java.io.IOException;
import java.util.Scanner;

public class NetworkPlayer extends Player {

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

    public NetworkPlayer(ClientHandler clientHandler) throws IOException {
        super();
        this.clientHandler = clientHandler;
    }

    @Override
    public void doTurn(Engine engine) {
        this.world = engine.getWorld();

        //Read from our input stream.
        try {
            getMoveCoordinates("REPLACE BY INPUTSTREAM");
        } catch (InvalidInputException e) {
            e.printStackTrace();
        }
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
        super.setName(sc.next());
        roomSupport = sc.next().equals("1");
        maxDimensionX = Integer.parseInt(sc.next());
        maxDimensionY = Integer.parseInt(sc.next());
        maxDimensionZ = Integer.parseInt(sc.next());
        maxWinLength = Integer.parseInt(sc.next());
        chatSupport = sc.next().equals("1");
        autoRefresh = sc.next().equals("1");
    }
}
