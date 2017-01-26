package ss.project.shared;

import lombok.Getter;
import ss.project.server.LeaderboardEntry;
import ss.project.server.Room;

import java.util.HashMap;

/**
 * The Class Protocol implements the protocol and provides functions to help implementing
 * the protocol. This protocol is for Group 7, Mod02 BIT/INF 2016/2017, UTWENTE
 */
public class Protocol {

    /**
     *
     */
    public static final HashMap<String, String> ERRORMAP;
    public static final HashMap<String, String> WINMAP;
    public static final int ROOM_PARAMETERS = 6;
    public static final String PIPE_SYMBOL = "\\|";

    static {
        ERRORMAP = new HashMap<>();
        ERRORMAP.put("1", "Client has not yet sent capabilities message, Server cannot proceed");
        ERRORMAP.put("2", "Room sent in message joinRoom does not exist");
        ERRORMAP.put("3", "The chosen room is no longer available, either it already filled up or was empty for too long");
        ERRORMAP.put("4", "The input given by the client isn't valid at this moment");
        ERRORMAP.put("5", "The given move is not possible on this board");
        ERRORMAP.put("6", "Client is not allowed to leave the room after the game has started");
        ERRORMAP.put("7", "A message with piping in a wrong place was received");
        WINMAP = new HashMap<>();
        WINMAP.put("1", "The game was won!");
        WINMAP.put("2", "Draw! The board is full.");
        WINMAP.put("3", "Player disconnected. The game cannot continue.");
        WINMAP.put("4", "Player didn't respond. The game cannot continue.");
    }

    /**
     * Create a message that follows the protocol naming.
     *
     * @param message Type of message.
     * @param args    Any arguments of this message.
     * @return A string message.
     */
    public static String createMessage(Client message, Object... args) {
        return createMessage(message.getMessage(), args);
    }

    /**
     * @param message
     * @param args
     * @return
     */
    public static String createMessage(Server message, Object... args) {
        return createMessage(message.getMessage(), args);
    }

    private static String createMessage(String message, Object... args) {
        StringBuilder sb = new StringBuilder();
        sb.append(message);
        for (Object arg : args) {
            sb.append(' ');
            if (arg instanceof Boolean) {
                sb.append((boolean) arg ? '1' : '0');
            } else if (arg instanceof Room) {
                sb.append(((Room) arg).serialize());
            } else if (arg instanceof LeaderboardEntry) {
                sb.append(((LeaderboardEntry) arg).serialize());
            } else {
                sb.append(arg);
            }
        }
        return sb.toString();
    }

    /**
     * Client to server messages.
     *
     * @author Simon Struck
     */
    public enum Client {
        CREATEROOM("createRoom"),
        JOINROOM("joinRoom"),
        GETROOMLIST("getRoomList"),
        LEAVEROOM("leaveRoom"),
        MAKEMOVE("makeMove"),
        SENDMESSAGE("sendMessage"),
        REQUESTLEADERBOARD("requestLeaderboard"),
        SENDCAPABILITIES("sendCapabilities");

        @Getter
        private final String message;

        Client(String message) {
            this.message = message;
        }

        public boolean equals(String other) {
            return other.equalsIgnoreCase(message);
        }
    }

    /**
     * Server to client messages.
     *
     * @author Simon Struck
     */
    public enum Server {
        SERVERCAPABILITIES("serverCapabilities"),
        SENDLISTROOMS("sendListRooms"),
        ROOMCREATED("roomCreated"),
        ASSIGNID("assignID"),
        STARTGAME("startGame"),
        TURNOFPLAYER("playerTurn"),
        NOTIFYMOVE("notifyMove"),
        NOTIFYEND("notifyEnd"),
        ERROR("error"),
        NOTIFYMESSAGE("notifyMessage"),
        SENDLEADERBOARD("sendLeaderBoard");

        @Getter
        private final String message;

        Server(String message) {
            this.message = message;
        }
    }

}
