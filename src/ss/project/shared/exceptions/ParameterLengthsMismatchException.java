package ss.project.shared.exceptions;

/**
 * Thrown when, for example, a list of arguments is expected with all
 * 4 sub-arguments, but it contains less sub-arguments.
 * <p>
 * Created by simon on 24.01.17.
 */
public class ParameterLengthsMismatchException extends ProtocolException {
    public ParameterLengthsMismatchException(int expected, int actual) {
        super("Amount of parameters differs!\nExpected: " + expected + "\nActual: " + actual);
    }
}
