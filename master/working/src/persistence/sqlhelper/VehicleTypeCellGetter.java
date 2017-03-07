package persistence.sqlhelper;

import domain.VehicleType;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Marcello De Bernardi
 */
public class VehicleTypeCellGetter extends CellGetter {
    public Object getObject(ResultSet results, int columnIndex) throws SQLException {
        return VehicleType.valueOf(results.getString(columnIndex));
    }
}
