package logic;

import entities.FuelType;
import entities.Installation;
import entities.Vehicle;
import entities.VehicleType;
import persistence.DatabaseRepository;
import java.util.*;

import static java.util.Collections.emptyList;
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
        ArrayList<Vehicle> arrayList = new ArrayList<Vehicle>();
        return arrayList;
    }

    public Vehicle searchVehicle(String regNumber) {
        List<Vehicle> results = persistence.getByCriteria(new Criterion<>(Vehicle.class,
                "regNumber", EqualTo, regNumber));
        return results.size() == 0 ? null : results.get(0);

    }



    public boolean addVehicle(String regNumber, int customerID, VehicleType vehicleType, String model, String manufacturer, double engineSize, FuelType fuelType, String colour, int mileage, Date renewalDateMot, Date dateLastServiced, boolean coveredByWarranty, String warrantyName, String warrantyCompAddress, Date warrantyExpirationDate)
    {
        Vehicle add = new Vehicle(regNumber,customerID,vehicleType, model,manufacturer,engineSize,fuelType,colour,mileage,renewalDateMot,dateLastServiced,coveredByWarranty,warrantyName,warrantyCompAddress,warrantyExpirationDate, null);
        boolean result = persistence.commitItem(add);
        return result;
    }

    public boolean deleteVehicle(String regNumber){
        return persistence.deleteItem(new Criterion<>(Vehicle.class, "regNumber", EqualTo, regNumber));
    }

    public boolean editVehicle(String regNumber, int customerID, VehicleType vehicleType, String model, String manufacturer, double engineSize, FuelType fuelType, String colour, int mileage, Date renewalDateMot, Date dateLastServiced, boolean coveredByWarranty, String warrantyName, String warrantyCompAddress, Date warrantyExpirationDate){
        //edit Vehicle

        return persistence.commitItem(new Vehicle(regNumber, customerID, vehicleType, model, manufacturer, engineSize, fuelType, colour, mileage, renewalDateMot, dateLastServiced, coveredByWarranty, warrantyName, warrantyCompAddress, warrantyExpirationDate, null ));

    }

    public static VehicleSys getInstance() {
        if (instance == null) instance = new VehicleSys();
        return instance;
    }

}
