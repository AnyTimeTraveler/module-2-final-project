package ss.project.server;

import ss.project.shared.network.events.*;
import ss.project.shared.network.io.Network;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by simon on 18.12.16.
 */
public class ChatServer implements EventHandler {
    private Network network;
    private LinkedBlockingQueue<Event> eventQueue;
    private boolean isRunning;
    private List<SocketChannel> clients;

    private ChatServer() {
        eventQueue = new LinkedBlockingQueue<>();
        isRunning = true;
        clients = new ArrayList<>();
        try {
            network = new Network(InetAddress.getLocalHost(), 1234, this);
            Thread networkThread = new Thread(network);
            networkThread.setDaemon(true);
            networkThread.setName("Network");
            networkThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        ChatServer server = new ChatServer();
        Thread mainThread = new Thread(server);
        mainThread.start();
    }

    @Override
    public void handleEvent(Event event) {
        eventQueue.add(event);
    }

    @Override
    public void run() {
        try {
            while (isRunning) {
                Event event = eventQueue.poll(5, TimeUnit.SECONDS);

                if (event == null) {
                    continue;
                }

                System.out.println("event = " + event);
                if (event instanceof ClientRegisterEvent) {
                    clients.add(((ClientRegisterEvent) event).getSocketChannel());
                } else if (event instanceof ClientRemoveEvent) {
                    clients.remove(((ClientRemoveEvent) event).getSocketChannel());
                } else if (event instanceof DataReceivedEvent) {
                    DataReceivedEvent myEvent = (DataReceivedEvent) event;
                    System.out.println(new String(myEvent.getData().getBtyes()));
                    for (SocketChannel channel : clients) {
                        if (!channel.equals(myEvent.getData().getChannel()))
                            network.send(myEvent.getData().getChannel(), myEvent.getData().getBtyes());
                    }
                }
            }
        } catch (InterruptedException ignored) {
        }
    }

    public void close() {
        isRunning = false;
        Thread.currentThread().interrupt();
    }
}
