package ss.project.server;

import ss.project.shared.IStartStop;
import ss.project.shared.network.events.Event;
import ss.project.shared.network.events.EventHandler;
import ss.project.shared.network.io.Network;
import ss.project.shared.ui.ServerTUI;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by simon on 09.12.16.
 */
public class Controller implements IStartStop, Runnable, EventHandler {
    public static final String HOST = "130.89.239.116";
    public static final int PORT = 1234;
    private Network network;

    private Thread tController;
    private ExecutorService tEngines;

    private boolean isRunning = true;
    private ServerTUI ui;

    public static void main(String[] args) throws InterruptedException {
        Controller controller = new Controller();
        controller.start();
        Thread.sleep(10 * 1000);
        controller.stop();
    }

    @Override
    public void run() {
        while (isRunning) {

        }
    }


    @Override
    public void start() {
        try {
            network = new Network(InetAddress.getByName(HOST), PORT, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ui = new ServerTUI();
        tController = new Thread(this);
        tEngines = Executors.newCachedThreadPool();
        tController.start();
    }

    @Override
    public void stop() {
        network.close();
        isRunning = false;
    }

    @Override
    public void handleEvent(Event event) {

    }
}
