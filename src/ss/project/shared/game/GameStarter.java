package ss.project.shared.game;

import ss.project.shared.ai.AiRandom;

/**
 * Creates a game and has the main function.
 * 
 * @author fw
 *
 */
public class GameStarter {

	public static void main(String[] args) {
		//Create a controller for the UI.

		//Create a new game with world size 4,4,4 and 2 players
        GameController gameController = new GameController();

        gameController.createSinglePlayer(new Vector3(4, 4, 4),
                new ComputerPlayer("computer 0", new AiRandom()),
                new ComputerPlayer("computer 1", new AiRandom()));
    }
}
