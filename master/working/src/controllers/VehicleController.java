package controllers;

import logic.AuthenticationSystem;
import logic.CriterionRepository;
import logic.VehicleSys;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import logic.AuthenticationSystem;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;

/**
 * Created by DillonVaghela on 2/9/17.
 */
public class VehicleController {

    //private VehicleSys vSys = VehicleSys.getInstance();
    public TextField reg;
    public TextField cID;
    public TextField vType;
    public TextField mod;
    public TextField manuf;
    public TextField eSize;
    public TextField fType;
    public TextField col;
    public TextField mil;
    public TextField rDateMot;
    public TextField dLastServiced;
    public TextField cByWarranty;
    public TextField wName;
    public TextField wCompAddress;
    public TextField wExpirationDate;

    public void addVehicle()
    {
        try
        {
            //boolean check = vSys.addVehicle(reg.getText(),cID.getText(),vType.getText(),mod.getText(),manuf.getText(), eSize.getText(),fType.getText(),col.getText(),mil.getText(),rDateMot.getText(),dLastServiced.getText(),cByWarranty.getText(),wName.getText(),wCompAddress.getText(),wExpirationDate.getText());
            //System.out.println(check);
        }
        catch (Exception e)
        {
            System.out.println("not working");
        }
    }
}
