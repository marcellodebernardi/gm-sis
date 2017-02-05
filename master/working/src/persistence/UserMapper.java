package persistence;

import entities.User;
import entities.UserType;

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


    String toSelectQuery(List<User> users) {
        String query = SELECTSTRING + users.get(0).getClass().getName() + " WHERE ";

        for(User user : users) {
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
            if (user.getUserType() != UserType.NULL)
                query = query + "userType = '" + user.getUserType() + "' AND ";

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

    String toInsertQuery(User user) {
        String query = INSERTSTRING;

        // todo implement

        return query;
    }

    String toUpdateQuery(User user) {
        String query = UPDATESTRING;

        // todo implement

        return query;
    }

    String toDeleteQuery(User user) {
        String query = DELETESTRING;

        // todo implement

        return query;
    }

    List<User> toObjects(ResultSet results) {
        ArrayList<User> userList = new ArrayList<>();
        try {
            while (results.next()) {
                userList.add(new User(
                        results.getString(0), // userID
                        results.getString(1), // password
                        results.getString(2), // firstName
                        results.getString(3), // surname
                        (results.getString(4).equals(UserType.ADMINISTRATOR.toString())
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
