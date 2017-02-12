package persistence;

import entities.SpecialistRepairCenter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Marcello De Bernardi
 * @version 0.1
 * @since 0.1
 */
class SRCMapper extends Mapper<SpecialistRepairCenter> {

    SRCMapper(MapperFactory factory) {
        super(factory);
    }

    String toSELECTQuery(List<SpecialistRepairCenter> centers) {
        String query = SELECTSTRING + SpecialistRepairCenter.class.getSimpleName() + " WHERE ";

        for(SpecialistRepairCenter center : centers) {
            query = query + "(";

            // add WHERE clauses
            if (center.getSpcID() != -1)
                query = query + "SpcID = " + center.getSpcID() + " AND ";
            if (center.getName() != null)
                query = query + "name = '" + center.getName() + "' AND ";
            if (center.getAddress() != null)
                query = query + "address = '" + center.getAddress() + "' AND ";
            if (center.getPhone() != null)
                query = query + "phone = '" + center.getPhone() + "' AND ";
            if (center.getEmail() != null)
                query = query + "email = '" + center.getEmail() + "' AND ";

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

    String toINSERTTransaction(SpecialistRepairCenter center) {
        return INSERTSTRING
                + SpecialistRepairCenter.class.getSimpleName() + " VALUES ("
                + center.getSpcID() + ", "
                + "'" + center.getName() + "', "
                + "'" + center.getAddress() + "', "
                + "'" + center.getPhone() + "', "
                + "'" + center.getEmail() + "'); ";
    }

    String toUPDATETransaction(SpecialistRepairCenter center) {
        return UPDATESTRING + SpecialistRepairCenter.class.getSimpleName() + " SET "
                + "name = '" + center.getName() + "', "
                + "address = '" + center.getAddress() + "', "
                + "phone = '" + center.getPhone() + "', "
                + "email = '" + center.getEmail() + "' "
                + "WHERE spcID = " + center.getSpcID() + ";";
    }

    String toDELETETransaction(SpecialistRepairCenter center) {
        // todo what if Center object has no spcID? Then primary key is missing
        return DELETESTRING
                + SpecialistRepairCenter.class.getSimpleName() + " WHERE "
                + "spcID = " + center.getSpcID() + ";";
    }

    List<SpecialistRepairCenter> toObjects(ResultSet results) {
        ArrayList<SpecialistRepairCenter> SPCList = new ArrayList<>();
        try {
            while (results.next()) {
                SPCList.add(new SpecialistRepairCenter(
                        results.getInt(1),      // spcID
                        results.getString(2),   // name
                        results.getString(3),   // address
                        results.getString(4),   // phone
                        results.getString(5))); // email
            }
        }
        catch (SQLException e) {
            System.err.print(e.getMessage());
            return null;
        }
        return SPCList;
    }
}
