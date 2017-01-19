package ss.project.shared.computerplayer;

import ss.project.shared.game.Engine;
import ss.project.shared.game.Vector2;
import ss.project.shared.game.World;

public class LinearComputerPlayer extends ComputerPlayer {

    /**
     * create a computer player with the specified AI.
     */
    public LinearComputerPlayer() {
        super();
        System.out.println("Initialize");
    }

    /**
     * create a computer player with the specified AI.
     */
    public LinearComputerPlayer(String name) {
        super(name);
        System.out.println("Initialize");
    }

    @Override
    public void doTurn(Engine engine) {
        //System.out.println("Do a turn");
        setNewGameItem(engine);
    }

    @Override
    public String getName() {
        return "";
    }

    private void setNewGameItem(Engine engine) {
        World world = engine.getWorld();
        for (int x = 0; x < world.getSize().getX(); x++) {
            for (int y = 0; y < world.getSize().getY(); y++) {
                if (engine.addGameItem(new Vector2(x, y), this)) {
                    return;
                }
            }
        }

    }
}
