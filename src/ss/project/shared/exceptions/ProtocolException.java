package ss.project.shared.exceptions;

/**
 * An abstract class used when something is not right about the message we receive according to the protocol.
 *
 * Created by simon on 24.01.17.
 */
public abstract class ProtocolException extends Exception {
    public ProtocolException(String message) {
        super(message);
    }
}
