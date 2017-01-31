package ss.project.shared.model;

import com.google.gson.annotations.Expose;
import lombok.Data;

import java.io.IOException;
import java.net.InetAddress;

/**
 * A model class that contains information about a connection.
 * Used in the serverbrowser.
 * Created by simon on 21.01.17.
 */
@Data
public class Connection {
    /**
     * The name of this connection.
     */
    @Expose
    private String name;
    /**
     * The ipv4 address.
     */
    @Expose
    private String address;
    /**
     * The port number of this connection.
     */
    @Expose
    private int port;

    /**
     * Create a new connection with a specified name.
     *
     * @param name    The name of this connection
     * @param address The ipv4 address.
     * @param port    The port of this connection.
     */
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
            InetAddress address = InetAddress.getByName(ip);
            return address.isReachable(1000);
        } catch (IOException e) {
            return false;
        }
    }
}
