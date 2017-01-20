package ss.project.shared.game;

import ss.project.client.ui.GameDisplay;

public class Engine {
    private World world;
    private Player[] players;
    private GameDisplay game;

    /**
     * True while the game is running.
     */
    private boolean gameRunning;

    /**
     * Create a new world and assign players.
     *
     * @param worldSize
     */
    public Engine(Vector3 worldSize, Player[] players) {
        this.world = new World(worldSize);
        this.players = players;
    }

    /**
     * @return world of this game.
     */
    public World getWorld() {
        return world;
    }

    /**
     * Get the UI of this engine.
     *
     * @return
     */
    public GameDisplay getUI() {
        return game;
    }

    /**
     * Set the UI of this engine.
     *
     * @param game
     */
    public void setUI(GameDisplay game) {
        this.game = game;
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
            getUI().refresh();
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
        if (id < players.length && id >= 0) {
            return players[id];
        }
        return null;
    }

    /**
     * @return the amount of current players, both computerplayer and real.
     */
    public int getPlayerCount() {
        return players.length;
    }

    /**
     * Get a player that is not equal to the given player.
     * Used in the AI.
     *
     * @param player
     * @return null if no other player has been found.
     */
    public Player getOtherPlayer(Player player) {
        for (int i = 0; i < getPlayerCount(); i++) {
            if (!getPlayer(i).equals(player)) {
                return getPlayer(i);
            }
        }
        return null;
    }

    /**
     * Start the game and make every player do turns.
     */
    public void startGame() {
        gameRunning = true;

        //By default start with player 0.
        int currentPlayer = 0;

        //Don't start the game if there are no players.
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
     * Reasons why the game has finished.
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
         * The game has crashed.
         */
        CRASHED
    }
}