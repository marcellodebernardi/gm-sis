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
    private CriterionRepository persistence = DatabaseRepository.getInstance();

    private VehicleSys() {


    }

    public List<Vehicle> getVehiclesList(){
        ArrayList<Vehicle> arrayList = new ArrayList<Vehicle>();
        return arrayList;
    }

    public Vehicle searchVehicle(String regNumber) {

        Vehicle result;
        result = persistence.getByCriteria(false, Vehicle.class,
                Collections.singletonList(new Vehicle(regNumber, -1, null, null, null, -1,null,null,-1,null,null,false, null,null,null)))
                .get(0);
        if (result == null)
        {
            result = persistence.getByCriteria(false, Vehicle.class,
                    Collections.singletonList(new Vehicle(regNumber, -1, null, null, null, -1,null,null,-1,null,null,true, null,null,null)))
                    .get(0);
        }

        return result;

    }

    public boolean addVehicle(String regNumber, int customerID, VehicleType vehicleType, String model, String manufacturer, double engineSize, FuelType fuelType, String colour, int mileage, Date renewalDateMot, Date dateLastServiced, boolean coveredByWarranty, String warrantyName, String warrantyCompAddress, Date warrantyExpirationDate)
    {
        Vehicle add = new Vehicle(regNumber,customerID,vehicleType, model,manufacturer,engineSize,fuelType,colour,mileage,renewalDateMot,dateLastServiced,coveredByWarranty,warrantyName,warrantyCompAddress,warrantyExpirationDate);
        boolean result = persistence.addItem(Vehicle.class, add);
        return result;
    }

    public boolean deleteVehicle(String regNumber){
        if ((persistence.deleteItem(Vehicle.class,new Vehicle(regNumber,-1,null,null,null,-1,null,null,-1,null,null, false,null,null,null))))
        {
            return true;
        }
        return persistence.deleteItem(Vehicle.class, new Vehicle(regNumber,-1,null,null,null,-1,null,null,-1,null,null, true,null,null,null));
    }

    public boolean editVehicle(Vehicle delete){

        return persistence.updateItem(Vehicle.class, delete);

    }

    public static VehicleSys getInstance() {
        if (instance == null) instance = new VehicleSys();
        return instance;
    }

}
