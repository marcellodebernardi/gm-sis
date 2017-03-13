package controllers;

import domain.DiagRepBooking;
import domain.Mechanic;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import jfxtras.internal.scene.control.skin.agenda.AgendaDaySkin;
import jfxtras.internal.scene.control.skin.agenda.AgendaSkin;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.control.agenda.AgendaSkinSwitcher;
import logic.BookingSystem;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
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
