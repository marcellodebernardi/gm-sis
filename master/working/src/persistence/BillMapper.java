package persistence;

import entities.Bill;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Marcello De Bernardi
 * @version 0.1
 * @since 0.1
 */
class BillMapper extends Mapper<Bill> {

    BillMapper(MapperFactory factory) {
        super(factory);
    }


    String toSELECTQuery(List<Bill> bills) {
        String query = SELECTSTRING + Bill.class.getSimpleName() + " WHERE ";

        for(Bill bill : bills) {
            query = query + "(";

            // add WHERE clauses
            if (bill.getBillID() != -1) query = query + "billID = " + bill.getBillID() + " AND ";
            if (bill.getAmount() != -1) query = query + "amount = " + bill.getAmount() + " AND ";
            query = query + "settled = '" + bill.isSettled() + "' AND "; // todo: can't leave settled as null

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

    String toINSERTQuery(Bill bill) {
        return INSERTSTRING
                + Bill.class.getSimpleName() + " VALUES ("
                + bill.getBillID() + ", "
                + bill.getAmount() + ", "
                + bill.isSettled() + ");";
    }

    String toUPDATEQuery(Bill bill) {
        return UPDATESTRING + Bill.class.getSimpleName() + " SET "
                + "amount = " + bill.getAmount() + ", "
                + "settled = '" + bill.isSettled() + "' "
                + "WHERE billID = " + bill.getBillID() + ";";
    }

    String toDELETEQuery(Bill bill) {
        return DELETESTRING
                + Bill.class.getSimpleName() + " WHERE "
                + "billID = " + bill.getBillID() + ";";
    }

    List<Bill> toObjects(ResultSet results) {
        ArrayList<Bill> billList = new ArrayList<>();
        try {
            while (results.next()) {
                billList.add(new Bill(
                        results.getInt(1),    // billID
                        results.getDouble(2), // amount
                        results.getBoolean(3) // settled
                ));
            }
        }
        catch (SQLException e) {
            System.err.print(e.toString());
            return null;
        }
        return billList;
    }
}