package persistence.sqlhelper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * @author Marcello De Bernardi
 */
public class DateCellGetter extends CellGetter {
    public Object getObject(ResultSet results, int columnIndex) throws SQLException {
        long val = results.getLong(columnIndex);
        return val == 0 ? null : new Date(val);
    }
}
