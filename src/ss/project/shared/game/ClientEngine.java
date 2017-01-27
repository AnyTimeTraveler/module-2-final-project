package ss.project.shared.game;

import lombok.Getter;
import ss.project.client.Controller;
import ss.project.client.networking.Network;
import ss.project.shared.Protocol;

/**
 * Created by fw on 26/01/2017.
 */
public class ClientEngine extends Engine {
    @Getter
    Network network;
    @Getter
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
    @Override
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
    @Override
    public void finishGame(Protocol.WinReason reason, int playerID) {
        //We shouldn't do anything, we should just wait til notifyEnd is called.
    }

    /**
     * Sets a players move.
     *
     * @param playerId id of player
     * @param x        coordinate
     * @param y        coordinate
     */
    public void notifyMove(int playerId, int x, int y) {
        super.addGameItem(new Vector2(x, y), getPlayer(playerId));
    }

    /**
     * Game has ended.
     *
     * @param reason        Reason for end of game
     * @param winningPlayer player (optional)
     */
    public void notifyEnd(int reason, int winningPlayer) {
        Protocol.WinReason winReason = Protocol.getWinReason(reason);
        switch (winReason) {
            case BOARDISFULL: {
                super.finishGame(Protocol.WinReason.BOARDISFULL, -1);
                break;
            }
            case GAMETIMEOUT: {
                System.out.println("What to do now...? Game has timeout...");
                break;
            }
            case PLAYERDISCONNECTED: {
                System.out.println(getPlayer(winningPlayer) + " disconnected the room");
                break;
            }
            case WINLENGTHACHIEVED: {
                System.out.println(getPlayer(winningPlayer) + " won the game...!");
                break;
            }
        }
        this.winReason = winReason;
        winner = winningPlayer;
        Controller.getController().switchTo(Controller.Panel.GAMEEND);
    }

    /**
     * Sets current players turn.
     *
     * @param playerID
     */
    public void setTurn(int playerID) {
        getUI().setCurrentPlayer(getPlayer(playerID));
    }
}
