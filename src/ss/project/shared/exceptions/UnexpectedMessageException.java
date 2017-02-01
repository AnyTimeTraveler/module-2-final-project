package ss.project.shared.exceptions;

/**
 * Thrown when a different message than the expected one is received.
 * <p>
 * Created by simon on 24.01.17.
 */
public class UnexpectedMessageException extends ProtocolException {
    public UnexpectedMessageException(String expected, String actual) {
        super("Did not receive expected message:\nExpected: " + expected + "\nActual: " + actual);
    }
}
