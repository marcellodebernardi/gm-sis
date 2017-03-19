package logic.booking;

import domain.DiagRepBooking;
import domain.Mechanic;
import logic.criterion.Criterion;
import logic.criterion.CriterionRepository;
import persistence.DatabaseRepository;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static logic.criterion.CriterionOperator.*;

/**
 * @author Marcello De Bernardi
 * @version 0.1
 * @since 0.1
 */
public class BookingSystem {
    private static BookingSystem instance;
    private CriterionRepository persistence;


    private BookingSystem() {
        this.persistence = DatabaseRepository.getInstance();
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
     * Returns a list containing all diagnosis and repair bookings for which either the diagnosis
     * or the repair is yet to take place.
     *
     * @return list of future bookings
     */
    public List<DiagRepBooking> getFutureBookings() {
        ZonedDateTime now = ZonedDateTime.now();

        return persistence.getByCriteria(new Criterion<>(DiagRepBooking.class,
                "diagnosisStart", MoreThan, now)
                .or("repairStart", MoreThan, now)
        );
    }

    /**
     * Returns a list containing all diagnosis and repair bookings for which either the diagnosis
     * or the repair has already taken place.
     *
     * @return list of past bookings
     */
    public List<DiagRepBooking> getPastBookings() {
        ZonedDateTime now = ZonedDateTime.now();

        return persistence.getByCriteria(new Criterion<>(DiagRepBooking.class,
                "diagnosisStart", LessThan, now)
                .or("repairStart", LessThan, now)
        );
    }

    /**
     * Returns all diagnosis and repair bookings such that the booking has either a diagnosis
     * appointment or a repair appointment inside the specified time range.
     *
     * @param start start time of time range
     * @param end end time of time range
     * @return list of bookings in range
     */
    public List<DiagRepBooking> getBookingsBetween(ZonedDateTime start, ZonedDateTime end) {
        return persistence.getByCriteria(new Criterion<>(DiagRepBooking.class,
                "diagnosisStart", MoreThan, start)
                .and("diagnosisStart", LessThan, end)
                .or("repairStart", MoreThan, start)
                .and("repairStart", LessThan, end)
        );
    }

    /**
     * Returns a list of all mechanics.
     *
     * @return list of mechanics
     */
    public List<Mechanic> getAllMechanics() {
        return persistence.getByCriteria(new Criterion<>(Mechanic.class));
    }

    /**
     * Returns a list of all bookings matching the query in one of several ways:
     *
     * 1. the query is the bookingID of a booking
     * 2. the query can be pattern-matched to the vehicle registration number
     * 3. the query can be pattern-matched to the customer name todo
     * 4. the query can be pattern-matched to the mechanic name todo
     *
     * @param query query
     * @return list of bookings, may be empty
     */
    public List<DiagRepBooking> searchBookings(String query) {
        if (query == null) throw new NullPointerException();

        return persistence.getByCriteria(
                new Criterion<>(DiagRepBooking.class, "vehicleRegNumber", Regex, query)
        );
    }

    /**
     * Returns a booking as identified by its booking ID.
     * @param bookingID ID of booking
     * @return booking, may be null
     */
    public DiagRepBooking getBookingByID(int bookingID) {
        List<DiagRepBooking> result = persistence.getByCriteria(
                new Criterion<>(DiagRepBooking.class, "bookingID", EqualTo, bookingID)
        );

        return result != null && result.size() != 0 ? result.get(0) : null;
    }

    /**
     * Returns a mechanic as identified by their ID.
     * @param mechanicID ID of mechanic
     * @return mechanic, may be null
     */
    public Mechanic getMechanicByID(int mechanicID) {
        List<Mechanic> result = persistence.getByCriteria(
                new Criterion<>(Mechanic.class, "mechanicID", EqualTo, mechanicID));
        return result != null ? result.get(0) : null;
    }

    /**
     * Returns all bookings, including past and future, of a particular vehicle.
     *
     * @param regNumber the registration number of the vehicle
     * @return list of bookings for vehicle
     */
    public List<DiagRepBooking> getVehicleBookings(String regNumber) {
        return persistence.getByCriteria(
                new Criterion<>(DiagRepBooking.class, "vehicleRegNumber", EqualTo, regNumber));
    }

    /**
     * <p>
     * Adds a new booking to the persistence layer. Assumes an existing customer and
     * vehicle.
     * </p>
     *
     * @return true if addition successful, false otherwise
     */
    public boolean commitBooking(DiagRepBooking booking) {
        // if is not closed, not on holiday and does not clash, commit and return result
        return !isClosed(booking) && !isHoliday(booking) && !clashes(booking) && persistence.commitItem(booking);
    }

    /**
     * Removes a specified booking from the system.
     *
     * @return true if removal successful, false otherwise
     */
    public boolean deleteBookingByID(int bookingID) {
        return persistence.deleteItem(
                new Criterion<>(DiagRepBooking.class, "bookingID", EqualTo, bookingID));
    }


    /* Checks time validity in terms of opening and closing hours as well as weekdays. */
    private boolean isClosed(DiagRepBooking booking) {
        /* return (!(booking.getDiagnosisInterval().getStart().toLocalTime().compareTo(OPENING_HOUR) < 0
                || booking.getDiagnosisInterval().getEnd().toLocalTime().compareTo(CLOSING_HOUR) > 0
                || booking.getRepairInterval().getStart().toLocalTime().compareTo(OPENING_HOUR) < 0
                || booking.getRepairInterval().getEnd().toLocalTime().compareTo(CLOSING_HOUR) > 0
                || booking.getDiagnosisInterval().getStart().toLocalDate().getDayOfWeek() < 1
                || booking.getDiagnosisInterval().getStart().toLocalDate().getDayOfWeek() > 5
                || booking.getRepairInterval().getStart().toLocalDate().getDayOfWeek() > 5
                || booking.getRepairInterval().getStart().toLocalDate().getDayOfWeek() > 5)); */
        return true;
    }

    /* Checks the booking is not for a bank or public holiday */
    private boolean isHoliday(DiagRepBooking booking) {
        /*return (!(HOLIDAYS.contains(booking.getDiagnosisInterval().getStart().toLocalDate())
                || HOLIDAYS.contains(booking.getRepairInterval().getStart().toLocalDate())));*/
        return true;
    }

    /* HELPER: Checks that the booking does not clash temporally with other bookings */
    private boolean clashes(DiagRepBooking booking) {
        List<ZonedDateTime> times = new ArrayList<>();
        times.add(booking.getDiagnosisStart());
        times.add(booking.getDiagnosisEnd());
        times.add(booking.getRepairStart());
        times.add(booking.getRepairEnd());

        for (ZonedDateTime time : times) {
            Criterion<DiagRepBooking> expression1
                    = new Criterion<>(DiagRepBooking.class, "mechanicID", EqualTo, booking.getMechanicID())
                    .and("diagnosisStart", LessThan, time)
                    .and("diagnosisEnd", MoreThan, time)
                    .or("mechanicID", EqualTo, booking.getMechanicID())
                    .and("repairEnd", MoreThan, time)
                    .and("repairStart", LessThan, time);

            if (persistence.getByCriteria(expression1).size() != 0) return true;
        }
        return false;
    }
}
