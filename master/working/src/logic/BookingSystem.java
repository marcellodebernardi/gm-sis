package logic;

import entities.DiagRepBooking;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.List;
import static logic.CriterionOperator.EqualTo;

/**
 * @author Marcello De Bernardi
 * @version 0.1
 * @since 0.1
 */
public class BookingSystem {
    private static BookingSystem instance;
    private CriterionRepository persistence;
    private LocalTime OPENING_HOUR = new LocalTime(9, 0);
    private LocalTime CLOSING_HOUR = new LocalTime(17, 0);
    private ArrayList<LocalDate> HOLIDAYS;


    private BookingSystem(CriterionRepository persistence) {
        this.persistence = persistence;

        // todo make more flexible
        HOLIDAYS = new ArrayList<>();
        HOLIDAYS.add(new LocalDate(1, 1, 2017));
        HOLIDAYS.add(new LocalDate(17, 3, 2017));
        HOLIDAYS.add(new LocalDate(14, 4, 2017));
        HOLIDAYS.add(new LocalDate(17, 4, 2017));
        HOLIDAYS.add(new LocalDate(1, 5, 2017));
        HOLIDAYS.add(new LocalDate(29, 5, 2017));
        HOLIDAYS.add(new LocalDate(7, 8, 2017));
        HOLIDAYS.add(new LocalDate(28, 8, 2017));
        HOLIDAYS.add(new LocalDate(30, 11, 2017));
        HOLIDAYS.add(new LocalDate(25, 12, 2017));
        HOLIDAYS.add(new LocalDate(26, 12, 2017));
        HOLIDAYS.add(new LocalDate(1, 1, 2018));
        HOLIDAYS.add(new LocalDate(30, 3, 2018));
        HOLIDAYS.add(new LocalDate(2, 4, 2018));
        HOLIDAYS.add(new LocalDate(7, 5, 2018));
        HOLIDAYS.add(new LocalDate(28, 5, 2018));
        HOLIDAYS.add(new LocalDate(27, 8, 2018));
        HOLIDAYS.add(new LocalDate(25, 12, 2018));
        HOLIDAYS.add(new LocalDate(26, 12, 2018));
    }


    /**
     * Returns the singleton instance of the BookingSystem.
     *
     * @return singleton instance of BookingSystem
     */
    public static BookingSystem getInstance(CriterionRepository persistence) {
        if (instance == null) instance = new BookingSystem(persistence);
        return instance;
    }

    /**
     * Returns a list containing all bookings for diagnosis and repair.
     *
     * @return List of diagnosis and repair bookings
     */
    public List<DiagRepBooking> getAllBookings() {
        return persistence.getByCriteria(new Criterion<>(DiagRepBooking.class));
    }

    public List<DiagRepBooking> getTodayBookings() {
        try {
            return persistence.getByCriteria(
                    new Criterion<>(DiagRepBooking.class, "diagnosisDate", EqualTo, new LocalDate())
                            .or("repairDate", EqualTo, new LocalDate()));
        }
        catch(CriterionException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    /**
     * <p>
     * Adds a new booking to the persistence layer. Assumes an existing customer and
     * vehicle.
     * </p>
     *
     * @return true if addition successful, false otherwise
     */
    public boolean addBooking(DiagRepBooking booking) {
        // todo come up with way to ensure ID is not missing
        return isWithinOpenHours(booking)
                && isNotOnHoliday(booking)
                && persistence.addItem(DiagRepBooking.class, booking);
    }

    /**
     * <p>
     * Edits an existing booking in the persistence layer. The booking is identified
     * using the unique bookingID, so that must be present in the database.
     * </p>
     *
     * @param booking the booking to edit
     * @return true if successful, false otherwise
     */
    public boolean editBooking(DiagRepBooking booking) {
        // todo same as above
        return isWithinOpenHours(booking)
                && isNotOnHoliday(booking)
                && persistence.updateItem(DiagRepBooking.class, booking);
    }

    /**
     * Removes a specified booking from the system.
     *
     * @return true if removal successful, false otherwise
     */
    public boolean deleteBooking(int bookingID) {
        // todo same as above
        return persistence.deleteItem(new Criterion<>(DiagRepBooking.class, "bookingID", EqualTo, bookingID));
    }


    /* Checks time validity in terms of opening and closing hours as well as weekdays. */
    private boolean isWithinOpenHours(DiagRepBooking booking) {
        return (!(booking.getDiagnosisInterval().getStart().toLocalTime().compareTo(OPENING_HOUR) < 0
                || booking.getDiagnosisInterval().getEnd().toLocalTime().compareTo(CLOSING_HOUR) > 0
                || booking.getRepairInterval().getStart().toLocalTime().compareTo(OPENING_HOUR) < 0
                || booking.getRepairInterval().getEnd().toLocalTime().compareTo(CLOSING_HOUR) > 0
                || booking.getDiagnosisInterval().getStart().toLocalDate().getDayOfWeek() < 1
                || booking.getDiagnosisInterval().getStart().toLocalDate().getDayOfWeek() > 5
                || booking.getRepairInterval().getStart().toLocalDate().getDayOfWeek() > 5
                || booking.getRepairInterval().getStart().toLocalDate().getDayOfWeek() > 5));
    }

    /* Checks the booking is not for a bank or public holiday */
    private boolean isNotOnHoliday(DiagRepBooking booking) {
        return (!(HOLIDAYS.contains(booking.getDiagnosisInterval().getStart().toLocalDate())
                || HOLIDAYS.contains(booking.getRepairInterval().getStart().toLocalDate())));
    }
}
