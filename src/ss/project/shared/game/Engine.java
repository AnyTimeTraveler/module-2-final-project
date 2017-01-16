package ss.project.shared.game;

public class Engine {
    private World world;
    private Player[] players;

    /**
     * True while the game is running.
     */
    private boolean gameRunning;

    /**
     * Create a new world and create players. TODO: remove the playerAmount and
     * aiAmount out of this constructor. The Engine should run multiple games.
     *
     * @param worldSize
     */
    public Engine(Vector3 worldSize, Player[] players) {
        this.world = new World(worldSize, this);

        this.players = players;
        startGame();
    }

    /**
     * @return world of this game.
     */
    public World getWorld() {
        return world;
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
     * @return the amount of current players, both ai and real.
     */
    public int getPlayerCount() {
        return players.length;
    }

    /**
     * Start the game and make every player do turns.
     */
    private void startGame() {
        gameRunning = true;

        //By default start with player 0.
        int currentPlayer = 0;

        //Don't start the game if there are no players.
        if (getPlayerCount() <= 0) {
            return;
        }

        while (gameRunning) {
            getPlayer(currentPlayer).doTurn(this.getWorld());

            currentPlayer++;
            if (currentPlayer >= getPlayerCount()) {
                currentPlayer = 0;
            }
        }
    }

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