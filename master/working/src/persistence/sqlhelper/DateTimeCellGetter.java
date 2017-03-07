package persistence.sqlhelper;

import org.joda.time.DateTime;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Marcello De Bernardi
 */
public class DateTimeCellGetter extends CellGetter {
    public Object getObject(ResultSet results, int columnIndex) throws SQLException {
        long val = results.getLong(columnIndex);
        return val == 0 ? null : new DateTime(val);
    }
}
