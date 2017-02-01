package ss.project.shared.computerplayer;

import lombok.Getter;
import ss.project.shared.game.Engine;
import ss.project.shared.game.Player;
import ss.project.shared.game.Vector2;
import ss.project.shared.game.World;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Smartest AI we have.
 * Min Max algorithm with alpha beta pruning.
 * Uses the same threading system as the Min Max.
 * <p>
 * Created by fw on 16/01/2017.
 */
public class MinMaxAlphaBetaComputerPlayer extends ComputerPlayer {
    protected Vector2[] vector2Cache;
    Random random = new Random();
    @Getter
    private int depth;
    private Player opponent;
    private World worldCopy;
    private Future<Integer>[][] workers;
    private ExecutorService executor;

    /**
     * create a computer player with the specified AI.
     */
    public MinMaxAlphaBetaComputerPlayer(int depth) {
        super();
        System.out.println("Initialized");
        this.depth = depth;
    }

    public MinMaxAlphaBetaComputerPlayer(String name, int depth) {
        super(name);
        System.out.println("Initialized");
        this.depth = depth;
    }

    public MinMaxAlphaBetaComputerPlayer() {
        super();
        this.depth = 7;
    }

    private void initialize(Engine engine) {
        if (worldCopy == null) {
            worldCopy = new World(engine.getWorld().getSize(), engine.getWorld().getWinLength());
        }
        if (opponent == null) {
            opponent = engine.getOtherPlayer(this);
        }
        if (workers == null) {
            //If this warning is fixed, that resolves in a compiler error. We therefore couldn't fix it.
            workers = new Future[engine.getWorld().getSize().getX()][engine.getWorld().getSize().getY()];
        }
        if (executor == null) {
            executor = Executors.newWorkStealingPool();
        }
        engine.getWorld().writeTo(worldCopy);

        int newSize = worldCopy.getSize().getX() * worldCopy.getSize().getY();
        if (vector2Cache == null || vector2Cache.length != newSize) {
            vector2Cache = new Vector2[newSize];
            int index = 0;
            for (int x = 0; x < worldCopy.getSize().getX(); x++) {
                for (int y = 0; y < worldCopy.getSize().getY(); y++) {
                    vector2Cache[index] = new Vector2(x, y);
                    index++;
                }
            }
        }
    }

    @Override
    public void doTurn(Engine engine) {
        initialize(engine);

        Vector2 bestPos = getBestPosition(worldCopy);
        System.out.println(bestPos.toString());
        if (!engine.addGameItem(bestPos, this)) {
            System.out.println("Tried to place somewhere that was not possible ABORT ERROR ABORT!  " + bestPos);
            System.out.println("Try to fix it by finding the first possible place...");
            for (int i = 0; i < vector2Cache.length; i++) {
                if (engine.addGameItem(vector2Cache[i], this)) {
                    System.out.println("Fixed it by placing it at: " + vector2Cache[i].toString());
                    return;
                }
            }
        }
    }

    @Override
    public void setSmartness(int value) {
        System.out.println("Smartness" + value);
        this.depth = value;
    }

    /**
     * Get the result of the calculation. Is not checked if actually right.
     *
     * @param engine
     * @return
     */
    public Vector2 getMove(Engine engine) {
        initialize(engine);
        return getBestPosition(worldCopy);
    }

    private Vector2 getBestPosition(World world) {
        int bestValue = Integer.MIN_VALUE;
        Vector2 result = Vector2.ZERO;
        for (int i = 0; i < vector2Cache.length; i++) {
            World copy = new World(world.getSize(), world.getWinLength());
            world.writeTo(copy);
            Vector2 coords = vector2Cache[i];
            copy.addGameItem(coords, this);
            workers[coords.getX()][coords.getY()] = executor.submit(() ->
                    -getBestPosition(copy, coords, depth, -9999, 9999, getOther(this)));
        }
        try {
            for (int i = 0; i < vector2Cache.length; i++) {
                Vector2 coords = vector2Cache[i];
                int value = workers[coords.getX()][coords.getY()].get();
                if (value >= bestValue) {
                    result = vector2Cache[i];
                    bestValue = value;
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param world       The world we add and remove stuff from.
     * @param coordinates
     * @param depth       The amount of nodes we get into.
     * @return The amount of points.
     */
    protected int getBestPosition(World world, Vector2 coordinates,
                                  int depth, int alpha, int beta, Player currentPlayer) {
        if (world.hasWon(coordinates, currentPlayer)) {
            return 9999;
        } else if (world.hasWon(coordinates, getOther(currentPlayer))) {
            return -9999;
        }

        if (depth == 0) {
            return random.nextInt(4) - 2;
        }

        int localAlpha = alpha;
        int localBeta = beta;
        int best = -9999;
        for (int i = 0; i < vector2Cache.length; i++) {
            if (!world.addGameItem(vector2Cache[i], currentPlayer)) {
                continue;
            }

            int value = -getBestPosition(world, vector2Cache[i],
                    depth - 1, -localBeta, -localAlpha, getOther(currentPlayer));

            world.removeGameItem(vector2Cache[i]);

            best = Math.max(value, best);
            localAlpha = Math.max(localAlpha, value);
            if (localAlpha >= localBeta) {
                break;
            }
        }
        return best;
    }

    protected Player getOther(Player player) {
        if (player.equals(opponent)) {
            return this;
        } else {
            return opponent;
        }
    }
}
