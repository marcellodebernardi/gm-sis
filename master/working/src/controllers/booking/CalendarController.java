package controllers.booking;

import domain.DiagRepBooking;
import domain.Mechanic;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.control.agenda.Agenda.AppointmentGroupImpl;
import logic.booking.BookingSystem;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * @author Marcello De Bernardi
 */
public class CalendarController {
    // controller state
    private BookingController master;
    private BookingSystem bookingSystem;
    // calendar and list panes top control bar
    @FXML private ComboBox calendarMechanicComboBox;
    @FXML private DatePicker calendarDatePicker;
    @FXML private Agenda bookingAgenda;
    // appointment groups
    private Agenda.AppointmentGroup diagnosis;
    private Agenda.AppointmentGroup repair;
    private Agenda.AppointmentGroup holiday;


    public CalendarController() {
        master = BookingController.getInstance();
        bookingSystem = BookingSystem.getInstance();

        diagnosis = new AppointmentGroupImpl().withStyleClass("diagnosisAppointment");
        repair = new AppointmentGroupImpl().withStyleClass("repairAppointment");
        holiday = new AppointmentGroupImpl().withStyleClass("holidayAppointment");
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                     INITIALIZATION                                                  //
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    @FXML private void initialize() {
        bookingAgenda.setAllowDragging(false);
        bookingAgenda.setAllowResize(false);

        initializeMechanicDropdown();
        initializeAgenda();
        populateAgenda(bookingSystem.getAllBookings());

        master.setController(CalendarController.class, this);
    }

    /** Initializes the mechanic combo box */
    @SuppressWarnings("Duplicates")
    private void initializeMechanicDropdown() {
        calendarMechanicComboBox.setCellFactory(p -> new ListCell<Mechanic>() {
            public void updateItem(Mechanic item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setText("");
                else setText(item.getFirstName() + " " + item.getSurname());
            }
        });
        calendarMechanicComboBox.setButtonCell(new ListCell<Mechanic>() {
            public void updateItem(Mechanic item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setText("");
                else setText(item.getFirstName() + " " + item.getSurname());
            }
        });

        calendarMechanicComboBox.setItems(FXCollections.observableArrayList(bookingSystem.getAllMechanics()));
        calendarMechanicComboBox.getSelectionModel().select(0);
    }

    /** Sets the action callback and edit appointment callback for the agenda */
    private void initializeAgenda() {
        bookingAgenda.setActionCallback((appointment) -> {
            DiagRepBooking booking = bookingSystem.getBookingByID(((BookingAppointment) appointment).getBookingID());
            ((DetailsController) master.getController(DetailsController.class)).populate(booking);
            ((DetailsController) master.getController(DetailsController.class)).setPaneTitleToView();
            return null;
        });
        bookingAgenda.setEditAppointmentCallback((appointment) -> null);
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                      EVENT HANDLERS                                                 //
    /////////////////////////////////////////////////////////////////////////////////////////////////////////

    /** Changes the time range displayed by the agenda */
    @FXML private void selectCalendarDate() {
        bookingAgenda.setDisplayedLocalDateTime(LocalDateTime.of(calendarDatePicker.getValue(),
                LocalTime.now()));
    }

    /** Changes the mechanics whose bookings are displayed on the agenda */
    @FXML private void selectCalendarMechanic() {
        Mechanic mechanic = (Mechanic) calendarMechanicComboBox.getSelectionModel().getSelectedItem();
        refreshAgenda(mechanic.getBookings());
    }

    /** Switches to the table view of bookings */
    @FXML private void openListPane() {
        try {
            master.setCenter(FXMLLoader.load(getClass().getResource("/resources/booking/ListPane.fxml")));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML private void openVehiclePane() {
        try {
            master.setCenter(FXMLLoader.load(getClass().getResource("/resources/booking/VehiclePane.fxml")));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                     FIELD STATE MANAGERS                                            //
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    void refreshAgenda(List<DiagRepBooking> bookings) {
        bookingAgenda.appointments().clear();
        populateAgenda(bookings);
    }

    void addAppointment(DiagRepBooking booking) {
        bookingAgenda.appointments().removeIf(p ->
                p instanceof BookingAppointment && ((BookingAppointment) p).getBookingID() == booking.getBookingID()
        );

        this.bookingAgenda.appointments().add(new BookingAppointment()
                .forBooking(booking)
                .asDiagnosis()
                .withAppointmentGroup(diagnosis));
        if (booking.getRepairStart() != null)
            bookingAgenda.appointments().add(new BookingAppointment()
                    .forBooking(booking)
                    .asRepair()
                    .withAppointmentGroup(repair));
    }

    private void populateAgenda(List<DiagRepBooking> bookings) {
        for (DiagRepBooking booking : bookings) {
            bookingAgenda.appointments().add(new BookingAppointment()
                    .forBooking(booking)
                    .asDiagnosis()
                    .withAppointmentGroup(diagnosis));

            if (booking.getRepairStart() != null)
                bookingAgenda.appointments().add(new BookingAppointment()
                        .forBooking(booking)
                        .asRepair()
                        .withAppointmentGroup(repair));
        }

        for (LocalDate h : bookingSystem.getAllHolidays().keySet()) {
            bookingAgenda.appointments().add(new HolidayAppointment().withAppointmentGroup(holiday).onDate(h));
        }
    }
}
