package persistence.support;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Marcello De Bernardi
 */
class BooleanCellGetter extends CellGetter {
    Object getObject(ResultSet results, int columnIndex) throws SQLException {
        return results.getBoolean(columnIndex);
    }
}
