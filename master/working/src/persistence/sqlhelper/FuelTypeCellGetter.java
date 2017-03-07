package persistence.sqlhelper;

import domain.FuelType;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Marcello De Bernardi
 */
public class FuelTypeCellGetter extends CellGetter {
    public Object getObject(ResultSet results, int columnIndex) throws SQLException {
        return FuelType.valueOf(results.getString(columnIndex));
    }
}
