package ss.project.shared.game;

import ss.project.client.networking.Network;
import ss.project.shared.Protocol;

/**
 * Created by fw on 26/01/2017.
 */
public class ClientEngine extends Engine {
    Network network;
    int playerID;

    /**
     * Create a new world and assign players.
     *
     * @param worldSize
     * @param winLength
     * @param players
     */
    public ClientEngine(Vector3 worldSize, int winLength, Player[] players, Network network, int playerID) {
        super(worldSize, winLength, players);
        this.network = network;
        this.playerID = playerID;
    }

    /**
     * Start the gameDisplay and make every player do turns.
     */
    @Override
    public void startGame() {

    }

    /**
     * @param coordinates
     * @param owner
     * @return True if it's a legit move. False if not.
     */
    public boolean addGameItem(Vector2 coordinates, Player owner) {
        if (owner.getId() == playerID) {
            //It's us, we should check whether it's valid.

            if (super.addGameItem(coordinates, owner)) {
                //It's valid, now send a message to the server.
                network.sendMessage(Protocol.createMessage(Protocol.Client.MAKEMOVE, coordinates.getX(), coordinates.getY()));
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * @param reason
     */
    public void finishGame(FinishReason reason) {

    }
}
