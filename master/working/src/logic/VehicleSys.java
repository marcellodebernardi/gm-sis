package logic;

import entities.FuelType;
import entities.Vehicle;
import entities.VehicleType;
import persistence.DatabaseRepository;
import java.util.*;

/**
 * Created by DillonVaghela on 2/9/17.
 */
public class VehicleSys {

    private static VehicleSys instance;
    private CriterionRepository persistence;

    private VehicleSys(CriterionRepository persistence) {
        this.persistence = persistence;

    }

    public List<Vehicle> getVehiclesList(){
        ArrayList<Vehicle> arrayList = new ArrayList<Vehicle>();
        return arrayList;
    }

    public Vehicle searchVehicle(String regNumber) {


        Vehicle vehicleFound = persistence.getByCriteria(false, Vehicle.class,
                Collections.singletonList(new Vehicle(regNumber, 0, null, null, null, 0,  null, null, 0,null,null, false, null, null, null)))
                .get(0);
        return vehicleFound;

    }

    public boolean addVehicle(String regNumber, int customerID, VehicleType vehicleType, String model, String manufacturer, double engineSize, FuelType fuelType, String colour, int mileage, Date renewalDateMot, Date dateLastServiced, boolean coveredByWarranty, String warrantyName, String warrantyCompAddress, Date warrantyExpirationDate)
    {
        boolean result = persistence.addItem(Vehicle.class, (new Vehicle(regNumber,customerID,vehicleType, model,manufacturer,engineSize,fuelType,colour,mileage,renewalDateMot,dateLastServiced,coveredByWarranty,warrantyName,warrantyCompAddress,warrantyExpirationDate)));
        System.out.println("2");
        return result;

    }

    public boolean deleteVehicle(){

        return false;
    }

    public boolean editVehicle(){

        return false;

    }

    public static VehicleSys getInstance(CriterionRepository persistence) {
        if (instance == null) instance = new VehicleSys(persistence);
        return instance;
    }

}
