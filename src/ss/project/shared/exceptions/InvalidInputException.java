package ss.project.shared.exceptions;

/**
 * Created by fw on 21/01/2017.
 */
public class InvalidInputException extends Exception {
    public InvalidInputException(String rawInput) {
        super("Invalid input: " + rawInput);
    }
}
