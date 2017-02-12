package persistence;

import entities.UserType;
import entities.Vehicle;
import entities.VehicleType;
import entities.FuelType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Marcello De Bernardi, Dillon Vaghela
 * @version 0.1
 * @since 0.1
 */


class VehicleMapper extends Mapper<Vehicle> {

/**
     * Constructor for UserMapper. Takes reference to factory singleton.
     *
     * @param factory MapperFactory for accessing other mappers
 *                     */

    VehicleMapper(MapperFactory factory) {
        super(factory);
    }


    String toSELECTQuery(List<Vehicle> vehicles) {
        String query = SELECTSTRING + Vehicle.class.getName().substring(9) + " WHERE ";

        for(Vehicle vehicle : vehicles) {
            query = query + "(";

            // add WHERE clauses
            if (vehicle.getRegNumber() != null)
                query = query + "regNumber = '" + vehicle.getRegNumber() + "' AND ";
            if (vehicle.getCustomerID() != 0)
                query = query + "customerID = '" + vehicle.getCustomerID() + "' AND ";
            if (vehicle.getVehicleType() != null)
                query = query + "vehicleType = '" + vehicle.getVehicleType() + "' AND ";
            if (vehicle.getModel() != null)
                query = query + "model = '" + vehicle.getModel() + "' AND ";
            if (vehicle.getManufacturer() != null)
                query = query + "manufacturer = '" + vehicle.getManufacturer() + "' AND ";
            if (vehicle.getEngineSize() != 0)
                query = query + "engineSize = '" + vehicle.getEngineSize() + "' AND ";
            if (vehicle.getFuelType() != null)
                query = query + "fuelType = '" + vehicle.getFuelType() + "' AND ";
            if (vehicle.getColour() != null)
                query = query + "colour = '" + vehicle.getColour() + "' AND ";
            if (vehicle.getMileage() != 0)
                query = query + "mileage = '" + vehicle.getMileage() + "' AND ";
            if (vehicle.getRenewalDateMot() != null)
                query = query + "renewalDateMot = '" + vehicle.getRenewalDateMot() + "' AND ";
            if (vehicle.getDateLastServiced() != null)
                query = query + "dateLastServiced = '" + vehicle.getDateLastServiced() + "' AND ";
            if (vehicle.isCoveredByWarranty() != false)
                query = query + "coveredByWarranty = '" + vehicle.isCoveredByWarranty() + "' AND ";
            if (vehicle.getWarrantyName() != null)
                query = query + "warrantyName = '" + vehicle.getWarrantyName() + "' AND ";
            if (vehicle.getWarrantyCompAddress() != null)
                query = query + "warrantyCompName = '" + vehicle.getWarrantyCompAddress() + "' AND ";
            if (vehicle.getWarrantyExpirationDate() != null)
                query = query + "warrantyExpirationDate = '" + vehicle.getWarrantyExpirationDate() + "' AND ";

            // remove unnecessary "AND" connective if present
            if (query.substring(query.length() - 4, query.length()).equals("AND "))
                query = query.substring(0, query.length() - 5);
            query = query + ") OR ";
        }

        // todo check for WHERE clause with no conditions

        // remove unnecessary OR logical connective
        query = query.substring(0, query.length()- 4);
        query = query + ";";

        return query;
    }

    String toINSERTQuery(Vehicle vehicle) {
        return INSERTSTRING
                + Vehicle.class.getSimpleName() + " VALUES ("
                + "'" + vehicle.getRegNumber() + "', "
                + "'" + vehicle.getCustomerID() + "', "
                + "'" + vehicle.getVehicleType().toString() + "', "
                + "'" + vehicle.getModel() + "', "
                + "'" + vehicle.getManufacturer() + "', "
                + "'" + vehicle.getEngineSize() + "', "
                + "'" + vehicle.getFuelType().toString() + "', "
                + "'" + vehicle.getColour() + "', "
                + "'" + vehicle.getMileage() + "', "
                + "'" + vehicle.getRenewalDateMot() + "', "
                + "'" + vehicle.getDateLastServiced() + "', "
                + "'" + vehicle.isCoveredByWarranty() + "', "
                + "'" + vehicle.getWarrantyName() + "', "
                + "'" + vehicle.getWarrantyCompAddress() + "', "
                + "'" + vehicle.getWarrantyExpirationDate()
                + "');";
    }

    String toUPDATEQuery(Vehicle vehicle) {
        return UPDATESTRING
                + Vehicle.class.getSimpleName() + " SET "
                + "customerID = '" + vehicle.getCustomerID() + "', "
                + "vehicleType = '" + vehicle.getVehicleType().toString() + "', "
                + "model = '" + vehicle.getModel() + "', "
                + "manufacturer = '" + vehicle.getManufacturer() + "' "
                + "engineSize = '" + vehicle.getEngineSize() + "' "
                + "fuelType = '" + vehicle.getFuelType().toString() + "' "
                + "colour = '" + vehicle.getColour() + "' "
                + "mileage = '" + vehicle.getMileage() + "' "
                + "renewalDateMot = '" + vehicle.getRenewalDateMot() + "' "
                + "dateLastServiced = '" + vehicle.getDateLastServiced() + "' "
                + "coveredByWarranty = '" + vehicle.isCoveredByWarranty() + "' "
                + "warrantyName = '" + vehicle.getWarrantyName() + "' "
                + "warrantyCompAddress = '" + vehicle.getWarrantyCompAddress() + "' "
                + "warrantyExpirationDate = '" + vehicle.getWarrantyExpirationDate() + "' "
                + "WHERE regNumber = '" + vehicle.getRegNumber() + "';";
    }

    String toDELETEQuery(Vehicle vehicle) {
        // todo what if User object has no userID? Then primary key is missing
        return DELETESTRING
                + Vehicle.class.getSimpleName() + " WHERE "
                + "regNumber = '" + vehicle.getRegNumber() + "';";
    }

    List<Vehicle> toObjects(ResultSet results) {
        ArrayList<Vehicle> vehiclesList = new ArrayList<>();
        try {
            while (results.next()) {
                vehiclesList.add(new Vehicle(
                        results.getString(1), // regNumber
                        results.getInt(2), // customerID
                        (results.getString(3).equals(VehicleType.Car.toString()) ? VehicleType.Car :(results.getString(3).equals(VehicleType.Van.toString()) ?  VehicleType.Van : VehicleType.Truck)), // vehicleType
                        results.getString(4), // model
                        results.getString(5), // manufacturer
                        results.getInt(6), // engineSize
                        (results.getString(7).equals(FuelType.diesel.toString()) ? FuelType.diesel : FuelType.petrol), // fuelType
                        results.getString(8), // colour
                        results.getInt(9), // mileage
                        results.getDate(10), // renewalDateMot
                        results.getDate(11), // dateLastServiced
                        results.getBoolean(12), // coveredByWarranty
                        results.getString(13), // warrantyName
                        results.getString(14), // warrantyCompAddress
                        results.getDate(15) // warrantyExpirationDate
                        ));
            }
        }
        catch (SQLException e) {
            System.err.print(e.toString());
            return null;
        }
        return vehiclesList;
    }
}

