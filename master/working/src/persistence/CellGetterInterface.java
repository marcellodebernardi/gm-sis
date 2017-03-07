package persistence;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Marcello De Bernardi
 */
public interface CellGetterInterface {
    Object getObject(Class<?> eClass, ResultSet results, int columnIndex) throws SQLException;
}
