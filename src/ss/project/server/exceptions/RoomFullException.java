package ss.project.server.exceptions;

import ss.project.server.Room;

/**
 * Created by fw on 21/01/2017.
 */
public class RoomFullException extends Exception {
    public RoomFullException(Room room) {
        super("Tried to join full room: " + room.toString());
    }
}
