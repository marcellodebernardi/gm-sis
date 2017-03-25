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
    private Appointment appointment;
    private List<DiagRepBooking> clashes;
    private String holiday;
    private LocalTime time;

    UnavailableDateException() {
    }

    UnavailableDateException(String message) {
        super(message);
    }


    /** Returns an enum outlining the because of the exception */
    public Cause reason() {
        return this.cause;
    }

    public Appointment concerning() throws UnsupportedOperationException {
        if (this.appointment == null) throw new UnsupportedOperationException("Appointment not specified.");
        return this.appointment;
    }

    /** Returns the bookings with which there is a clash */
    public List<DiagRepBooking> clashes() throws UnsupportedOperationException {
        if (cause != CLASHES) throw new UnsupportedOperationException("Date is not unavailable due to clash.");
        return clashes;
    }

    /** Returns the holiday on which the given time falls */
    public String holiday() throws UnsupportedOperationException {
        if (cause != HOLIDAY) throw new UnsupportedOperationException("Date is not unavailable due to holiday.");
        return holiday;
    }

    /** Returns the invalid outside-opening-hours appointment time */
    public LocalTime time() throws UnsupportedOperationException {
        if (cause != CLOSED) throw new UnsupportedOperationException("Date is not unavailable due to being closed");
        return time;
    }

    /** Sets the because of the unavailability */
    UnavailableDateException because(Cause cause) {
        this.cause = cause;
        return this;
    }

    /** Registers whether the date problem is with the diagnosis or repair appointment */
    UnavailableDateException concerning(Appointment appointment) {
        this.appointment = appointment;
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


    /** Enumerates the nature of the unavailability of the date */
    public enum Cause {
        CLASHES, CLOSED, HOLIDAY;

        public String toString() {
            if (this == CLASHES) return "The booking clashes with another booking.";
            else if (this == CLOSED) return "The garage is closed at that time";
            else return "A public or bank holiday falls on that date, and the garage will be closed";
        }
    }

    /** Enumerates whether the problematic date is that of the diagnosis of the repair */
    public enum Appointment {
        DIAGNOSIS, REPAIR
    }
}
