package logic;

import entities.DiagnosisRepairBooking;

import java.util.ArrayList;
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
    public BookingSystem getInstance(CriterionRepository persistence) {
        if (instance == null) instance = new BookingSystem(persistence);
        return instance;
    }

    /**
     * Returns a list containing all bookings for diagnosis and repair.
     *
     * @return List of diagnosis and repair bookings
     */
    public List<DiagnosisRepairBooking> getBookingList() {
        // List<Criterion> tempList = persistence.getByCriteria(false, new DiagnosisRepairBooking());
        List<DiagnosisRepairBooking> finalList = new ArrayList<DiagnosisRepairBooking>();

        // for (int i = 0; i < tempList.size(); i++) {
           // finalList.set(i, (DiagnosisRepairBooking)tempList.get(i));
        //}

        return finalList;
    }

    /**
     * Adds a new booking to the system.
     *
     * @return true if addition successful, false otherwise
     */
    public boolean addBooking() {
        //return persistence.addItem(new DiagnosisRepairBooking());
        return false;
    }

    /**
     * Removes a specified booking from the system.
     *
     * @return true if removal successful, false otherwise
     */
    public boolean deleteBooking(String bookingID) {
        // TODO: bookingID in booking constructor
        // return persistence.deleteByCriteria(false, new DiagnosisRepairBooking());
        return false;
    }
}
