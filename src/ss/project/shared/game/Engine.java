package ss.project.shared.game;

import lombok.Getter;
import lombok.Setter;
import ss.project.client.Controller;
import ss.project.client.ui.GameDisplay;
import ss.project.server.Room;
import ss.project.shared.Protocol;
import ss.project.shared.model.GameParameters;

import java.util.*;

/**
 * The main class responsible for the game in both singleplayer and on the server.
 *
 * @see Engine#startGame()
 */
public class Engine {
    /**
     * The reason the game has finished.
     *
     * @see Protocol#WINREASONMAP
     */
    @Getter
    protected Protocol.WinReason winReason;
    /**
     * The id of the player who won, or lost connection.
     */
    @Getter
    protected int winner;
    /**
     * A reference to the world of this engine.
     */
    private World world;
    /**
     * A map of players with reference to their ids.
     */
    private Map<Integer, Player> players = new HashMap<>();
    /**
     * The UI of the game, null if nothing is set.
     */
    private GameDisplay gameDisplay;
    /**
     * True while the gameDisplay is running.
     */
    @Getter
    private boolean gameRunning;
    /**
     * If this is a server, this is the room corresponding to this engine.
     */
    @Setter
    private Room room;

    /**
     * Create a new world and assign players.
     *
     * @param worldSize The size of the world.
     * @param winLength The length required to win in this game.
     * @param players   The players of this game.
     */
    //@ ensures this.world != null;
    //@ ensures this.players.size() == players.length;
    public Engine(Vector3 worldSize, int winLength, Collection<? extends Player> players) {
        this.world = new World(worldSize, winLength);
        for (Player player : players) {
            this.players.put(player.getId(), player);
        }
    }

    /**
     * Creates an engine with parameters from the protocol.
     *
     * @param parameters
     * @param players    A collection of players that will be assigned to this engine.
     * @see Engine#Engine(Vector3, int, Collection)
     */
    public Engine(GameParameters parameters, Collection<? extends Player> players) {
        this(parameters.getWorldSize(), parameters.getWinLength(), players);
    }

    /**
     * @return world of this engine.
     */
    //@ pure;
    public World getWorld() {
        return world;
    }

    /**
     * Get the gameUI of this engine.
     *
     * @return null if no UI was assigned in the first place.
     */
    //@ pure;
    public GameDisplay getUI() {
        return gameDisplay;
    }

    /**
     * Set the gameUI of this engine.
     * If set, will be updated everytime a move is made.
     *
     * @param gameDisplay The UI.
     */
    //@ requires gameDisplay != null;
    //@ ensures getUI().equals(gameDisplay);
    public void setUI(GameDisplay gameDisplay) {
        this.gameDisplay = gameDisplay;
    }

    /**
     * Add a game item to the game at specified coordinates and owner.
     * This will check whether it's a legal move.
     * The game will be finished if this move made the player win.
     * The game will be finished if the board is full.
     * This will update the UI if assigned.
     *
     * @param coordinates Coordinates of the move.
     * @param owner       Owner of the move.
     * @return True if it's a legit move. False if not.
     */
    public boolean addGameItem(Vector2 coordinates, Player owner) {
        boolean result = this.getWorld().addGameItem(coordinates, owner);
        if (result) {
            if (this.getWorld().hasWon(coordinates, owner)) {
                //Someone won!
                finishGame(Protocol.WinReason.WINLENGTHACHIEVED, owner.getId());
                System.out.println(owner.getName() + " won!");
            } else if (getWorld().isFull()) {
                finishGame(Protocol.WinReason.BOARDISFULL, -1);
            }
            if (getUI() != null) {
                getUI().update();
            }
        }
        return result;
    }

    /**
     * Check if a move is valid, but do not place it.
     *
     * @param coordinates The coordinates the move will be checked.
     * @return True if it's a valid move, false if not.
     */
    public boolean isValidMove(Vector2 coordinates) {
        return getWorld().isValidMove(coordinates);
    }

    /**
     * returns the player if within range.
     *
     * @param id Id of the player.
     * @return Null if ID not in range, else the player object.
     */
    //@ pure;
    //@ requires id >= 0;
    public Player getPlayer(int id) {
        if (players.containsKey(id)) {
            return players.get(id);
        }
        return null;
    }

    /**
     * @return the amount of current players, both computerplayer and real.
     */
    //@ ensures \result >= 0;
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
    //@ ensures (\exists Player p; players.containsValue(p); p.equals(player)) ==> \result != null;
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
     * If it's the server it will also check for timeout.
     */
    //@ requires getPlayerCount() <= 0;
    //@ ensures gameRunning;
    public void startGame() {
        boolean isServer = (getUI() == null);
        gameRunning = true;

        //Don't start the gameDisplay if there are no players.
        if (getPlayerCount() <= 1) {
            return;
        }

        Timer timeoutTimer = new Timer();
        TimerTask timeoutTask = new TimerTask() {
            @Override
            public void run() {
                finishGame(Protocol.WinReason.GAMETIMEOUT, -1);
            }
        };
        Collection<Player> turnSet = players.values();
        while (gameRunning) {
            for (Player player : turnSet) {
                if (gameRunning) {
                    if (getUI() != null) {
                        getUI().setCurrentPlayer(player);
                    }
//                    if (isServer) {
//                        timeoutTimer.schedule(timeoutTask, ServerConfig.getInstance().TimeoutInSeconds * 1000);
//                    }
                    player.doTurn(this);
                    timeoutTimer.cancel();
                } else {
                    return;
                }
            }
        }
        timeoutTimer.cancel();
    }

    /**
     * Finish a game with a specified reason. This will stop the game, set the winreason
     * the winner and send a message to the whole room.
     *
     * @param reason The reason this game is finished. See the protocol.
     */
    //@ ensures (reason.equals(WINLENGTHACHIEVED) || reason.equals(PLAYERDISCONNECTED)) ==> getWinner() == playerid;
    //@ ensures getWinReason().equals(reason);
    public void finishGame(Protocol.WinReason reason, int playerid) {
        if (reason.equals(Protocol.WinReason.WINLENGTHACHIEVED) || reason.equals(Protocol.WinReason.PLAYERDISCONNECTED)) {
            winner = playerid;
        }
        gameRunning = false;
        winReason = reason;

        if (getUI() != null) {
            Controller.getController().switchTo(Controller.Panel.GAMEEND);
        } else {
            room.broadcast(Protocol.createMessage(Protocol.Server.NOTIFYEND, reason.getId(), playerid));
        }
    }
}