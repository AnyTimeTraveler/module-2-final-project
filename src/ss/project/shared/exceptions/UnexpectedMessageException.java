package ss.project.shared.exceptions;

/**
 * Created by simon on 24.01.17.
 */
public class UnexpectedMessageException extends ProtocolException {
    public UnexpectedMessageException(String expected, String actual) {
        super("Did not receive expected message:\nExpected: " + expected + "\nActual: " + actual);
    }
}
