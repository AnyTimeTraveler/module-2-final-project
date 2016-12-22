package ss.project.shared.game;

import ss.project.shared.ai.*;

public class Player {

	private Engine					engine;
	private String					name;
	private int id;
	private boolean					hasAI;
	private IArtificialIntelligence	ai;

	/**
	 * Create a new player object with specified name.
	 * 
	 * @param name
	 *            name of the new player.
	 */
	public Player(int id, Engine engine, String name, boolean isAi) {
		this.id = id;
		this.engine = engine;
		this.name = name;
		this.hasAI = isAi;

		if (this.hasAI) {
            //Assign an ai.
            this.ai = new AiRandom();
			this.ai.initialize(this);
		}
	}
	
	/**
	 * Create a new player with the specified ai.
	 * @param id
	 * @param engine
	 * @param name
	 * @param isAi
	 * @param ai
	 */
	public Player(int id, Engine engine, String name, boolean isAi, IArtificialIntelligence ai) {
		this.id = id;
		this.engine = engine;
		this.name = name;
		this.hasAI = isAi;

		if (this.hasAI) {
            //Assign an ai.
            this.ai = ai;
			this.ai.initialize(this);
		}
	}

	/**
	 * 
	 * @return True if this player is controlled by the computer.
	 */
	public boolean isHasAI() {
		return hasAI;
	}

	/**
	 * Called everytime a new turn should be done. TODO: add the possibility for
     * the user to do a turn, now it's just ai.
     */
	public void doTurn() {
		if (this.hasAI) {
            //The user is an ai, make the ai do a turn.
            this.ai.doTurn(engine.getWorld());
		} else {
			//The user can select a place to build.
			return;
		}
	}

	/**
	 * get the name of this player. Set when initializing.
	 *
	 * @return a string of the name.
	 */
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return getName();
	}

	public int getID() {
		return id;
	}
}
