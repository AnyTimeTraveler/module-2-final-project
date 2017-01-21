package ss.project.shared.computerplayer;

import ss.project.shared.game.Engine;
import ss.project.shared.game.Player;
import ss.project.shared.game.Vector2;
import ss.project.shared.game.World;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by fw on 16/01/2017.
 */
public class MinMaxComputerPlayer extends ComputerPlayer {
    private Player opponent;
    private World worldCopy;
    private int depth = 6;
    private Future<Integer>[][] workers;
    private ExecutorService executor;

    /**
     * create a computer player with the specified AI.
     */
    public MinMaxComputerPlayer() {
        super();
        System.out.println("Initialized");
    }

    public MinMaxComputerPlayer(String name) {
        super(name);
        System.out.println("Initialized");
    }

    @Override
    public void doTurn(Engine engine) {
        if (worldCopy == null)
            worldCopy = new World(engine.getWorld().getSize());
        if (opponent == null)
            opponent = engine.getOtherPlayer(this);
        if (workers == null)
            workers = new Future[engine.getWorld().getSize().getX()][engine.getWorld().getSize().getY()];
        if (executor == null) {
            executor = Executors.newWorkStealingPool();
        }

        engine.getWorld().writeTo(worldCopy);
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

    private Vector2 getBestPosition(World world) {
        System.out.println("Starting heavy part!");
        int bestValue = Integer.MIN_VALUE;
        Vector2 result = Vector2.ZERO;
        for (int x = 0; x < world.getSize().getX(); x++) {
            for (int y = 0; y < world.getSize().getY(); y++) {
                World copy = new World(world.getSize());
                world.writeTo(copy);
                int finalX = x;
                int finalY = y;
                workers[x][y] = executor.submit(() -> getBestPosition(copy, new Vector2(finalX, finalY), depth, true));
            }
        }
        try {
            System.out.println("Waiting for threads to finish");
            for (int x = 0; x < workers.length; x++) {
                for (int y = 0; y < workers[x].length; y++) {
                    int value = workers[x][y].get();

                    if (value > bestValue) {
                        result = new Vector2(x, y);
                        bestValue = value;
                    }
                }
            }
            System.out.println("Threads finished");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("Stopping heavy part!");
        return result;
    }

    /**
     * @param world       The world we add and remove stuff from.
     * @param coordinates
     * @param depth       The amount of nodes we get into.
     * @param maximize    If true we try to maximize the points the player would get.
     *                    If set to false we try to get the least amount of points.
     * @return The amount of points.
     */
    private int getBestPosition(World world, Vector2 coordinates, int depth, boolean maximize) {
        if (depth == 0) {
            return 0;
        }

        if ((maximize && !world.addGameItem(coordinates, this)) || (!maximize && !world.addGameItem(coordinates, opponent))) {
            if (maximize) {
                return 0;
            } else {
                return 0;
            }
        }

        if (maximize && world.hasWon(coordinates, this)) {
            world.removeGameItem(coordinates);
            return (int) Math.pow(10, depth);
        } else if (!maximize && world.hasWon(coordinates, opponent)) {
            world.removeGameItem(coordinates);
            return (int) Math.pow(-10, depth);
        }

        int sum = 0;
        for (int x = 0; x < world.getSize().getX(); x++) {
            for (int y = 0; y < world.getSize().getY(); y++) {
                int value = getBestPosition(world, new Vector2(x, y), depth - 1, !maximize);
                sum += value;
            }
        }
        world.removeGameItem(coordinates);
        return sum;
    }
}
