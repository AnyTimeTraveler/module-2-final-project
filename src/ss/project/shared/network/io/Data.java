package ss.project.shared.network.io;

import java.io.Serializable;
import java.nio.channels.SocketChannel;

public class Data implements Serializable {
    private static final long serialVersionUID = -6888334462744644445L;
    private SocketChannel channel;
    private byte[] data;

    public Data(SocketChannel channel, byte[] array) {
        this.channel = channel;
        data = array;
    }

    public SocketChannel getChannel() {
        return channel;
    }

    public byte[] getBtyes() {
        return data;
    }
}
