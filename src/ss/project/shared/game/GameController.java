package ss.project.shared.game;

public class GameController {

    private Engine singlePlayerEngine;

    public GameController() {
        // TODO Auto-generated constructor stub
    }

    public void createSinglePlayer(Vector3 size, Player... players) {
        singlePlayerEngine = new Engine(size, players);
        singlePlayerEngine.startGame();
    }

    public void createMultiplayer() {
        //Create multiplayer engine.
    }
}
