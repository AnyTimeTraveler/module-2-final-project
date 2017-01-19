package ss.project.shared.computerplayer;

import lombok.Getter;
import ss.project.shared.game.*;

/**
 * Created by fw on 16/01/2017.
 */
public class MinMaxComputerPlayer extends ComputerPlayer {
    private Player opponent;
    private World worldCopy;
    private int depth = 6;

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
        if (!engine.addGameItem(bestPos, this)) {
            System.out.println(bestPos.toString());
            if (!engine.addGameItem(bestPos, this)) {
                System.out.println("Tried to place somewhere that was not possible ABORT ERROR ABORT!  " + bestPos);
            }
        }
    }

    private Vector2 getBestPosition(World world) {
        int bestValue = Integer.MIN_VALUE;
        Vector2 result = Vector2.ZERO;
        for (int x = 0; x < world.getSize().getX(); x++) {
            for (int y = 0; y < world.getSize().getY(); y++) {
                int value = getBestPosition(world, new Vector2(x, y), depth, true);
                if (value > bestValue) {
                    result = new Vector2(x, y);
                    bestValue = value;
                }
            }
        }
        //if (result.equals(Vector2.ZERO)) {
        //    System.out.println("error?");
        //}
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
        //int bestValue = Integer.MIN_VALUE;
        //if (!maximize) {
        //    bestValue = Integer.MAX_VALUE;
        //}
        int sum = 0;
        for (int x = 0; x < world.getSize().getX(); x++) {
            for (int y = 0; y < world.getSize().getY(); y++) {
                int value = getBestPosition(world, new Vector2(x, y), depth - 1, !maximize);
//                if (maximize) {
//                    if (value > bestValue) {
//                        bestValue = value;
//                    }
//                } else {
//                    if (value < bestValue) {
//                        bestValue = value;
//                    }
//                }
                sum += value;
            }
        }
        world.removeGameItem(coordinates);
        return sum;
    }

    private class Worker extends Thread {
        private MinMaxComputerPlayer caller;
        @Getter
        private Vector2 result;

        public Worker(MinMaxComputerPlayer caller) {
            this.caller = caller;
        }

        @Override
        public void run() {
            result = caller.getBestPosition(new World(new Vector3(1, 2, 3)));
        }
    }
}
