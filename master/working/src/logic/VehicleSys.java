package logic;

import domain.FuelType;
import domain.Vehicle;
import domain.VehicleType;
import persistence.DatabaseRepository;
import java.util.*;

import static logic.CriterionOperator.*;

/**
 * Created by DillonVaghela on 2/9/17.
 */
public class VehicleSys {

    private static VehicleSys instance;
    private CriterionRepository persistence = DatabaseRepository.getInstance();
    private Vehicle vehicle;

    private VehicleSys() {


    }

    public List<Vehicle> getVehiclesList(){
        List<Vehicle> results = persistence.getByCriteria(new Criterion<>(Vehicle.class));
        return results;
    }

    public List<Vehicle> searchVehicle(String regNumber, int customerID, VehicleType vehicleType, String model, String manufacturer, double engineSize, FuelType fuelType, String colour, int mileage, Date renewalDateMot, Date dateLastServiced, boolean coveredByWarranty, String warrantyName, String warrantyCompAddress, Date warrantyExpirationDate) {
        List<Vehicle> results = persistence.getByCriteria(new Criterion<>(Vehicle.class,
                "regNumber", EqualTo, regNumber).and("customerID", EqualTo, customerID).and("vehicleType", EqualTo, vehicleType)
                .and("model",EqualTo,model).and("manufacturer",EqualTo, manufacturer).and("engineSize", EqualTo, engineSize).and("fuelType",
                        EqualTo, fuelType).and("colour",EqualTo,colour).and("mileage",EqualTo,mileage).and("renewalDateMot",EqualTo,renewalDateMot)
                .and("dateLastServiced",EqualTo,dateLastServiced).and("coveredByWarranty",EqualTo, coveredByWarranty).and("warrantyName",EqualTo,
                        warrantyName).and("warrantyCompAddress",EqualTo, warrantyCompAddress).and("warrantyExpirationDate",EqualTo,warrantyExpirationDate));
        return results;

    }

    public Vehicle searchAVehicle(String regNumber)
    {
        List<Vehicle> results = persistence.getByCriteria(new Criterion<>(Vehicle.class,
                "regNumber", EqualTo, regNumber));
        return results !=null ? results.get(0) : null;
    }

    public boolean addEditVehicle(String regNumber, int customerID, VehicleType vehicleType, String model, String manufacturer, double engineSize, FuelType fuelType, String colour, int mileage, Date renewalDateMot, Date dateLastServiced, boolean coveredByWarranty, String warrantyName, String warrantyCompAddress, Date warrantyExpirationDate)
    {
        Vehicle addEdit = new Vehicle(regNumber,customerID,vehicleType, model,manufacturer,engineSize,fuelType,colour,mileage,renewalDateMot,dateLastServiced,coveredByWarranty,warrantyName,warrantyCompAddress,warrantyExpirationDate, null);
        boolean result = persistence.commitItem(addEdit);
        return result;
    }

    public boolean deleteVehicle(String regNumber){
        return persistence.deleteItem(new Criterion<>(Vehicle.class, "regNumber", EqualTo, regNumber));
    }


    public static VehicleSys getInstance() {
        if (instance == null) instance = new VehicleSys();
        return instance;
    }

}
