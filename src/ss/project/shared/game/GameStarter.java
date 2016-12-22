package ss.project.shared.game;


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
		new Engine(new Vector3(4, 4, 4), 2, 2);
	}
}
