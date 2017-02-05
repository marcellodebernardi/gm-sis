package persistence;

import entities.Bill;
import entities.Booking;
import entities.DiagnosisRepairBooking;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Marcello De Bernardi
 * @version 0.1
 * @since 0.1
 */
class BookingMapper extends Mapper<Booking> {

    /**
     * Constructor for BookingMapper. Takes reference to factory singleton
     * for accessing other mappers.
     *
     * @param factory factory singleton
     */
    BookingMapper(MapperFactory factory) {
        super(factory);
    }
    // todo implement

    String toSelectQuery(List<Booking> bookings) {
        return null;
    }

    String toInsertQuery(Booking booking) {
        return null;
    }

    String toUpdateQuery(Booking booking) {
        return null;
    }

    String toDeleteQuery(Booking booking) {
        return null;
    }

    List<Booking> toObjects(ResultSet results) {
        ArrayList<Booking> bookingList = new ArrayList<>();
        try {
            while (results.next()) {
                bookingList.add(new DiagnosisRepairBooking(
                        results.getInt(0), // bookingID
                        results.getInt(1), // customerID
                        results.getString(2), // vehicleRegNumber
                        results.getString(3), // description
                        (Bill)results.getObject(4), // bill
                        (Date)results.getObject(5), // diagnosisDate
                        (Date)results.getObject(6), // repairDate
                        (Date)results.getObject(7)) // repairEndDate
                        );
            }
        }
        catch (SQLException e) {
            System.err.print(e.toString());
            return null;
        }
        return bookingList;
    }
}
