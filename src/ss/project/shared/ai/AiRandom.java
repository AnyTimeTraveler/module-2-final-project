package ss.project.shared.ai;

import ss.project.shared.game.Engine;
import ss.project.shared.game.Player;
import ss.project.shared.game.Vector2;
import ss.project.shared.game.World;

import java.util.concurrent.ThreadLocalRandom;

public class AiRandom implements AI {

    private Player player;

    @Override
    public void initialize(Player player) {
        System.out.println("Initialize");
        this.player = player;
    }

    @Override
    public void doTurn(Engine engine) {
        setNewGameItem(engine);
    }

    private void setNewGameItem(Engine engine) {
        World world = engine.getWorld();
        while (true) {
            int x = ThreadLocalRandom.current().nextInt(0, world.getSize().getX());
            int y = ThreadLocalRandom.current().nextInt(0, world.getSize().getY());

            if (engine.addGameItem(new Vector2(x, y), player)) {
                return;
            }
        }
    }

    @Override
    public void end() {
        System.out.println("End");
    }

}
