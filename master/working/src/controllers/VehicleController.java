package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.ComboBox;
import entities.*;
import persistence.DatabaseRepository;

import javax.xml.soap.Text;
import java.text.*;

import java.util.Date;
import java.util.Locale;

/**
 * Created by DillonVaghela on 2/9/17.
 */
public class VehicleController {

    private VehicleSys vSys = VehicleSys.getInstance();
    public TextField vReg;
    public TextField reg;
    public TextField cID;
    public ComboBox vType = new ComboBox();
    public TextField mod;
    public TextField manuf;
    public TextField eSize;
    public ComboBox fType;
    public TextField col;
    public TextField mil;
    public TextField rDateMot;
    public TextField dLastServiced;
    public ComboBox cByWarranty;
    public TextField wName;
    public TextField wCompAddress;
    public TextField wExpirationDate;
    public Button editV;
    public TextField tVReg;
    @FXML
    public TextField eVR;
    public TextField eCID;



    public VehicleController()
    {

    }



    public void addVehicle()
    {
        try
        {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            Date rdm = format.parse(rDateMot.getText());
            Date dls = format.parse(dLastServiced.getText());
            Date wed = format.parse(wExpirationDate.getText());
            VehicleType vT;
            if (vType.getItems().get(0).equals("Car"))
            {
                vT = VehicleType.Car;
            }
            else if (vType.getItems().get(1).equals("Van"))
            {
                vT = VehicleType.Van;
            }
            else
            {
                vT = VehicleType.Truck;
            }
            FuelType fT;
            if (fType.getItems().get(0).equals("Diesel"))
            {
                fT = FuelType.diesel;
            }
            else
            {
                fT = FuelType.petrol;
            }
            Boolean W;
            if (cByWarranty.getItems().get(0).equals("True"))
            {
                W = true;
            }
            else
            {
                W = false;
            }

            boolean check = vSys.addVehicle( reg.getText(),Integer.parseInt(cID.getText()), vT,mod.getText(),manuf.getText(), Double.parseDouble(eSize.getText()),fT,col.getText(),Integer.parseInt(mil.getText()), rdm,dls,W,wName.getText(),wCompAddress.getText(),wed);
            System.out.println(check + "added");
        }
        catch (Exception e)
        {
            e.printStackTrace(  );
        }
    }

    public void deleteVehicle()
    {
        try
        {
            boolean check = vSys.deleteVehicle( vReg.getText());
            System.out.println(check + "deleted");
        }
        catch (Exception e)
        {
            e.printStackTrace(  );
        }
    }

    @FXML
    public void VehicleEditB() throws Exception {
        try{
            Vehicle vehicle = vSys.searchVehicle(tVReg.getText());
            setVehicleDets(vehicle);
        }
        catch (Exception e)
        {
            System.out.println("cant open a");
            System.out.println(e);
        }

    }

    public void setVehicleDets(Vehicle vehicle)
    {
        eVR.setText(vehicle.getRegNumber());
    }



}
