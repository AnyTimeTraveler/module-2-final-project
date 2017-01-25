package ss.project.client.networking;

import lombok.Data;
import ss.project.shared.Protocol;

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

    public enum Status {
        ONLINE,
        INCOMPATIBLE,
        OFFLINE
    }
}
