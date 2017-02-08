package persistence;

import entities.Bill;
import entities.Booking;
import entities.DiagRepBooking;

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
class DiagRepBookingMapper extends Mapper<DiagRepBooking> {

    /**
     * Constructor for DiagnosisRepairBookingMapper. Takes reference to factory singleton
     * for accessing other mappers.
     *
     * @param factory factory singleton
     */
    DiagRepBookingMapper(MapperFactory factory) {
        super(factory);
    }


    /**
     * Returns SQL selection query for diagnosis and repair bookings based on given criteria
     *
     * @param bookings Criterion objects from which to generate SQL SELECT statement
     * @return SQL SELECT statement for given criteria
     */
    String toSELECTQuery(List<DiagRepBooking> bookings) {
        String query = SELECTSTRING + DiagRepBooking.class.getSimpleName() + " WHERE ";

        for(DiagRepBooking booking: bookings) {
            query = query + "(";

            // add WHERE clauses
            if (booking.getBookingID() != -1) query += "bookingID = " + booking.getBookingID() + " AND ";
            if (booking.getCustomerID() != -1) query +=  "customerID = " + booking.getCustomerID() + " AND ";
            if (booking.getVehicleRegNumber() != null) query += "vehicleRegNumber = '" + booking.getVehicleRegNumber() + "' AND ";
            if (booking.getDescription() != null) query += "description = '" + booking.getDescription() + "' AND ";
            if (booking.getBill() != null) query += "bill = " + booking.getBill().getBillID() + " AND ";
            if (booking.getDiagnosisDate() != null) query += "diagnosisDate = " + booking.getDiagnosisDate().getTime() + " AND ";
            if (booking.getRepairDate() != null) query += "repairDate = " + booking.getRepairDate().getTime() + " AND ";
            if (booking.getRepairEndDate() != null) query += "repairEndDate = " + booking.getRepairEndDate().getTime() + " AND ";

            // remove unnecessary "AND" connective if present
            if (query.substring(query.length() - 4, query.length()).equals("AND "))
                query = query.substring(0, query.length() - 5);
            query = query + ") OR ";
        }

        // todo check for WHERE clause with no conditions

        // remove unnecessary OR logical connective
        query = query.substring(0, query.length()- 4);
        query = query + ";";

        return query;
    }

    /**
     * Returns SQL INSERT query for given item.
     *
     * @param booking booking to insert into database
     * @return SQL INSERT statement for given item
     */
    String toINSERTQuery(DiagRepBooking booking) {
        return null;
    }

    /**
     * Returns an SQL UPDATE statement for given item.
     *
     * @param booking item to update in database
     * @return SQL UPDATE statement for given item
     */
    String toUPDATEQuery(DiagRepBooking booking) {
        return UPDATESTRING + DiagRepBooking.class.getSimpleName() + " SET "
                + "customerID = " + booking.getCustomerID() + ", "
                + "vehicleRegNumber = '" + booking.getVehicleRegNumber() + "', "
                + "description = '" + booking.getDescription() + "', "
                + "bill = " + booking.getBill().getBillID() + ", "
                + "diagnosisDate = " + booking.getDiagnosisDate().getTime() + ", "
                + "repairDate = " + booking.getRepairDate().getTime() + ", "
                + "repairEndDate = " + booking.getRepairEndDate().getTime() + ", "
                + "WHERE bookingID = " + booking.getBookingID() + ";";
    }

    /**
     * Returns an SQL DELETE query for given item.
     *
     * @param booking
     * @return
     */
    String toDELETEQuery(DiagRepBooking booking) {
        return DELETESTRING
                + DiagRepBooking.class.getSimpleName() + " WHERE "
                + "bookingID = " + booking.getBookingID() + ";";
    }

    List<DiagRepBooking> toObjects(ResultSet results) {
        ArrayList<DiagRepBooking> bookingList = new ArrayList<>();
        try {
            while (results.next()) {
                bookingList.add(new DiagRepBooking(
                        results.getInt(1), // bookingID
                        results.getInt(2), // customerID
                        results.getString(3), // vehicleRegNumber
                        results.getString(4), // description
                        (Bill)results.getObject(5), // bill
                        (Date)results.getObject(6), // diagnosisDate
                        (Date)results.getObject(7), // repairDate
                        (Date)results.getObject(8)) // repairEndDate
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
