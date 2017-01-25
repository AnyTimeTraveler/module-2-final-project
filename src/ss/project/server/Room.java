package ss.project.server;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import ss.project.shared.Protocol;
import ss.project.shared.exceptions.*;
import ss.project.shared.game.Player;
import ss.project.shared.game.Vector3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * A room object with players.
 * <p>
 * Created by fw on 21/01/2017.
 */
@EqualsAndHashCode
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

    public Room(int id, int maxPlayers, int sizeX, int sizeY, int sizeZ, int winLength) {
        this.id = id;
        this.maxPlayers = maxPlayers;
        this.worldSize = new Vector3(sizeX, sizeY, sizeZ);
        this.winLength = winLength;
        players = new ArrayList<>();
    }

    /**
     * Create an empty room with the specified ID.
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

    public static List<Room> parseRoomString(String line) throws ProtocolException {
        Scanner sc = new Scanner(line);
        // test for invalid message
        if (!sc.next().equalsIgnoreCase(Protocol.Server.SENDLISTROOMS.getMessage())) {
            throw new UnexpectedMessageException(Protocol.Server.SENDLISTROOMS.getMessage(), line);
        }
        List<Room> rooms = new ArrayList<>();
        while (sc.hasNext()) {
            // split each room by it's parameters
            String[] roomParams = sc.next().split(Protocol.PIPE_SYMBOL);
            // check if the amount of parameters matches
            if (roomParams.length != Protocol.ROOM_PARAMETERS) {
                throw new ParameterLengthsMismatchException(Protocol.ROOM_PARAMETERS, roomParams.length);
            }
            try {
                // convert all strings to integers
                int[] integers = Arrays.stream(roomParams).mapToInt(Integer::parseInt).toArray();
                // create a room with the given parameters
                rooms.add(new Room(integers[0], integers[1], integers[2], integers[3], integers[4], integers[5]));
            } catch (NumberFormatException e) {
                IllegalParameterException illegalParameterException = new IllegalParameterException("Could not convert arguments to numbers: " + Arrays.stream(roomParams).collect(Collectors.joining(", ")));
                illegalParameterException.setStackTrace(e.getStackTrace());
                throw illegalParameterException;
            }
        }
        return rooms;
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

    public String serialize() {
        return String.join("|",
                String.valueOf(id),
                String.valueOf(maxPlayers),
                String.valueOf(worldSize.getX()),
                String.valueOf(worldSize.getY()),
                String.valueOf(worldSize.getZ()),
                String.valueOf(winLength));
    }

    public int getCurrentPlayers() {
        return players.size();
    }
}
