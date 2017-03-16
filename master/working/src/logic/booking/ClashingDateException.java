package logic.booking;

import domain.DiagRepBooking;

/**
 * @author Marcello De Bernardi
 */
public class ClashingDateException extends UnavailableDateException {
    private DiagRepBooking clash;


    ClashingDateException with(DiagRepBooking booking) {
        clash = booking;
        return this;
    }
}
