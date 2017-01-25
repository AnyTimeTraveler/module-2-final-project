package ss.project.server;

import ss.project.server.exceptions.AlreadyJoinedException;
import ss.project.server.exceptions.InvalidInputException;
import ss.project.server.exceptions.NotInRoomException;
import ss.project.server.exceptions.RoomFullException;
import ss.project.shared.Protocol;
import ss.project.shared.game.Player;
import ss.project.shared.game.Vector3;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * A room object with players.
 * <p>
 * Created by fw on 21/01/2017.
 */
public class Room {
    /**
     * DO NOT USE THIS DIRECTLY!
     */
    private static int nextId;
    private int maxPlayers;
    private int id;
    /**
     * Current joined players.
     */
    private List<Player> players;
    /**
     * Length needed to win a game.
     */
    private int winLength;
    private Vector3 worldSize;

    /**
     * Create a room and automaticly assign an ID.
     *
     * @param maxPlayers The amount of players that can join this room.
     * @param worldSize  The dimensions of the world of this room.
     * @param winLength  The length needed to win a game.
     */
    public Room(int maxPlayers, Vector3 worldSize, int winLength) {
        id = getNextId();
        this.maxPlayers = maxPlayers;
        this.worldSize = worldSize;
        this.winLength = winLength;
        players = new ArrayList<>();
    }

    /**
     * Create a room and give it a specified ID. Only do this if you already know the ID.
     *
     * @param id         The id this room should get.
     * @param maxPlayers The amount of players that can join this room.
     * @param worldSize  The dimensions of the world of this room.
     * @param winLength  The length needed to win a game.
     */
    public Room(int id, int maxPlayers, Vector3 worldSize, int winLength) {
        this.id = id;
        this.maxPlayers = maxPlayers;
        this.worldSize = worldSize;
        this.winLength = winLength;
        players = new ArrayList<>();
    }

    private static synchronized int getNextId() {
        return nextId++;
    }

    /**
     * Convert a line from the protocol to a list of rooms.
     *
     * @param line See the protocol of sendListRooms.
     * @return A list of new created Rooms.
     * @throws NumberFormatException If a number is expected, but a text is found.
     * @throws InvalidInputException If not enough arguments are given for the rooms.
     */
    public static List<Room> parseRoomString(String line) throws NumberFormatException, InvalidInputException {
        Scanner sc = new Scanner(line);
        if (line.split(" ")[0].equals(Protocol.Server.SENDLISTROOMS.getMessage())) {
            sc.next();
        }

        List<Room> result = new ArrayList<>();

        while (sc.hasNext()) {
            String[] arguments = sc.next().split("|");
            if (arguments.length < 6) {
                throw new InvalidInputException(line);
            }
            int roomID = Integer.parseInt(arguments[0]);
            int maxPlayerAmount = Integer.parseInt(arguments[1]);
            int maxDimensionX = Integer.parseInt(arguments[2]);
            int maxDimensionY = Integer.parseInt(arguments[3]);
            int maxDimensionZ = Integer.parseInt(arguments[4]);
            int maxWinLength = Integer.parseInt(arguments[5]);
            result.add(new Room(roomID, maxPlayerAmount, new Vector3(maxDimensionX, maxDimensionY, maxDimensionZ), maxWinLength));
        }

        return result;
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
