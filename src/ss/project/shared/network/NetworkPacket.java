package ss.project.shared.network;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 * Created by simon on 09.12.16.
 */
public class NetworkPacket {
    private PacketType type;

    public NetworkPacket(byte[] data) {

    }

    public static ArrayList<NetworkInterface> getMulticastInterfaces() throws SocketException {
        ArrayList<NetworkInterface> compatibleInterfaces = new ArrayList<NetworkInterface>();
        Enumeration<NetworkInterface> availableIntefaces = NetworkInterface.getNetworkInterfaces();

        while (availableIntefaces.hasMoreElements()) {
            NetworkInterface networkInterface = availableIntefaces.nextElement();
            if (!networkInterface.isLoopback() &&
                    !networkInterface.isVirtual() &&
                    networkInterface.isUp() &&
                    networkInterface.supportsMulticast()) {
                compatibleInterfaces.add(networkInterface);
            }
        }
        return compatibleInterfaces;
    }

    public PacketType getType() {
        return type;
    }
}
