package ss.project.shared.exceptions;

import ss.project.shared.game.Player;

/**
 * Thrown when someone tries to join a room, but it’s already in that room.
 *
 * Created by fw on 21/01/2017.
 */
public class AlreadyJoinedException extends Exception {
    public AlreadyJoinedException(Player player) {
        super("Player had already joined the room: " + player.toString());
    }
}
