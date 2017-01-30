package ss.project.shared.model;

import lombok.Data;
import ss.project.shared.Serializable;

/**
 * A model class that contains data about a chat message.
 * Is used in the Controller and showed in the view.
 * <p>
 * Created by simon on 26.01.17.
 */
@Data
public class ChatMessage implements Serializable {
    /**
     * The name of the player that sent this message.
     */
    private String name;
    /**
     * The message itself.
     */
    private String message;

    /**
     * Create a new chatmessage with the specified sender and message.
     *
     * @param name    The name of the player who sent this message.
     * @param message The message the player sent.
     */
    public ChatMessage(String name, String message) {
        setName(name);
        setMessage(message);
    }

    /**
     * Create a ChatMessage object from a string that is formatted as the protocol describes.
     *
     * @param line
     * @return A new ChatMessage object with correct owner and message.
     */
    public static ChatMessage fromString(String line) {
        int nameStart = line.indexOf(' ') + 1;
        int messageStart = line.indexOf(' ', nameStart);
        return new ChatMessage(line.substring(nameStart, messageStart), line.substring(messageStart + 1));
    }

    /**
     * Convert this ChatMessage into a string that can be send over the network.
     *
     * @return
     */
    public String serialize() {
        return name + " " + message;
    }
}
