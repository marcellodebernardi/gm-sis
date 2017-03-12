package logic;

import domain.DiagRepBooking;
import domain.Mechanic;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import persistence.DatabaseRepository;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;

import static logic.CriterionOperator.*;

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


    private BookingSystem() {
        this.persistence = DatabaseRepository.getInstance();

        // todo make more flexible
        HOLIDAYS = new ArrayList<>();
        HOLIDAYS.add(new LocalDate(2017, 1, 1));
        HOLIDAYS.add(new LocalDate(2017, 3, 17));
        HOLIDAYS.add(new LocalDate(2017, 4, 17));

        HOLIDAYS.add(new LocalDate(2017, 5, 1));
        HOLIDAYS.add(new LocalDate(2017, 5, 29));
        HOLIDAYS.add(new LocalDate(2017, 8, 7));
        HOLIDAYS.add(new LocalDate(2017, 8, 28));
        HOLIDAYS.add(new LocalDate(2017, 11, 30));
        HOLIDAYS.add(new LocalDate(2017, 12, 25));
        HOLIDAYS.add(new LocalDate(2017, 12, 26));
        HOLIDAYS.add(new LocalDate(2018, 1, 1));
        HOLIDAYS.add(new LocalDate(2018, 3, 30));
        HOLIDAYS.add(new LocalDate(2018, 4, 2));
        HOLIDAYS.add(new LocalDate(2018, 5, 7));
        HOLIDAYS.add(new LocalDate(2018, 5, 28));
        HOLIDAYS.add(new LocalDate(2018, 8, 27));
        HOLIDAYS.add(new LocalDate(2018, 12, 25));
        HOLIDAYS.add(new LocalDate(2018, 12, 26));
    }


    /**
     * Returns the singleton instance of the BookingSystem.
     *
     * @return singleton instance of BookingSystem
     */
    public static BookingSystem getInstance() {
        if (instance == null) instance = new BookingSystem();
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

    /**
     * Returns a list of all mechanics.
     *
     * @return list of mechanics
     */
    public List<Mechanic> getAllMechanics() {
        return persistence.getByCriteria(new Criterion<>(Mechanic.class));
    }

    public List<DiagRepBooking> getDayBookings(int offset) {
        long millisInDay = 86400000;
        long rangeStart = new DateTime().withTimeAtStartOfDay().getMillis() + (millisInDay * offset);
        long rangeEnd = rangeStart + millisInDay;

        try {
            List<DiagRepBooking> bookingsOnDay =
                    persistence
                            .getByCriteria(new Criterion<>(
                                    DiagRepBooking.class,
                                    "diagnosisStart", MoreThan, rangeStart)
                                    .and("diagnosisStart", LessThan, rangeEnd));
            bookingsOnDay.addAll(persistence
                            .getByCriteria(new Criterion<>(
                                    DiagRepBooking.class,
                                    "repairStart", MoreThan, rangeStart)
                                    .and("repairStart", LessThan, rangeEnd)));
            return bookingsOnDay;

        }
        catch(CriterionException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public List<DiagRepBooking> searchBookings(String query) {
        try {
            int queryAsInt = Integer.parseInt(query);
            return persistence
                    .getByCriteria(
                            new Criterion<>(DiagRepBooking.class, "bookingID", EqualTo, queryAsInt)
                                    .or("vehicleRegNumber", Regex, query));
        }
        catch(NumberFormatException e) {
            return persistence.getByCriteria(
                    new Criterion<>(
                            DiagRepBooking.class, "vehicleRegNumber", Regex, query));
        }
    }

    public Mechanic getMechanicByID(int mechanicID) {
        List<Mechanic> result =  persistence.getByCriteria(new Criterion<>(Mechanic.class, "mechanicID", EqualTo, mechanicID));
        return result != null ? result.get(0) : null;
    }

    public List<DiagRepBooking> getVBooking(String regNumber) {
        List<DiagRepBooking> Results = persistence.getByCriteria(new Criterion<>(DiagRepBooking.class, "vehicleRegNumber",EqualTo, regNumber));
        return Results !=null ? Results : null;
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
        return isWithinOpenHours(booking)
                && isNotOnHoliday(booking)
                && persistence.commitItem(booking);
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
                && persistence.commitItem(booking);
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
