package logic.booking;

import domain.DiagRepBooking;

import java.time.LocalTime;
import java.util.List;

import static logic.booking.UnavailableDateException.Cause.*;

/**
 * @author Marcello De Bernardi.
 */
public class UnavailableDateException extends Exception {
    private Cause cause;
    private List<DiagRepBooking> clashes;
    private String holiday;
    private LocalTime time;

    UnavailableDateException() {
    }

    UnavailableDateException(String message) {
        super(message);
    }


    /** Sets the cause of the unavailability */
    UnavailableDateException because(Cause cause) {
        this.cause = cause;
        return this;
    }

    /** Sets the list of bookings with which the booking clashes */
    UnavailableDateException with(List<DiagRepBooking> bookings) {
        if (cause != CLASHES) throw new UnsupportedOperationException("Date is not unavailable due to clash.");
        clashes = bookings;
        return this;
    }

    /** Sets the holiday due to which the date is not available */
    UnavailableDateException on(String holiday) {
        if (cause != HOLIDAY) throw new UnsupportedOperationException("Date is not unavailable due to holiday");
        this.holiday = holiday;
        return this;
    }

    UnavailableDateException at(LocalTime time) {
        if (cause != CLOSED) throw new UnsupportedOperationException("Date is not unavailable due to being closed");
        this.time = time;
        return this;
    }

    /** Returns an enum outlining the cause of the exception */
    public Cause because() {
        return this.cause;
    }


    public enum Cause {
        CLASHES, CLOSED, HOLIDAY
    }
}
