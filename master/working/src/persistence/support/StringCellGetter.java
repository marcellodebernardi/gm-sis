package persistence.support;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Marcello De Bernardi
 */
class StringCellGetter extends CellGetter {
    Object getObject(ResultSet results, int columnIndex) throws SQLException {
        return results.getString(columnIndex);
    }
}
