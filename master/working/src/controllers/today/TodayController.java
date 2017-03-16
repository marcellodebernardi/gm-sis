package controllers.today;

import domain.DiagRepBooking;
import javafx.fxml.FXML;
import jfxtras.internal.scene.control.skin.agenda.AgendaDaySkin;
import jfxtras.scene.control.agenda.Agenda;
import logic.booking.BookingSystem;

import java.util.List;

/**
 * @author Marcello De Bernardi
 */
public class TodayController {
    private BookingSystem bookingSystem;

    @FXML private Agenda todayBookingsAgenda;


    public TodayController() {
        bookingSystem = BookingSystem.getInstance();
    }

    @FXML private void initialize() {
        todayBookingsAgenda.setSkin(new AgendaDaySkin(todayBookingsAgenda));
        populateAgenda(bookingSystem.getAllBookings());
    }

    ///////////////////// EVENT HANDLERS ////////////////////////////


    //////////////////// DATA MANIPULATIONS /////////////////////////
    private void populateAgenda(List<DiagRepBooking> bookings) {
    }
}
