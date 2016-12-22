package ss.project.shared.network.events;

/**
 * Created by simon on 18.12.16.
 */
public class UIWriteEvent implements Event {
    private final String text;

    public UIWriteEvent(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
