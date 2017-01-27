package ss.project.client.networking;

import com.google.gson.annotations.Expose;
import lombok.Data;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;

/**
 * Created by simon on 21.01.17.
 */
@Data
public class Connection {
    @Expose
    private String name;
    @Expose
    private String address;
    @Expose
    private int port;

    public Connection(String name, String address, int port) {
        this.name = name;
        this.address = address;
        this.port = port;
    }

    /**
     * Check if a string ip is valid.
     *
     * @return True if it's valid and reachable, false it not.
     */
    public static boolean validIP(String ip) {
        try {
            InetAddress address = Inet4Address.getByName(ip);
            return address.isReachable(1000);
        } catch (IOException e) {
            return false;
        }
    }
}
