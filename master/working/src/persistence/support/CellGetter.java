package persistence.support;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Marcello De Bernardi
 */
abstract class CellGetter {
    abstract Object getObject(ResultSet results, int columnIndex) throws SQLException;
}
