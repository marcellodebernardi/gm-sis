package persistence.support;

import domain.CustomerType;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Marcello De Bernardi
 */
class CustomerTypeCellGetter extends CellGetter {
    Object getObject(ResultSet results, int columnIndex) throws SQLException {
        return CustomerType.valueOf(results.getString(columnIndex));
    }
}
