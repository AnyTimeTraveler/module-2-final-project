package ss.project.shared.game;

import ss.project.shared.ai.AI;

public class ComputerPlayer extends Player {

	private AI ai;

	/**
	 * create a computer player with the specified AI.
	 * @param name 
	 * @param ai
	 */
	public ComputerPlayer(String name, AI ai) {
		super(name);
		this.ai = ai;
	}

	@Override
	public void doTurn(World world) {
		ai.doTurn(world);
	}

}
