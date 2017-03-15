package controllers.booking;

import domain.DiagRepBooking;
import domain.Mechanic;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import jfxtras.scene.control.agenda.Agenda;
import logic.BookingSystem;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Marcello De Bernardi
 */
public class CalendarPaneController {
    private BookingController master;
    private BookingSystem bookingSystem;

    // calendar and list panes top control bar
    @FXML private ComboBox calendarMechanicComboBox;
    @FXML private DatePicker calendarDatePicker;
    @FXML private Agenda bookingAgenda;


    public CalendarPaneController() {
        master = BookingController.getInstance();
        bookingSystem = BookingSystem.getInstance();
    }

    @FXML private void initialize() {
        populateCalendarMechanicComboBox(bookingSystem.getAllMechanics());
        populateAgenda(bookingSystem.getAllBookings());

        bookingAgenda.setActionCallback((appointment) -> {
            DiagRepBooking booking = bookingSystem.getBookingByID(((BookingAppointment) appointment).getBookingID());
            ((DetailsPaneController) master.getController(DetailsPaneController.class)).populateDetailFields(booking);
            return null;
        });
        bookingAgenda.setEditAppointmentCallback((appointment) -> null );

        master.setController(CalendarPaneController.class, this);
    }

    ///////////////////// EVENT HANDLERS ////////////////////////////
    @FXML private void selectCalendarDate() {
        bookingAgenda.setDisplayedLocalDateTime(LocalDateTime.of(calendarDatePicker.getValue(),
                LocalTime.now()));
    }

    @FXML private void selectCalendarMechanic() {
        Mechanic mechanic = bookingSystem.getMechanicByID(Integer.parseInt(calendarMechanicComboBox
                .getSelectionModel()
                .getSelectedItem()
                .toString()
                .split(":")[0])
        );
        // todo do stuff
    }

    @FXML private void openListPane() {
        try {
            master.setCenter(FXMLLoader.load(getClass().getResource("/resources/booking/ListPane.fxml")));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    //////////////////// DATA MANIPULATIONS /////////////////////////
    void refreshAGenda() {
        bookingAgenda.appointments().clear();
        populateAgenda(bookingSystem.getAllBookings());
    }

    void addBookingAppointment(DiagRepBooking booking) {
        this.bookingAgenda.appointments().add(new Agenda.AppointmentImplLocal()
                .withStartLocalDateTime(LocalDateTime.ofInstant(booking.getDiagnosisStart().toInstant(),
                        booking.getDiagnosisStart().getZone()))
                .withEndLocalDateTime(LocalDateTime.ofInstant(booking.getDiagnosisEnd().toInstant(),
                        booking.getDiagnosisEnd().getZone()))
                .withDescription(booking.getDescription())
        );
    }

    private void populateCalendarMechanicComboBox(List<Mechanic> mechanics) {
        List<String> mechanicInfo = new ArrayList<>();
        for (Mechanic m : mechanics) {
            mechanicInfo.add(m.getMechanicID() + ": " + m.getFirstName() + " " + m.getSurname());
        }
        ObservableList<String> calendarMechanicInfoObservable = FXCollections.observableArrayList(mechanicInfo);
        calendarMechanicComboBox.setItems(calendarMechanicInfoObservable);
    }

    private void populateAgenda(List<DiagRepBooking> bookings) {
        for (DiagRepBooking booking : bookings) {
            bookingAgenda.appointments().add(new BookingAppointment().asDiagnosis(booking));

            if (booking.getRepairStart() != null)
                bookingAgenda.appointments().add(new BookingAppointment().asRepair(booking));
        }
        bookingAgenda.setAllowDragging(true);
        bookingAgenda.setAllowResize(true);
    }
}
