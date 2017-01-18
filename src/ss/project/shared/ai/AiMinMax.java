package ss.project.shared.ai;

import ss.project.shared.game.Engine;
import ss.project.shared.game.Player;
import ss.project.shared.game.Vector2;
import ss.project.shared.game.World;

/**
 * Created by fw on 16/01/2017.
 */
public class AiMinMax implements AI {
    private Player player;
    private Player opponent;
    private World worldCopy;

    @Override
    public void initialize(Player player) {
        this.player = player;
        opponent = new Player("Dummy") {
            @Override
            public void doTurn(Engine engine) {
                //Don't do anything.
            }
        };
        System.out.println("Initialized");
    }

    @Override
    public void doTurn(Engine engine) {
        if (worldCopy == null) {
            worldCopy = new World(engine.getWorld().getSize());
        }

        engine.getWorld().writeTo(worldCopy);
        Vector2 bestPos = getBestPosition(worldCopy);
        if (!engine.addGameItem(bestPos, player)) {
            System.out.println("Tried to place somewhere that was not possible ABORT ERROR ABORT!  " + bestPos);
            return;
        }
    }

    private Vector2 getBestPosition(World world) {
        int highestValue = Integer.MIN_VALUE;
        Vector2 result = Vector2.ZERO;
        for (int x = 0; x < world.getSize().getX(); x++) {
            for (int y = 0; y < world.getSize().getY(); y++) {
                int value = getBestPosition(world, new Vector2(x, y), 3, true);
                if (value > highestValue) {
                    result = new Vector2(x, y);
                    highestValue = value;
                }
            }
        }
        if (result.equals(Vector2.ZERO)) {
            System.out.println("error?");
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

        if ((maximize && !world.addGameItem(coordinates, player)) || (!maximize && !world.addGameItem(coordinates, opponent))) {
            world.removeGameItem(coordinates);
            return 0;
        }

        if (world.hasWon(coordinates, player)) {
            world.removeGameItem(coordinates);
            if (maximize) {
                return Integer.MAX_VALUE;
            } else {
                return Integer.MIN_VALUE;
            }
        }
        int highestValue = Integer.MIN_VALUE;
        if (!maximize) {
            highestValue = Integer.MAX_VALUE;
        }
        for (int x = 0; x < world.getSize().getX(); x++) {
            for (int y = 0; y < world.getSize().getY(); y++) {
                int value = getBestPosition(world, new Vector2(x, y), depth - 1, !maximize);
                if (maximize) {
                    if (value > highestValue) {
                        highestValue = value;
                    }
                } else if (value < highestValue) {
                    highestValue = value;
                }
            }
        }
        world.removeGameItem(coordinates);
        return highestValue;
    }

    @Override
    public void end() {
        worldCopy = null;
    }
}
