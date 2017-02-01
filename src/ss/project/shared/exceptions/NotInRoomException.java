package ss.project.shared.exceptions;

import ss.project.shared.game.Player;

/**
 * Thrown when a player wants to leave a room, but it’s not in that room.
 *
 * Created by fw on 21/01/2017.
 */
public class NotInRoomException extends Exception {
    public NotInRoomException(Player player) {
        super("Player " + player.toString() + " was not in this room.");
    }
}
