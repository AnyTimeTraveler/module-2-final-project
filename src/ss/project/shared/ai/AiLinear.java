package ss.project.shared.ai;

import ss.project.shared.game.Engine;
import ss.project.shared.game.Player;
import ss.project.shared.game.Vector2;
import ss.project.shared.game.World;

public class AiLinear implements AI {

    private Player player;

    @Override
    public void initialize(Player player) {
        System.out.println("Initialize");
        this.player = player;
    }

    @Override
    public void doTurn(Engine engine) {
        //System.out.println("Do a turn");
        setNewGameItem(engine);
    }

    private void setNewGameItem(Engine engine) {
        World world = engine.getWorld();
        for (int x = 0; x < world.getSize().getX(); x++) {
            for (int y = 0; y < world.getSize().getY(); y++) {
                //if (world.addGameItem(new Vector2(x, y), player)) {
                if (engine.addGameItem(new Vector2(x, y), player)) {
                    return;
                }
            }
        }

    }

    @Override
    public void end() {
        System.out.println("End");
    }

}
