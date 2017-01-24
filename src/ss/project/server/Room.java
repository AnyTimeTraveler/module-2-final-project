package ss.project.server;

import lombok.Getter;
import ss.project.server.exceptions.AlreadyJoinedException;
import ss.project.server.exceptions.NotInRoomException;
import ss.project.server.exceptions.RoomFullException;
import ss.project.shared.game.Player;
import ss.project.shared.game.Vector3;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

/**
 * A room object with players.
 * <p>
 * Created by fw on 21/01/2017.
 */
public class Room {
    // DO NOT USE THIS DIRECTLY!
    private static int nextId;
    @Getter
    private int maxPlayers;
    private int id;
    private List<Player> players;
    private int winLength;
    private Vector3 worldSize;

    /**
     * Create an empty room with the specified ID.
     *
     * @param id
     */
    public Room(int maxPlayers, Vector3 worldSize) {
        id = getNextId();
        this.maxPlayers = maxPlayers;
        this.worldSize = worldSize;
        players = new ArrayList<>();
    }

    private static synchronized int getNextId() {
        return nextId++;
    }

    public static List<Room> parseRoomString(String line) {
        //TODO: Implement.
        throw new NotImplementedException();
    }

    /**
     * Join this room.
     *
     * @param player Player that wants to join this room.
     * @throws AlreadyJoinedException This player had already joined this room.
     * @throws RoomFullException      This room is full.
     */
    public void join(Player player) throws AlreadyJoinedException, RoomFullException {
        if (players.contains(player)) {
            //Already joined.
            throw new AlreadyJoinedException(player);
        }
        if (players.size() >= maxPlayers) {
            throw new RoomFullException(this);
        }
        players.add(player);
    }

    /**
     * Remove a player from a room.
     *
     * @param player That wants to leave this room.
     * @throws NotInRoomException When player was not in this room at the time of leaving.
     */
    public void leave(Player player) throws NotInRoomException {
        if (!players.contains(player)) {
            throw new NotInRoomException(player);
        }
        players.remove(player);
    }

    /**
     * Get the id of this room.
     *
     * @return an integer representing the id of this room.
     */
    public int getId() {
        return id;
    }

    /**
     * Get the winlength needed for this room.
     *
     * @return
     */
    public int getWinLength() {
        return winLength;
    }

    /**
     * Get the max amount of players that are possible in this room.
     *
     * @return
     */
    public int getMaxPlayers() {
        return maxPlayers;
    }

    /**
     * Get the size of this world.
     *
     * @return
     */
    public Vector3 getWorldSize() {
        return worldSize;
    }
}
