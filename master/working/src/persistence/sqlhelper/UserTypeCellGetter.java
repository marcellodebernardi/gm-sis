package persistence.sqlhelper;

import domain.UserType;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Marcello De Bernardi
 */
public class UserTypeCellGetter extends CellGetter {
    public Object getObject(ResultSet results, int columnIndex) throws SQLException {
        return UserType.valueOf(results.getString(columnIndex));
    }
}
