package persistence;

import entities.User;

import java.sql.ResultSet;
import java.util.List;

/**
 * @author Marcello De Bernardi
 * @version 0.1
 * @since 0.1
 */
class UserMapper extends Mapper<User> {
    // todo implement

    String toSelectQuery(List<User> users) {
        String query = SELECTSTRING + users.get(0).getClass().getName() + " ";



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
        return null;
    }
}
