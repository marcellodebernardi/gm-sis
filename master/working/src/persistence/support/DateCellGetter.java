package persistence.support;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * @author Marcello De Bernardi
 */
class DateCellGetter extends CellGetter {
    Object getObject(ResultSet results, int columnIndex) throws SQLException {
        long val = results.getLong(columnIndex);
        return val == 0 ? null : new Date(val);
    }
}
