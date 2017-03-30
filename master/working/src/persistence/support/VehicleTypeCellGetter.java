package persistence.support;

import domain.VehicleType;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Marcello De Bernardi
 */
class VehicleTypeCellGetter extends CellGetter {
    Object getObject(ResultSet results, int columnIndex) throws SQLException {
        return VehicleType.valueOf(results.getString(columnIndex));
    }
}
