package ss.project.shared;

import lombok.Getter;

import java.util.HashMap;

/**
 * The Class Protocol implements the protocol and provides functions to help implementing
 * the protocol. This protocol is for Group 7, Mod02 BIT/INF 2016/2017, UTWENTE
 */
public class Protocol {

    public static final HashMap<String, String> ERRORMAP;
    public static final HashMap<String, String> WINMAP;

    static {
        ERRORMAP = new HashMap<>();
        ERRORMAP.put("1", "Client has not yet sent capabilities message, Server cannot proceed");
        ERRORMAP.put("2", "Room sent in message joinRoom does not exist");
        ERRORMAP.put("3", "The chosen room is no longer available, either it already filled up or was empty for too long");
        ERRORMAP.put("4", "The input given by the client isn�t valid at this moment");
        ERRORMAP.put("5", "The given move is not possible on this board");
        ERRORMAP.put("6", "Client is not allowed to leave the room after the game has started");
        ERRORMAP.put("7", "A message with piping in a wrong place was received");
    }

    static {
        WINMAP = new HashMap<>();
        WINMAP.put("1", "The game was won!");
        WINMAP.put("2", "Draw! The board is full.");
        WINMAP.put("3", "Player disconnected. The game cannot continue.");
        WINMAP.put("4", "Player didn't respond. The game cannot continue.");
    }

    public static String createMessage(Client message, Object... args) {
        return message.getMessage();
    }

    public static String createMessage(Server message, Object... args) {
        return createMessage(message.getMessage());
    }

    private static String createMessage(String message, Object... args) {
        StringBuilder sb = new StringBuilder();
        sb.append(message);
        for (Object arg : args) {
            sb.append(' ');
            sb.append(arg);
        }
        return sb.toString();
    }

    /**
     * Client to server messages.
     *
     * @author Merel Meekes
     */
    public enum Client {

        JOINROOM("joinRoom"),
        GETROOMLIST("getRoomList"),
        LEAVEROOM("leaveRoom"),
        MAKEMOVE("makeMove"),
        SENDMESSAGE("sendMessage"),
        REQUESTLEADERBOARD("requestLeaderboard"),
        SENDCAPABILITIES("sendCapabilities");

        @Getter
        private String message;

        Client(String message) {
            this.message = message;
        }

    }

    /**
     * Server to client messages.
     *
     * @author Merel Meekes
     */
    public enum Server {

        SERVERCAPABILITIES("serverCapabilities"),
        SENDLISTROOMS("sendListRooms"),
        ASSIGNID("assignID"),
        STARTGAME("startGame"),
        TURNOFPLAYER("playerTurn"),
        NOTIFYMOVE("notifyMove"),
        NOTIFYEND("notifyEnd"),
        ERROR("error"),
        NOTIFYMESSAGE("notifyMessage"),
        SENDLEADERBOARD("sendLeaderBoard");

        @Getter
        private String message;

        Server(String message) {
            this.message = message;
        }

    }

}
