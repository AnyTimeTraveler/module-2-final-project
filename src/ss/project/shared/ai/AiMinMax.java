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

    @Override
    public void initialize(Player player) {
        this.player = player;
        System.out.println("Initialized");
    }

    @Override
    public void doTurn(Engine engine) {
        World world = engine.getWorld();
        if (!engine.addGameItem(getBestPosition(world), player)) {
            System.out.println("Tried to place somewhere that was not possible!");
            return;
        }
    }

    private Vector2 getBestPosition(World world) {
        World copy = world.deepCopy();
        return Vector2.ZERO;
    }

    @Override
    public void end() {

    }
}
