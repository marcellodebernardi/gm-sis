package persistence;

import entities.User;
import entities.UserType;
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
class UserMapper extends Mapper<User> {
    /**
     * Constructor for UserMapper. Takes reference to factory singleton.
     *
     * @param factory MapperFactory for accessing other mappers
     */
    UserMapper(MapperFactory factory) {
        super(factory);
    }


    String toSELECTQuery(Criterion<User> criteria) {
        String query = SELECTSTRING + User.class.getName().substring(9) + " WHERE ";

        // todo rewrite
        /* for(User user : criteria) {
            query = query + "(";

            // add WHERE clauses
            if (user.getUserID() != null)
                query = query + "userID = '" + user.getUserID() + "' AND ";
            if (user.getPassword() != null)
                query = query + "password = '" + user.getPassword() + "' AND ";
            if (user.getFirstName() != null)
                query = query + "firstName = '" + user.getFirstName() + "' AND ";
            if (user.getSurname() != null)
                query = query + "surname = '" + user.getSurname() + "' AND ";
            if (user.getUserType() != null)
                query = query + "userType = '" + user.getUserType() + "' AND ";

            // remove unnecessary "AND" connective if present
            if (query.substring(query.length() - 4, query.length()).equals("AND "))
                query = query.substring(0, query.length() - 5);
            query = query + ") OR ";
        } */

        // todo check for WHERE clause with no conditions

        // remove unnecessary OR logical connective
        query = query.substring(0, query.length()- 4);
        query = query + ";";

        return query;
    }

    String toINSERTTransaction(User user) {
        return INSERTSTRING
                + User.class.getSimpleName() + " VALUES ("
                + "'" + user.getUserID() + "', "
                + "'" + user.getPassword() + "', "
                + "'" + user.getFirstName() + "', "
                + "'" + user.getSurname() + "', "
                + "'" + user.getUserType().toString() + "');";
    }

    String toUPDATETransaction(User user) {
        return UPDATESTRING + User.class.getSimpleName() + " SET "
                + "password = '" + user.getPassword() + "', "
                + "firstName = '" + user.getFirstName() + "', "
                + "surname = '" + user.getSurname() + "', "
                + "userType = '" + user.getUserType().toString() + "' "
                + "WHERE userID = '" + user.getUserID() + "';";
    }

    String toDELETETransaction(User user) {
        // todo what if User object has no userID? Then primary key is missing
        return DELETESTRING
                + User.class.getSimpleName() + " WHERE "
                + "userID = '" + user.getUserID() + "';";
    }

    List<User> toObjects(ResultSet results) {
        ArrayList<User> userList = new ArrayList<>();
        try {
            while (results.next()) {
                userList.add(new User(
                        results.getString(1), // userID
                        results.getString(2), // password
                        results.getString(3), // firstName
                        results.getString(4), // surname
                        (results.getString(5).equals(UserType.ADMINISTRATOR.toString())
                                ? UserType.ADMINISTRATOR : UserType.NORMAL)));
            }
        }
        catch (SQLException e) {
            System.err.print(e.toString());
            return null;
        }
        return userList;
    }
}
