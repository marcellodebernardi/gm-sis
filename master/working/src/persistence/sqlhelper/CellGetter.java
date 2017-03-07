package persistence.sqlhelper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Marcello De Bernardi
 */
public abstract class CellGetter {
    public abstract Object getObject(ResultSet results, int columnIndex) throws SQLException;
}
