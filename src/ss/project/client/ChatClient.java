package ss.project.client;

import ss.project.shared.network.events.DataReceivedEvent;
import ss.project.shared.network.events.Event;
import ss.project.shared.network.events.EventHandler;
import ss.project.shared.network.events.UIWriteEvent;
import ss.project.shared.network.io.Network;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.channels.SocketChannel;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by simon on 18.12.16.
 */
public class ChatClient implements Runnable, EventHandler {
    private SocketChannel server;
    private Network network;
    private LinkedBlockingQueue<Event> eventQueue;
    private boolean isRunning;
    private ChatUI ui;

    private ChatClient() {
        eventQueue = new LinkedBlockingQueue<>();
        isRunning = true;
        try {
            network = new Network(this);
            Thread networkThread = new Thread(network);
            networkThread.setDaemon(true);
            networkThread.setName("Network");
            networkThread.start();
            server = network.connect(InetAddress.getLocalHost(), 1234);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        ui = new ChatUI(eventQueue);
        ui.start();
    }

    public static void main(String[] args) {
        ChatClient client = new ChatClient();
        Thread mainThread = new Thread(client);
        mainThread.start();
    }

    @Override
    public void run() {
        try {
            while (isRunning) {
                Event event = eventQueue.poll(5, TimeUnit.SECONDS);
                if (event == null) {
                    continue;
                }

                if (event instanceof UIWriteEvent) {
                    network.send(server, ((UIWriteEvent) event).getText().getBytes());
                } else if (event instanceof DataReceivedEvent) {
                    System.out.println(new String(((DataReceivedEvent) event).getData().getBtyes()));
                } else {
                    System.out.println("event = " + event);
                }
            }
        } catch (InterruptedException ignored) {
        }
    }

    public void close() {
        isRunning = false;
        Thread.currentThread().interrupt();
    }

    @Override
    public void handleEvent(Event event) {
        eventQueue.add(event);
    }
}
