package persistence;

/**
 * @author Marcello De Bernardi
 */
public class DependencyException extends RuntimeException {
    DependencyException(String message) {
        super(message);
    }
}
