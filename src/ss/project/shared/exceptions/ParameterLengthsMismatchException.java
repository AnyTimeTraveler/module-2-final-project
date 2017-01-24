package ss.project.shared.exceptions;

/**
 * Created by simon on 24.01.17.
 */
public class ParameterLengthsMismatchException extends ProtocolException {
    public ParameterLengthsMismatchException(int expected, int actual) {
        super("Amount of parameters differs!\nExpected: " + expected + "\nActual: " + actual);
    }
}
