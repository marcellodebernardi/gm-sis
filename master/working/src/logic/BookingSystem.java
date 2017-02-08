package logic;

import entities.DiagRepBooking;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Marcello De Bernardi
 * @version 0.1
 * @since 0.1
 */
public class BookingSystem {

    private static BookingSystem instance;
    private CriterionRepository persistence;


    private BookingSystem(CriterionRepository persistence) {
        this.persistence = persistence;
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
        return persistence.getByCriteria(false, DiagRepBooking.class,
                Collections.singletonList(new DiagRepBooking()));
    }

    public List<DiagRepBooking> getTodayBookings() {
        // todo how to search database by date
        List<DiagRepBooking> criteria = Arrays.asList();
        return persistence.getByCriteria(false, DiagRepBooking.class, criteria);
    }

    /**
     * Adds a new booking to the system.
     *
     * @return true if addition successful, false otherwise
     */
    public boolean addBooking() {
        // todo business logic: prevent bookings on invalid times
        //return persistence.addItem(new DiagRepBooking());
        return false;
    }

    /**
     * Removes a specified booking from the system.
     *
     * @return true if removal successful, false otherwise
     */
    public boolean deleteBooking(int bookingID) {
        // todo business logic: prevent deleting bookings that should not be deleted
        return persistence.deleteItem(DiagRepBooking.class,
                new DiagRepBooking(bookingID, -1, null,
                        null, null, null, null, null));
    }
}
