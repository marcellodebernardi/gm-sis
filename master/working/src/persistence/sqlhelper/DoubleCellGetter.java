package persistence.sqlhelper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Marcello De Bernardi
 */
public class DoubleCellGetter extends CellGetter {
    public Object getObject(ResultSet results, int columnIndex) throws SQLException {
        return results.getDouble(columnIndex);
    }
}
