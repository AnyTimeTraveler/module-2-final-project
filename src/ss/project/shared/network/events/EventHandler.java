package ss.project.shared.network.events;

public interface EventHandler extends Runnable {

    void handleEvent(Event event);

}
