package ss.project.shared.model;

import lombok.Data;
import ss.project.shared.Serializable;

/**
 * Created by simon on 26.01.17.
 */
@Data
public class ChatMessage implements Serializable {
    private String name;
    private String message;

    public ChatMessage(String name, String message) {
        setName(name);
        setMessage(message);
    }

    public static ChatMessage fromString(String line) {
        int nameStart = line.indexOf(' ') + 1;
        int messageStart = line.indexOf(' ', nameStart);
        return new ChatMessage(line.substring(nameStart, messageStart), line.substring(messageStart + 1));
    }

    public String serialize() {
        return name + " " + message;
    }
}
