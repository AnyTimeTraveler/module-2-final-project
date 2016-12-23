package ss.project.shared.game;

import ss.project.shared.ai.IArtificialIntelligence;

public class ComputerPlayer extends Player {

    private IArtificialIntelligence ai;

    /**
     * create a computer player with the specified AI.
     *
     * @param name
     * @param ai
     */
    public ComputerPlayer(String name, IArtificialIntelligence ai) {
        super(name);
        this.ai = ai;
    }

    @Override
    public void doTurn(World world) {
        ai.doTurn(world);
    }

}
