package persistence;

import entities.DiagRepBooking;
import logic.Criterion;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

    // todo change everything to question mark notation

    /**
     * Returns SQL selection query for diagnosis and repair bookings based on given criteria
     *
     * @param criteria Criterion objects from which to generate SQL SELECT statement
     * @return SQL SELECT statement for given criteria
     */
    String toSELECTQuery(Criterion<DiagRepBooking> criteria) {
        String query = SELECTSTRING + DiagRepBooking.class.getSimpleName() + " WHERE ";

        // todo rewrite
        /* for (DiagRepBooking booking : criteria) {
            query = query + "(";

            // add WHERE clauses
            if (booking.getBookingID() != -1)
                query += "bookingID = " + booking.getBookingID() + " AND ";
            if (booking.getCustomerID() != -1)
                query += "customerID = " + booking.getCustomerID() + " AND ";
            if (booking.getVehicleRegNumber() != null)
                query += "vehicleRegNumber = '" + booking.getVehicleRegNumber() + "' AND ";
            if (booking.getDescription() != null)
                query += "description = '" + booking.getDescription() + "' AND ";
            if (booking.getBill() != null)
                query += "bill = '" + booking.getBill().getBillID() + "' AND ";
            if (booking.getDiagnosisInterval() != null)
                query += "diagnosisIntervalStart = " + booking.getDiagnosisInterval().getStartMillis() + " AND ";
            else if (booking.getDiagnosisDate() != null)
                query += "diagnosisDate = '" + booking.getDiagnosisDate().toString() + " AND ";
            if (booking.getRepairInterval() != null)
                query += "repairIntervalStart = " + booking.getRepairInterval().getStartMillis() + " AND ";
            else if (booking.getRepairDate() != null)
                query += "repairDate = '" + booking.getRepairDate().toString() + " AND ";
            if (booking.getSpecRepBooking() != null)
                query += "specRepairBooking = " + booking.getSpecRepBooking().getSpcRepID() + " AND ";

            // remove unnecessary "AND" connective if present
            if (query.substring(query.length() - 4, query.length()).equals("AND "))
                query = query.substring(0, query.length() - 5);
            query = query + ") OR ";
        } */

        // todo check for WHERE clause with no conditions
        // remove unnecessary OR logical connective
        query = query.substring(0, query.length() - 4);
        query = query + ";";

        return query;
    }

    /**
     * Returns SQL INSERT query for given item.
     *
     * @param booking booking to insert into database
     * @return SQL INSERT statement for given item
     */
    String toINSERTTransaction(DiagRepBooking booking) {
        return INSERTSTRING
                + DiagRepBooking.class.getSimpleName() + " VALUES ("
                + "" + booking.getBookingID() + ", "
                + "" + booking.getCustomerID() + ", "
                + "'" + booking.getVehicleRegNumber() + "', "
                + "'" + booking.getDescription() + "', "
        //        + "" + booking.getBill().getBillID() + ", "
                + "" + booking.getDiagnosisInterval().getStartMillis() + ", "
                + "" + booking.getDiagnosisInterval().getEndMillis() + ", "
                + "" + booking.getRepairInterval().getStartMillis() + ", "
                + "" + booking.getRepairInterval().getEndMillis() + ", "
        //        + "'" + booking.getDiagnosisDate().toString() + "', "
        //        + "'" + booking.getRepairDate().toString() + "', "
                + "" + booking.getSpecRepBooking().getSpcRepID() + ");";
        // todo parts list
        // todo additional transaction parts
    }

    /**
     * Returns an SQL UPDATE statement for given item.
     *
     * @param booking item to update in database
     * @return SQL UPDATE statement for given item
     */
    String toUPDATETransaction(DiagRepBooking booking) {
        return UPDATESTRING + DiagRepBooking.class.getSimpleName() + " SET "
                + "customerID = " + booking.getCustomerID() + ", "
                + "vehicleRegNumber = '" + booking.getVehicleRegNumber() + "', "
                + "description = '" + booking.getDescription() + "', "
                //+ "bill = " + booking.getBill().getBillID() + ", "
                //+ "diagnosisDate = " + booking.getDiagnosisDate().getTime() + ", "
                //+ "repairDate = " + booking.getRepairDate().getTime() + ", "
                //+ "repairEndDate = " + booking.getRepairEndDate().getTime() + ", "
                + "WHERE bookingID = " + booking.getBookingID() + ";";
    }

    /**
     * Returns an SQL DELETE query for given item.
     *
     * @param criteria
     * @return
     */
    String toDELETETransaction(Criterion<DiagRepBooking> criteria) {
        return DELETESTRING
                + DiagRepBooking.class.getSimpleName() + " WHERE "
                + "bookingID = " + criteria + ";";
    }

    List<DiagRepBooking> toObjects(ResultSet results) {
        ArrayList<DiagRepBooking> bookingList = new ArrayList<>();
        try {
            while (results.next()) {
                // todo implement
            }
        } catch (SQLException e) {
            System.err.print(e.toString());
            return null;
        }
        return bookingList;
    }
}
