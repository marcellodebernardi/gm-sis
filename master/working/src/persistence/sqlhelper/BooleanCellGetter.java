package persistence.sqlhelper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Marcello De Bernardi
 */
public class BooleanCellGetter extends CellGetter {
    public Object getObject(ResultSet results, int columnIndex) throws SQLException {
        return results.getBoolean(columnIndex);
    }
}
