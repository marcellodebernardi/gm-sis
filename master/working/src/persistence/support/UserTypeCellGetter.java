package persistence.support;

import domain.UserType;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Marcello De Bernardi
 */
class UserTypeCellGetter extends CellGetter {
    Object getObject(ResultSet results, int columnIndex) throws SQLException {
        return UserType.valueOf(results.getString(columnIndex));
    }
}
