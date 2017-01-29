package ss.project.shared.computerplayer;

import ss.project.shared.game.Engine;
import ss.project.shared.game.Player;

public abstract class ComputerPlayer extends Player {

    /**
     * create a computer player with the specified AI.
     */
    public ComputerPlayer() {
        super();
    }

    /**
     * create a computer player with the specified AI.
     */
    public ComputerPlayer(String name) {
        super(name);
    }

    public abstract void doTurn(Engine engine);

    public abstract void setSmartness(int value);
}
