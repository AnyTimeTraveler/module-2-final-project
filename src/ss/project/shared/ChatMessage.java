package ss.project.shared;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

/**
 * Created by simon on 26.01.17.
 */
@Data
public class ChatMessage {
    @Setter(AccessLevel.PRIVATE)
    private long date;
    private String message;

    public ChatMessage(String message) {
        setDate(System.currentTimeMillis());
        setMessage(message);
    }

    public static ChatMessage fromString(String line) {
        if (Protocol.Server.NOTIFYMESSAGE.equals(line.substring(0, line.indexOf(' ')))) {
            return new ChatMessage(line.substring(line.indexOf(' ') + 1));
        } else {
            return new ChatMessage(line);
        }
    }
}
