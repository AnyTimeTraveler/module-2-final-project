package ss.project.client.networking;

import lombok.Data;

/**
 * Created by simon on 21.01.17.
 */
@Data
public class Connection {
    private String name;
    private String address;
    private int port;

    public Connection(String name, String address, int port) {
        this.name = name;
        this.address = address;
        this.port = port;
    }


}
