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

    /**
     * Check if a string ip is valid.
     *
     * @param ip
     * @return True if it's valid, false it not.
     */
    public static boolean validIP(String ip) {
        try {
            if (ip == null || ip.isEmpty()) {
                return false;
            }

            if (ip.equals("localhost")) {
                return true;
            }

            String[] parts = ip.split("\\.");
            if (parts.length != 4) {
                return false;
            }

            for (String s : parts) {
                int i = Integer.parseInt(s);
                if ((i < 0) || (i > 255)) {
                    return false;
                }
            }
            if (ip.endsWith(".")) {
                return false;
            }

            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
}
