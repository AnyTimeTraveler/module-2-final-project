package ss.project.shared.exceptions;

import ss.project.server.Room;

/**
 * Created by fw on 21/01/2017.
 */
public class RoomFullException extends RoomException {
    public RoomFullException(Room room) {
        super("Tried to join full room: " + room.toString());
    }
}
