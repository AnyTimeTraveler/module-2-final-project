package ss.project.shared.network.events;

import java.nio.channels.SocketChannel;

public class ClientRegisterEvent implements Event {
    private SocketChannel socketChannel;

    public ClientRegisterEvent(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }

    @Override
    public String toString() {
        return "ClientRegisterEvent: " + socketChannel;
    }
}
