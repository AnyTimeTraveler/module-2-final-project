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
 * Created by fw on 16/01/2017.
 */
public class MinMaxAlphaBetaComputerPlayer extends ComputerPlayer {
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
        if (worldCopy == null)
            worldCopy = new World(engine.getWorld().getSize(), engine.getWorld().getWinLength());
        if (opponent == null)
            opponent = engine.getOtherPlayer(this);
        if (workers == null)
            workers = new Future[engine.getWorld().getSize().getX()][engine.getWorld().getSize().getY()];
        if (executor == null) {
            executor = Executors.newWorkStealingPool();
        }
        engine.getWorld().writeTo(worldCopy);
    }

    @Override
    public void doTurn(Engine engine) {
        initialize(engine);

        Vector2 bestPos = getBestPosition(worldCopy);
        System.out.println(bestPos.toString());
        if (!engine.addGameItem(bestPos, this)) {
            System.out.println("Tried to place somewhere that was not possible ABORT ERROR ABORT!  " + bestPos);
            System.out.println("Try to fix it by finding the first possible place...");
            for (int x = 0; x < worldCopy.getSize().getX(); x++) {
                for (int y = 0; y < worldCopy.getSize().getY(); y++) {
                    if (engine.addGameItem(new Vector2(x, y), this)) {
                        System.out.println("Fixed it by placing it at: " + new Vector2(x, y).toString());
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void setSmartness(int value) {
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
        for (int x = 0; x < world.getSize().getX(); x++) {
            for (int y = 0; y < world.getSize().getY(); y++) {
                World copy = new World(world.getSize(), world.getWinLength());
                world.writeTo(copy);
                int finalX = x;
                int finalY = y;
                copy.addGameItem(new Vector2(x, y), this);
                workers[x][y] = executor.submit(() -> -getBestPosition(copy, new Vector2(finalX, finalY), depth, -9999, 9999, getOther(this)));
            }
        }
        try {
            for (int x = 0; x < workers.length; x++) {
                for (int y = 0; y < workers[x].length; y++) {
                    int value = workers[x][y].get();
                    if (value >= bestValue) {
                        result = new Vector2(x, y);
                        bestValue = value;
                    }
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
    private int getBestPosition(World world, Vector2 coordinates, int depth, int alpha, int beta, Player currentPlayer) {
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
        for (int x = 0; x < world.getSize().getX(); x++) {
            for (int y = 0; y < world.getSize().getY(); y++) {
                Vector2 coord = new Vector2(x, y);
                if (!world.addGameItem(coord, currentPlayer)) {
                    continue;
                }

                int value = -getBestPosition(world, coord, depth - 1, -localBeta, -localAlpha, getOther(currentPlayer));

                world.removeGameItem(coord);

                best = Math.max(value, best);
                localAlpha = Math.max(localAlpha, value);
                if (localAlpha >= localBeta) {
                    break;
                }
            }
        }
        return best;
    }

    private Player getOther(Player player) {
        if (player.equals(opponent)) {
            return this;
        } else {
            return opponent;
        }
    }
}
