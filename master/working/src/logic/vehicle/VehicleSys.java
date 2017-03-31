package logic.vehicle;

import domain.*;
import logic.criterion.Criterion;
import logic.criterion.CriterionRepository;
import persistence.DatabaseRepository;

import java.util.Date;
import java.util.List;

import static logic.criterion.CriterionOperator.*;

/**
 * Created by DillonVaghela on 2/9/17.
 */
public class VehicleSys {

    private static VehicleSys instance;
    private CriterionRepository persistence = DatabaseRepository.getInstance();

    private VehicleSys() {


    }

    public static VehicleSys getInstance() {
        if (instance == null) instance = new VehicleSys();
        return instance;
    }

    public List<Vehicle> getVehiclesList() {
        List<Vehicle> results = persistence.getByCriteria(new Criterion<>(Vehicle.class));
        return results;
    }

    public List<Vehicle> searchVehicleT(String regNumber, String manufacturer, VehicleType vt) {
        List<Vehicle> results = persistence.getByCriteria(new Criterion<>(Vehicle.class,
                "vehicleRegNumber", matches, regNumber).and("manufacturer", matches, manufacturer).and("vehicleType", equalTo, vt));
        return results;

    }


    public List<Vehicle> searchVehicle(String regNumber, String manufacturer) {
        List<Vehicle> results = persistence.getByCriteria(new Criterion<>(Vehicle.class,
                "vehicleRegNumber", matches, regNumber).and("manufacturer", matches, manufacturer));
        return results;

    }

    public List<Vehicle> smartSearchVehicle(String query) {
        return persistence.getByCriteria(new Criterion<>(Vehicle.class)
                .where("vehicleRegNumber", matches, query)
                .or("manufacturer", matches, query)
                .or("model", matches, query)
                .or("customerID", in, new Criterion<>(Customer.class)
                        .where("customerFirstname", matches, query)
                        .or("customerSurname", matches, query)));
    }

    public Vehicle searchAVehicle(String regNumber) {
        List<Vehicle> results = persistence.getByCriteria(new Criterion<>(Vehicle.class,
                "vehicleRegNumber", equalTo, regNumber.toUpperCase()));
        return results != null && results.size() != 0 ? results.get(0) : null;
    }

    public boolean addEditVehicle(String regNumber, int customerID, VehicleType vehicleType, String model, String manufacturer, double engineSize, FuelType fuelType, String colour, int mileage, Date renewalDateMot, Date dateLastServiced, boolean coveredByWarranty, String warrantyName, String warrantyCompAddress, Date warrantyExpirationDate) {
        Vehicle addEdit = new Vehicle(regNumber.trim(), customerID, vehicleType, model, manufacturer, engineSize, fuelType, colour, mileage, renewalDateMot, dateLastServiced, coveredByWarranty, warrantyName, warrantyCompAddress, warrantyExpirationDate, null, null);
        boolean result = persistence.commitItem(addEdit);
        return result;
    }

    public boolean addEditVehicle(Vehicle vehicle) {
        return persistence.commitItem(vehicle);
    }

    public boolean deleteVehicle(String regNumber) {
        persistence.deleteItem(new Criterion<>(VehicleRepair.class, "vehicleRegNumber", equalTo, regNumber));
        return persistence.deleteItem(new Criterion<>(Vehicle.class, "vehicleRegNumber", equalTo, regNumber));
    }


}
