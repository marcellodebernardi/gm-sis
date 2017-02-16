package controllers;

import entities.Vehicle;
import javafx.scene.control.TextField;
import logic.VehicleSys;
import javax.xml.soap.Text;

/**
 * Created by DillonVaghela on 2/16/17.
 */
public class VehicleEditController {
    public TextField Ereg;
    public TextField cID;
    public TextField vT;
    public TextField model;
    public TextField manu;
    public TextField eSize;
    public TextField fType;
    public TextField colour;
    public TextField mileage;
    public TextField rDM;
    public TextField dLS;
    public TextField cBW;
    public TextField wN;
    public TextField wCA;
    public TextField wED;

    public void initialize() throws Exception {
        Vehicle vehicle = VehicleSys.getInstance().getVehicle();
        if (vehicle != null)
        {
        System.out.println(vehicle.getRegNumber());
        }
        else {
            System.out.println("error");
        }
        //Ereg.setText(vehicle.getRegNumber());

    }


}
