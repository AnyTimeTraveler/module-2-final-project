package ss.project.shared.game;

import ss.project.shared.computerplayer.MinMaxComputerPlayer;
import ss.project.shared.computerplayer.RandomComputerPlayer;


/**
 * Creates a game and has the main function.
 *
 * @author fw
 */
public class GameStarter {

    public static void main(String[] args) {
        //Create a controller for the MainWindow.
        //Create a new game with world size 4,4,4 and 2 players
        GameController gameController = new GameController();

        gameController.createSinglePlayer(new Vector3(4, 4, 4),
                new RandomComputerPlayer("computer random"),
                new MinMaxComputerPlayer("computer minmax")
        );
    }
}
