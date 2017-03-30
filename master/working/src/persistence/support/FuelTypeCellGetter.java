package persistence.support;

import domain.FuelType;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Marcello De Bernardi
 */
class FuelTypeCellGetter extends CellGetter {
    Object getObject(ResultSet results, int columnIndex) throws SQLException {
        return FuelType.valueOf(results.getString(columnIndex));
    }
}
