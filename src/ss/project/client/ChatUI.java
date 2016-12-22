package ss.project.client;

import ss.project.shared.network.events.Event;
import ss.project.shared.network.events.UIWriteEvent;

import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by simon on 18.12.16.
 */
public class ChatUI implements Runnable {
    private final LinkedBlockingQueue<Event> queue;
    private boolean isRunning;

    public ChatUI(LinkedBlockingQueue<Event> queue) {
        this.queue = queue;
    }

    public void start() {
        Thread uiThread = new Thread(this);
        uiThread.setDaemon(true);
        uiThread.setName("ChatUI");
        uiThread.start();
    }

    @Override
    public void run() {
        isRunning = true;
        Scanner sc = new Scanner(System.in);
        while (isRunning) {
            String text = sc.nextLine();
            if (text.equalsIgnoreCase("exit")) {
                isRunning = false;
            } else {
                queue.add(new UIWriteEvent(text));
                System.out.println("Sending...");
            }
        }
    }
}
