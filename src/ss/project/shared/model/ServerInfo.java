package ss.project.shared.model;

import lombok.Data;
import ss.project.shared.Protocol;

import java.net.InetAddress;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Created by simon on 22.01.17.
 */
@Data
public class ServerInfo {
    private Status status;
    private Connection connection;
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
    public ServerInfo(Status status, Connection connection, int maxPlayers, boolean roomSupport, int maxDimensionX, int maxDimensionY, int maxDimensionZ, int maxWinLength, boolean chatSupport) {
        this.status = status;
        this.connection = connection;
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
     * @param address     The string from the protocol.
     * @param inetAddress
     * @param port        @return A new ServerInfo object, created from the string.
     */
    public static ServerInfo fromString(String address, InetAddress inetAddress, int port) {
        try {
            ServerInfo serverInfo = new ServerInfo();
            serverInfo.status = Status.ONLINE;
            Scanner sc = new Scanner(address);
            if (address.split(" ")[0].equals(Protocol.Server.SERVERCAPABILITIES.getMessage())) {
                sc.next();
            }
            serverInfo.maxPlayers = Integer.parseInt(sc.next());
            serverInfo.connection = new Connection("Unnamed", inetAddress.getHostAddress(), port);
            serverInfo.roomSupport = sc.next().equals("1");
            serverInfo.maxDimensionX = Integer.parseInt(sc.next());
            serverInfo.maxDimensionY = Integer.parseInt(sc.next());
            serverInfo.maxDimensionZ = Integer.parseInt(sc.next());
            serverInfo.maxWinLength = Integer.parseInt(sc.next());
            serverInfo.chatSupport = sc.next().equals("1");
            sc.close();
            return serverInfo;
        } catch (NumberFormatException | NoSuchElementException e) {
            e.printStackTrace();
            return new ServerInfo();
        }
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
