package ss.project.shared.exceptions;

/**
 * An abstract class used when something wrong happens in relation to a room.
 *
 * Created by simon on 24.01.17.
 */
public abstract class RoomException extends Exception {
    public RoomException(String message) {
        super(message);
    }
}
