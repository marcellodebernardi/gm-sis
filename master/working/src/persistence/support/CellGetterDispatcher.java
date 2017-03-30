package persistence.support;

import domain.CustomerType;
import domain.FuelType;
import domain.UserType;
import domain.VehicleType;
import persistence.CellGetterInterface;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.HashMap;

/**
 * @author Marcello De Bernardi
 */
public class CellGetterDispatcher implements CellGetterInterface {
    private HashMap<String, CellGetter> cellGetterMap;

    public CellGetterDispatcher() {
        cellGetterMap = new HashMap<>();

        cellGetterMap.put(int.class.getSimpleName(), new IntegerCellGetter());
        cellGetterMap.put(Integer.class.getSimpleName(), new IntegerCellGetter());
        cellGetterMap.put(float.class.getSimpleName(), new FloatCellGetter());
        cellGetterMap.put(Float.class.getSimpleName(), new FloatCellGetter());
        cellGetterMap.put(double.class.getSimpleName(), new DoubleCellGetter());
        cellGetterMap.put(Double.class.getSimpleName(), new DoubleCellGetter());
        cellGetterMap.put(boolean.class.getSimpleName(), new BooleanCellGetter());
        cellGetterMap.put(Boolean.class.getSimpleName(), new BooleanCellGetter());
        cellGetterMap.put(String.class.getSimpleName(), new StringCellGetter());
        cellGetterMap.put(UserType.class.getSimpleName(), new UserTypeCellGetter());
        cellGetterMap.put(VehicleType.class.getSimpleName(), new VehicleTypeCellGetter());
        cellGetterMap.put(CustomerType.class.getSimpleName(), new CustomerTypeCellGetter());
        cellGetterMap.put(FuelType.class.getSimpleName(), new FuelTypeCellGetter());
        cellGetterMap.put(Date.class.getSimpleName(), new DateCellGetter());
        cellGetterMap.put(ZonedDateTime.class.getSimpleName(), new ZonedDateTimeCellGetter());
    }


    public Object getObject(Class<?> eClass, ResultSet results, int columnIndex) throws SQLException {
        return cellGetterMap.get(eClass.getSimpleName()).getObject(results, columnIndex);
    }
}
