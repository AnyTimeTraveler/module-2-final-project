package ss.project.shared.computerplayer;

import ss.project.shared.game.Engine;
import ss.project.shared.game.Player;

/**
 * Base class of all AI.
 */
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

    /**
     * Called every time this player needs to do a turn.
     *
     * @param engine The engine that should be used to set the move.
     */
    public abstract void doTurn(Engine engine);

    /**
     * Set the smartness/thinking time of this computerplayer.
     *
     * @param value
     */
    public abstract void setSmartness(int value);
}
