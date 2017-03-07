package persistence.sqlhelper;

import domain.CustomerType;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Marcello De Bernardi
 */
public class CustomerTypeCellGetter extends CellGetter {
    public Object getObject(ResultSet results, int columnIndex) throws SQLException {
        return CustomerType.valueOf(results.getString(columnIndex));
    }
}
