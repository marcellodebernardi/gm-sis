package logic;

import entities.Vehicle;
import persistence.DatabaseRepository;
import java.util.*;

/**
 * Created by DillonVaghela on 2/9/17.
 */
public class VehicleSys {

    private static VehicleSys instance;
    private CriterionRepository persistence = DatabaseRepository.getInstance();

    private VehicleSys(CriterionRepository persistence) {
        this.persistence = persistence;

    }

    public List<Vehicle> getVehiclesList(){
        ArrayList<Vehicle> arrayList = new ArrayList<Vehicle>();
        return arrayList;
    }

    public List<Vehicle> searchVehicle() {

        /** TODO: create search method
         *
         */

        List<Vehicle> vehicleList = new ArrayList<Vehicle>();
        return vehicleList;

    }

    public boolean addVehicle(){

        return false;

    }

    public boolean deleteVehicle(){

        return false;
    }

    public boolean editVehicle(){

        return false;

    }

    public VehicleSys getInstance(CriterionRepository persistence) {
        if (instance == null) instance = new VehicleSys(persistence);
        return instance;
    }

}
