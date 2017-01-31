package ss.project.server;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Synchronized;
import ss.project.shared.NetworkPlayer;
import ss.project.shared.Protocol;
import ss.project.shared.Serializable;
import ss.project.shared.exceptions.*;
import ss.project.shared.game.Engine;
import ss.project.shared.game.Vector3;
import ss.project.shared.model.ChatMessage;
import ss.project.shared.model.GameParameters;

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
public class Room implements Serializable {

    private static final Object playersLock = new Object();
    /**
     * Do not use this directly.
     *
     * @see Room#getNextId()
     */
    private static int nextId;
    /**
     * A reference to the engine responsible for the game.
     */
    @Getter
    private Engine engine;
    /**
     * The thread the engine is running on.
     */
    private Thread engineThread;
    /**
     * The max amount of players that can join this room.
     */
    private int maxPlayers;
    /**
     * The unique id of this room.
     */
    private int id;
    /**
     * Current joined players.
     */
    private List<NetworkPlayer> players;
    /**
     * The GameParameters of this room.
     */
    private GameParameters parameters;

    /**
     * Create a room and give it a specified ID. Only do this if you already know the ID.
     *
     * @param id         The id this room should get.
     * @param maxPlayers The amount of players that can join this room.
     * @param sizeX      The x-dimension of the world of this room.
     * @param sizeY      The y-dimension of the world of this room.
     * @param sizeZ      The z-dimension of the world of this room.
     * @param winLength  The length needed to win a game.
     */
    public Room(int id, int maxPlayers, int sizeX, int sizeY, int sizeZ, int winLength) {
        this.id = id;
        this.maxPlayers = maxPlayers;
        this.parameters = new GameParameters(sizeX, sizeY, sizeZ, winLength);
        players = new ArrayList<>();
    }

    /**
     * Create an empty room with the specified ID.
     * Create a room and automaticly assign an ID.
     *
     * @param maxPlayers The amount of players that can join this room.
     * @param sizeX      The x-dimension of the world of this room.
     * @param sizeY      The y-dimension of the world of this room.
     * @param sizeZ      The z-dimension of the world of this room.
     * @param winLength  The length needed to win a game.
     */
    public Room(int maxPlayers, int sizeX, int sizeY, int sizeZ, int winLength) {
        id = getNextId();
        this.maxPlayers = maxPlayers;
        this.parameters = new GameParameters(sizeX, sizeY, sizeZ, winLength);
        players = new ArrayList<>();
    }

    /**
     * Create a room and give it a specified ID. Only do this if you already know the ID.
     *
     * @param id         The id this room should get.
     * @param maxPlayers The amount of players that can join this room.
     * @param worldSize  The x-dimension of the world of this room.
     * @param winLength  The length needed to win a game.
     * @see Room#Room(int, int, int, int, int, int)
     */
    public Room(int id, int maxPlayers, Vector3 worldSize, int winLength) {
        this(id, maxPlayers, worldSize.getX(), worldSize.getY(), worldSize.getZ(), winLength);
    }

    /**
     * Create a room and give it a specified ID. Only do this if you already know the ID.
     *
     * @param maxPlayers The amount of players that can join this room.
     * @param worldSize  The x-dimension of the world of this room.
     * @param winLength  The length needed to win a game.
     */
    public Room(int maxPlayers, Vector3 worldSize, int winLength) {
        this(maxPlayers, worldSize.getX(), worldSize.getY(), worldSize.getZ(), winLength);
    }

    /**
     * Create a room from a string from the protocol.
     *
     * @param line
     * @return A new room.
     * @throws ProtocolException If the input is not in the correct format.
     */
    public static Room fromString(String line, String splitRegex) throws ProtocolException {
        if (splitRegex.equals(Protocol.PIPE_SYMBOL)) {
            // split room by it's parameters
            String[] roomParams = line.split(splitRegex);
            // check if the amount of parameters matches
            if (roomParams.length != Protocol.ROOM_PARAMETERS) {
                throw new ParameterLengthsMismatchException(Protocol.ROOM_PARAMETERS, roomParams.length);
            }
            try {
                // convert all strings to integers
                int[] integers = Arrays.stream(roomParams).mapToInt(Integer::parseInt).toArray();
                // create a room with the given parameters
                return new Room(integers[0], integers[1], integers[2], integers[3], integers[4], integers[5]);
            } catch (NumberFormatException e) {
                IllegalParameterException illegalParameterException = new IllegalParameterException("Could not convert arguments to numbers: " + Arrays.stream(roomParams).collect(Collectors.joining(", ")));
                illegalParameterException.setStackTrace(e.getStackTrace());
                throw illegalParameterException;
            }
        } else {
            String[] roomParams = line.split(splitRegex);
            if (roomParams.length != Protocol.ROOM_PARAMETERS) {
                throw new ParameterLengthsMismatchException(Protocol.ROOM_PARAMETERS, roomParams.length);
            }
            try {
                // create a room with the given parameters
                return new Room(Integer.parseInt(roomParams[1]), Integer.parseInt(roomParams[2]), Integer.parseInt(roomParams[3]), Integer.parseInt(roomParams[4]), Integer.parseInt(roomParams[5]));
            } catch (NumberFormatException e) {
                IllegalParameterException illegalParameterException = new IllegalParameterException("Could not convert arguments to numbers: " + Arrays.stream(roomParams).collect(Collectors.joining(", ")));
                illegalParameterException.setStackTrace(e.getStackTrace());
                throw illegalParameterException;
            }
        }
    }

    /**
     * Get the next unique id for rooms.
     *
     * @return A unique id.
     */
    @Synchronized
    private static int getNextId() {
        return nextId++;
    }

    /**
     * Create a list of rooms from a string in protocol format.
     *
     * @param line
     * @return A new list of rooms.
     * @throws ProtocolException The line is not in the correct format.
     */
    public static List<Room> parseRoomListString(String line) throws ProtocolException {
        Scanner sc = new Scanner(line);
        // test for invalid message
        if (!sc.next().equalsIgnoreCase(Protocol.Server.SENDLISTROOMS.getMessage())) {
            throw new UnexpectedMessageException(Protocol.Server.SENDLISTROOMS.getMessage(), line);
        }
        List<Room> rooms = new ArrayList<>();
        while (sc.hasNext()) {
            rooms.add(Room.fromString(sc.next(), Protocol.PIPE_SYMBOL));
        }
        return rooms;
    }

    /**
     * Join this room.
     * Will also send a message to all players that a player joined the room.
     *
     * @param player Player that wants to join this room.
     * @throws AlreadyJoinedException This player had already joined this room.
     * @throws RoomFullException      This room is full.
     */
    public void join(NetworkPlayer player) throws AlreadyJoinedException, RoomFullException {
        synchronized (playersLock) {
            if (players.contains(player)) {
                //Already joined.
                throw new AlreadyJoinedException(player);
            }
            if (players.size() >= maxPlayers) {
                throw new RoomFullException(this);
            }
            players.add(player);
        }
        player.setCurrentRoom(this);
        broadcast(Protocol.createMessage(Protocol.Server.NOTIFYMESSAGE, new ChatMessage("Server", player.getName() + " joined the room.")));
    }

    /**
     * Start the game in this room.
     * Creates an engine and starts it at its own thread.
     * Will set all players' state to in-game.
     * Will notify all players the game has started.
     */
    void startGame() {
        synchronized (playersLock) {
            Object[] args = new Object[players.size() + 1];
            args[0] = parameters;
            for (int i = 0; i < players.size(); i++) {
                args[i + 1] = players.get(i);
            }
            for (NetworkPlayer np : players) {
                np.setInGame(true);
                np.getClientHandler().sendMessage(Protocol.createMessage(Protocol.Server.STARTGAME, args));
            }
            engine = new Engine(parameters, players);
        }
        engine.setRoom(this);
        engineThread = new Thread(() -> engine.startGame());
        engineThread.setDaemon(true);
        engineThread.start();
    }

    /**
     * Remove a player from a room.
     *
     * @param player That wants to leave this room.
     * @throws NotInRoomException When player was not in this room at the time of leaving.
     */
    public void leave(NetworkPlayer player) throws NotInRoomException {
        if (!players.contains(player)) {
            throw new NotInRoomException(player);
        }
        players.remove(player);
        player.setCurrentRoom(null);
        broadcast(Protocol.createMessage(Protocol.Server.NOTIFYMESSAGE, new ChatMessage("Server", player.getName() + " left the room.")));
        player.getClientHandler().sendMessage(Protocol.createMessage(Protocol.Server.NOTIFYMESSAGE, new ChatMessage("Server", player.getName() + " left the room.")));
    }

    /**
     * Notify the room that this player has lost connection and stop the game.
     *
     * @param networkPlayer
     */
    public void lostConnection(NetworkPlayer networkPlayer) {
        if (networkPlayer.isInGame()) {
            try {
                leave(networkPlayer);
            } catch (NotInRoomException e) {
                e.printStackTrace();
            }
            //Stop the game.
            endGame(Protocol.WinReason.PLAYERDISCONNECTED, networkPlayer.getId());
        }
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
        return parameters.getWinLength();
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
     * @return A new vector3.
     */
    public Vector3 getWorldSize() {
        return parameters.getWorldSize();
    }

    public String serialize() {
        return String.join("|",
                String.valueOf(id),
                String.valueOf(maxPlayers),
                String.valueOf(parameters.getSizeX()),
                String.valueOf(parameters.getSizeY()),
                String.valueOf(parameters.getSizeZ()),
                String.valueOf(parameters.getWinLength()));
    }

    public String serializeCreation() {
        return String.join(Protocol.SPACE_SYMBOL,
                String.valueOf(maxPlayers),
                String.valueOf(parameters.getSizeX()),
                String.valueOf(parameters.getSizeY()),
                String.valueOf(parameters.getSizeZ()),
                String.valueOf(parameters.getWinLength()));
    }

    /**
     * Get the current amount of players in this room.
     *
     * @return
     */
    public int getCurrentPlayers() {
        return players.size();
    }

    /**
     * Send a message to all clients of this room.
     *
     * @param message A message with protocol format.
     * @see Protocol#createMessage(Protocol.Client, Object...)
     * @see Protocol#createMessage(Protocol.Server, Object...)
     */
    @Synchronized("playersLock")
    public void broadcast(String message) {
        for (NetworkPlayer p : players) {
            p.getClientHandler().sendMessage(message);
        }
    }

    /**
     * Checks if the amount of players in this room is equal to the max amount of players in this room.
     *
     * @return True if the room is full, False otherwise.
     */
    public boolean isFull() {
        return players.size() >= maxPlayers;
    }

    /**
     * End the game with the specified reason. Will reset all players to the lobby.
     *
     * @param reason
     * @param id
     */
    public void endGame(Protocol.WinReason reason, int id) {
        //We remove this room from the server.
        Server.getInstance().removeRoom(this);

        engine.finishGame(reason, id);
        engineThread.interrupt();
        synchronized (playersLock) {
            //Reset every player to the lobby.
            for (int i = 0; i < players.size(); i++) {
                players.get(i).setCurrentRoom(null);
                players.get(i).setInGame(false);
            }
            players.clear();
        }
    }
}
