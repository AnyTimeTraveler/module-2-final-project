package ss.project.shared;

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

    /**
     * Client to server messages.
     *
     * @author Merel Meekes
     */
    public static class Client {

        public static final String SENDCAPABILITIES = "sendCapabilities";

        public static final String JOINROOM = "joinRoom";

        public static final String GETROOMLIST = "getRoomList";

        public static final String LEAVEROOM = "leaveRoom";

        public static final String MAKEMOVE = "makeMove";

        public static final String SENDMESSAGE = "sendMessage";

        public static final String REQUESTLEADERBOARD = "requestLeaderboard";

    }

    /**
     * Server to client messages.
     *
     * @author Merel Meekes
     */
    public class Server {

        public static final String SERVERCAPABILITIES = "serverCapabilities";

        public static final String SENDLISTROOMS = "sendListRooms";

        public static final String ASSIGNID = "assignID";

        public static final String STARTGAME = "startGame";

        public static final String TURNOFPLAYER = "playerTurn";

        public static final String NOTIFYMOVE = "notifyMove";

        public static final String NOTIFYEND = "notifyEnd";

        public static final String ERROR = "error";

        public static final String NOTIFYMESSAGE = "notifyMessage";

        public static final String SENDLEADERBOARD = "sendLeaderBoard";

    }

}
