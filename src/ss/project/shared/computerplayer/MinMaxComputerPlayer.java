package ss.project.shared.computerplayer;

import lombok.Getter;
import ss.project.shared.game.Engine;
import ss.project.shared.game.Player;
import ss.project.shared.game.Vector2;
import ss.project.shared.game.World;

/**
 * Created by fw on 16/01/2017.
 */
public class MinMaxComputerPlayer extends ComputerPlayer {
    private Player opponent;
    private World worldCopy;
    private int depth = 7;

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
        if (worldCopy == null) {
            worldCopy = new World(engine.getWorld().getSize());
        }
        if (opponent == null) {
            opponent = engine.getOtherPlayer(this);
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
        int bestValue = Integer.MIN_VALUE;
        Vector2 result = Vector2.ZERO;
        Worker[][] workers = new Worker[world.getSize().getX()][world.getSize().getY()];
        for (int x = 0; x < world.getSize().getX(); x++) {
            for (int y = 0; y < world.getSize().getY(); y++) {
                World copy = new World(world.getSize());
                world.writeTo(copy);
                workers[x][y] = new Worker(this, copy, new Vector2(x, y), depth, true);
                workers[x][y].start();
            }
        }
        for (int x = 0; x < workers.length; x++) {
            for (int y = 0; y < workers[x].length; y++) {
                try {
                    workers[x][y].join();
                    int value = workers[x][y].getResult();
                    if (value > bestValue) {
                        result = new Vector2(x, y);
                        bestValue = value;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
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
            return 10;
        } else if (!maximize && world.hasWon(coordinates, opponent)) {
            world.removeGameItem(coordinates);
            return -10;
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

    private class Worker extends Thread {
        private MinMaxComputerPlayer caller;
        @Getter
        private int result;
        private World world;
        private Vector2 coords;
        private int depth;
        private boolean maximize;

        public Worker(MinMaxComputerPlayer caller, World world, Vector2 coordinates, int depth, boolean maximize) {
            this.caller = caller;
            this.world = world;
            this.coords = coordinates;
            this.depth = depth;
            this.maximize = maximize;
        }

        @Override
        public void run() {
            result = caller.getBestPosition(world, coords, depth, maximize);
        }
    }
}
