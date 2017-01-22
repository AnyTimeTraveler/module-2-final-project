package ss.project.server;


import ss.project.server.exceptions.InvalidInputException;
import ss.project.shared.game.Engine;
import ss.project.shared.game.Player;
import ss.project.shared.game.Vector2;
import ss.project.shared.game.World;

import java.io.IOException;

public class NetworkPlayer extends Player {

    private ClientHandler clientHandler;
    private World world;

    public NetworkPlayer(String name, ClientHandler clientHandler) throws IOException {
        super(name);
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
}
