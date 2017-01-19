package ss.project.shared.computerplayer;

import ss.project.shared.game.Engine;
import ss.project.shared.game.Vector2;
import ss.project.shared.game.World;

import java.util.concurrent.ThreadLocalRandom;

public class RandomComputerPlayer extends ComputerPlayer {

    /**
     * create a computer player with the specified AI.
     *
     * @param name
     */
    public RandomComputerPlayer(String name) {
        super(name);
        System.out.println("Initialize");
    }

    @Override
    public void doTurn(Engine engine) {
        setNewGameItem(engine);
    }

    @Override
    public String getName() {
        return null;
    }

    private void setNewGameItem(Engine engine) {
        World world = engine.getWorld();
        while (true) {
            int x = ThreadLocalRandom.current().nextInt(0, world.getSize().getX());
            int y = ThreadLocalRandom.current().nextInt(0, world.getSize().getY());

            if (engine.addGameItem(new Vector2(x, y), this)) {
                return;
            }
        }
    }
}
