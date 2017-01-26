package ss.project.client.ui;

import ss.project.client.HumanPlayer;
import ss.project.shared.game.Player;

/**
 * Created by fw on 20/01/2017.
 */
public interface GameDisplay {
    void startTurn(Object waiter, HumanPlayer humanPlayer);

    void update();

    void showHint(int x, int y, int z);

    void removeHint(int x, int y, int z);

    public void setCurrentPlayer(Player player);
}
