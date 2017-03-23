package logic.booking;

/**
 * @author Marcello De Bernardi.
 */
class UnavailableDateException extends Exception {
    UnavailableDateException() {
    }

    UnavailableDateException(String message) {
        super(message);
    }
}
