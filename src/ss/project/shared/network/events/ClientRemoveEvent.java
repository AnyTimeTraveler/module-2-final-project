package ss.project.shared.network.events;

import java.nio.channels.SocketChannel;

public class ClientRemoveEvent implements Event {
    private SocketChannel socketChannel;

    public ClientRemoveEvent(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }

    @Override
    public String toString() {
        return "ClientRemoveEvent: " + socketChannel;
    }
}
