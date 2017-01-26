package ss.project.shared.game;

import ss.project.client.ui.GameDisplay;

import java.util.HashMap;
import java.util.Map;

public class Engine {
    private World world;
    private Map<Integer, Player> players = new HashMap<>();
    private GameDisplay gameDisplay;

    /**
     * True while the gameDisplay is running.
     */
    private boolean gameRunning;

    /**
     * Create a new world and assign players.
     *
     * @param worldSize
     */
    public Engine(Vector3 worldSize, int winLength, Player[] players) {
        this.world = new World(worldSize, winLength);
        //this.players = players;
        for (Player player : players) {
            this.players.put(player.getId(), player);
        }
    }

    /**
     * @return world of this gameDisplay.
     */
    public World getWorld() {
        return world;
    }

    /**
     * Get the MainWindow of this engine.
     *
     * @return
     */
    public GameDisplay getUI() {
        return gameDisplay;
    }

    /**
     * Set the MainWindow of this engine.
     *
     * @param gameDisplay
     */
    public void setUI(GameDisplay gameDisplay) {
        this.gameDisplay = gameDisplay;
    }

    /**
     * @param coordinates
     * @param owner
     * @return True if it's a legit move. False if not.
     */
    public boolean addGameItem(Vector2 coordinates, Player owner) {
        boolean result = this.getWorld().addGameItem(coordinates, owner);
        if (result) {
            if (this.getWorld().hasWon(coordinates, owner)) {
                //Someone won!
                finishGame(FinishReason.WON);
                System.out.println(owner.getName() + " won!");
            } else if (getWorld().isFull()) {
                finishGame(FinishReason.FULL);
            }
            getUI().update();
        }
        return result;
    }

    /**
     * returns the player if within range.
     *
     * @param id Index number of the player.
     * @return Null if ID not in range, else the player object.
     */
    public Player getPlayer(int id) {
        if (players.containsKey(id)) {
            return players.get(id);
        }
        return null;
    }

    /**
     * @return the amount of current players, both computerplayer and real.
     */
    public int getPlayerCount() {
        return players.size();
    }

    /**
     * Get a player that is not equal to the given player.
     * Used in the AI.
     *
     * @param player
     * @return null if no other player has been found.
     */
    public Player getOtherPlayer(Player player) {
        for (HashMap.Entry<Integer, Player> entry : players.entrySet()) {
            if (!entry.getValue().equals(player)) {
                return entry.getValue();
            }
        }
        return null;
    }

    /**
     * Start the gameDisplay and make every player do turns.
     */
    public void startGame() {
        gameRunning = true;

        //By default start with player 0.
        int currentPlayer = 0;

        //Don't start the gameDisplay if there are no players.
        if (getPlayerCount() <= 0) {
            return;
        }

        while (gameRunning) {
            getPlayer(currentPlayer).doTurn(this);
            currentPlayer++;
            if (currentPlayer >= getPlayerCount()) {
                currentPlayer = 0;
            }
        }
    }

    /**
     * @param reason
     */
    public void finishGame(FinishReason reason) {
        switch (reason) {
            case CRASHED: {
                break;
            }
            case WON: {
                break;
            }
            case FULL: {
                break;
            }
        }
        gameRunning = false;
        System.out.println("Finished game (reason " + reason.toString() + ")");
    }

    /**
     * Reasons why the gameDisplay has finished.
     */
    public enum FinishReason {
        /**
         * Someone has won.
         */
        WON,
        /**
         * The board is full.
         */
        FULL,
        /**
         * The gameDisplay has crashed.
         */
        CRASHED
    }
}