package ss.project.client.networking;

import lombok.Data;
import ss.project.shared.Protocol;
import ss.project.shared.game.Vector3;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Created by simon on 22.01.17.
 */
@Data
public class ServerInfo {
    private Status status;
    private int maxPlayers;
    private boolean roomSupport;
    private int maxDimensionX;
    private int maxDimensionY;
    private int maxDimensionZ;
    private int maxWinLength;
    private boolean chatSupport;

    private ServerInfo() {
        status = Status.INCOMPATIBLE;
    }

    /**
     * Create a new ServerInfo.
     *
     * @param status        Status of this server.
     * @param maxPlayers    The amount of players this server supports.
     * @param roomSupport   If true, this server supports rooms (lobbies).
     * @param maxDimensionX The maximum X dimension of the world.
     * @param maxDimensionY The maximum Y dimension of the world.
     * @param maxDimensionZ The maximum Z dimension of the world.
     * @param maxWinLength  The length needed to win in a game.
     * @param chatSupport   If true, this server supports global chat.
     */
    public ServerInfo(Status status, int maxPlayers, boolean roomSupport, int maxDimensionX, int maxDimensionY, int maxDimensionZ, int maxWinLength, boolean chatSupport) {
        this.status = status;
        this.maxPlayers = maxPlayers;
        this.roomSupport = roomSupport;
        this.maxDimensionX = maxDimensionX;
        this.maxDimensionY = maxDimensionY;
        this.maxDimensionZ = maxDimensionZ;
        this.maxWinLength = maxWinLength;
        this.chatSupport = chatSupport;
    }

    /**
     * Create a ServerInfo object from protocol string.
     *
     * @param line The string from the protocol.
     * @return A new ServerInfo object, created from the string.
     */
    public static ServerInfo fromString(String line) {
        try {
            ServerInfo serverInfo = new ServerInfo();
            serverInfo.status = Status.ONLINE;
            Scanner sc = new Scanner(line);
            if (line.split(" ")[0].equals(Protocol.Server.SERVERCAPABILITIES.getMessage())) {
                sc.next();
            }
            serverInfo.maxPlayers = Integer.parseInt(sc.next());
            serverInfo.roomSupport = sc.next().equals("1");
            serverInfo.maxDimensionX = Integer.parseInt(sc.next());
            serverInfo.maxDimensionY = Integer.parseInt(sc.next());
            serverInfo.maxDimensionZ = Integer.parseInt(sc.next());
            serverInfo.maxWinLength = Integer.parseInt(sc.next());
            serverInfo.chatSupport = sc.next().equals("1");
            return serverInfo;
        } catch (NumberFormatException | NoSuchElementException e) {
            //TODO: instead of printing stacktrace, handle the error and send error code.
            e.printStackTrace();
        }
        return new ServerInfo();
    }

    /**
     * Get the world size of this server.
     *
     * @return A vector3 containing X,Y,Z.
     */
    public Vector3 getWorldSize() {
        return new Vector3(maxDimensionX, maxDimensionY, maxDimensionZ);
    }

    /**
     * Possible status for the Server.
     */
    public enum Status {
        /**
         * Server is online and can be connected to.
         */
        ONLINE,
        /**
         * Server is not compatible with our protocol.
         */
        INCOMPATIBLE,
        /**
         * Server is offline, and not usable to play.
         */
        OFFLINE
    }
}
