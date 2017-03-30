package persistence.support;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * @author Marcello De Bernardi
 */
public class ZonedDateTimeCellGetter extends CellGetter {
    Object getObject(ResultSet results, int columnIndex) throws SQLException {
        long val = results.getLong(columnIndex);
        return val == 0 ? null : ZonedDateTime.ofInstant(Instant.ofEpochMilli(val), ZoneId.systemDefault());
    }
}
