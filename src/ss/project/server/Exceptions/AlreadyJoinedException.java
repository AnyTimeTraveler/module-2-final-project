package ss.project.server.Exceptions;

import ss.project.shared.game.Player;

/**
 * Created by fw on 21/01/2017.
 */
public class AlreadyJoinedException extends Exception {
    public AlreadyJoinedException(Player player) {
        super("Player had already joined the room: " + player.toString());
    }
}
