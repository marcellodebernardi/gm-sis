package logic.vehicle;

import domain.FuelType;
import domain.Vehicle;
import domain.VehicleType;
import logic.criterion.Criterion;
import logic.criterion.CriterionRepository;
import persistence.DatabaseRepository;
import java.util.*;

import static logic.criterion.CriterionOperator.*;

/**
 * Created by DillonVaghela on 2/9/17.
 */
public class VehicleSys {

    private static VehicleSys instance;
    private CriterionRepository persistence = DatabaseRepository.getInstance();

    private VehicleSys() {


    }

    public List<Vehicle> getVehiclesList(){
        List<Vehicle> results = persistence.getByCriteria(new Criterion<>(Vehicle.class));
        return results;
    }

    public List<Vehicle> searchVehicleT(String regNumber, String manufacturer, VehicleType vt) {
        List<Vehicle> results = persistence.getByCriteria(new Criterion<>(Vehicle.class,
                "regNumber", Regex, regNumber).and("manufacturer",Regex, manufacturer).and("vehicleType", EqualTo, vt));
        return results;

    }

    public List<Vehicle> searchByTemplate(String model, String manufacturer, Double engineSize, VehicleType vt, FuelType ft) {
        List<Vehicle> results = persistence.getByCriteria(new Criterion<>(Vehicle.class,
                "model", Regex, model).and("manufacturer",Regex, manufacturer).and("engineSize",EqualTo,engineSize).and("vehicleType", EqualTo, vt).and("fuelType", EqualTo, ft));
        return results;

    }

    public List<Vehicle> searchVehicle(String regNumber, String manufacturer) {
        List<Vehicle> results = persistence.getByCriteria(new Criterion<>(Vehicle.class,
                "regNumber", Regex, regNumber).and("manufacturer",Regex, manufacturer));
        return results;

    }

    public Vehicle searchAVehicle(String regNumber)
    {
        List<Vehicle> results = persistence.getByCriteria(new Criterion<>(Vehicle.class,
                "regNumber", EqualTo, regNumber));
        return results !=null && results.size() != 0 ? results.get(0) : null;
    }

    public boolean addEditVehicle(String regNumber, int customerID, VehicleType vehicleType, String model, String manufacturer, double engineSize, FuelType fuelType, String colour, int mileage, Date renewalDateMot, Date dateLastServiced, boolean coveredByWarranty, String warrantyName, String warrantyCompAddress, Date warrantyExpirationDate)
    {
        Vehicle addEdit = new Vehicle(regNumber,customerID,vehicleType, model,manufacturer,engineSize,fuelType,colour,mileage,renewalDateMot,dateLastServiced,coveredByWarranty,warrantyName,warrantyCompAddress,warrantyExpirationDate, null, null);
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

    public boolean VehicleExists(String reg)
    {
        if(!persistence.getByCriteria(new Criterion<>(Vehicle.class, "regNumber", EqualTo,reg)).isEmpty())
            return true;
        return false;
    }

}
