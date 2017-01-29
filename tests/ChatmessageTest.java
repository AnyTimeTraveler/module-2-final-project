import org.junit.Assert;
import org.junit.Test;
import ss.project.shared.Protocol;
import ss.project.shared.model.ChatMessage;

/**
 * Created by simon on 27.01.17.
 */
public class ChatmessageTest {
    @Test
    public void fromString() {
        String name = "Simon";
        String message = "Hello, this is a Test.";
        String serializedMessage = Protocol.createMessage(Protocol.Server.NOTIFYMESSAGE, name, message);
        ChatMessage chatMessage = ChatMessage.fromString(serializedMessage);
        Assert.assertEquals(name, chatMessage.getName());
        Assert.assertEquals(message, chatMessage.getMessage());
        Assert.assertEquals(new ChatMessage(name, message), chatMessage);
    }

    @Test
    public void serialize() {
        String name = "Simon";
        String message = "Hello, this is a Test.";
        ChatMessage chatMessage = new ChatMessage(name, message);
        ChatMessage chatMessage1 = ChatMessage.fromString(Protocol.createMessage(Protocol.Server.NOTIFYMESSAGE, chatMessage));

        Assert.assertEquals(chatMessage, chatMessage1);
    }
}
