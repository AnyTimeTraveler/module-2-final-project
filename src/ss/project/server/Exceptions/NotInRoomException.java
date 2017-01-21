package ss.project.server.Exceptions;

import ss.project.shared.game.Player;

/**
 * Created by fw on 21/01/2017.
 */
public class NotInRoomException extends Exception {
    public NotInRoomException(Player player) {
        super("Player " + player.toString() + " was not in this room.");
    }
}
