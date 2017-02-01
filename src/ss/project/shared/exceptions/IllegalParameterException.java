package ss.project.shared.exceptions;

/**
 * Thrown when, for example, an argument was expected to be a Integer, but is a text.
 *
 * Created by simon on 24.01.17.
 */
public class IllegalParameterException extends ProtocolException {
    public IllegalParameterException(String message) {
        super(message);
    }
}
