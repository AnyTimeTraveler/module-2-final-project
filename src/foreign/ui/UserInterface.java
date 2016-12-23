package foreign.ui;

import ss.project.server.Controller;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by simon on 23.12.16.
 */
public abstract class UserInterface implements Runnable {
    private Controller controller;
    public void showWorld(){
        controller.getWorld();
    }
}
