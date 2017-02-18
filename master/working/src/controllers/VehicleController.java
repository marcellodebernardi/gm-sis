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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
    public ComboBox eVT;
    public TextField eModel;
    public TextField eM;
    public TextField eES;
    public ComboBox eFT;
    public TextField eC;
    public TextField eMi;
    public TextField eRDM;
    public TextField eDLS;
    public ComboBox eCBW;
    public TextField eWN;
    public TextField eWCA;
    public TextField eWED;
    public ComboBox sCBW;
    public TextField sWN;
    public TextField sWCA;
    public TextField sWED;



    public VehicleController()
    {

    }



    public void addVehicle()
    {
        try
        {
            boolean check = false;
            if ((!reg.getText().equals("")) && (!cID.getText().equals("")) && (!vType.getSelectionModel().getSelectedItem().toString().equals("")) && (!mod.getText().equals("")) && (!manuf.getText().equals("")) && (!eSize.getText().equals("")) && (!fType.getSelectionModel().getSelectedItem().toString().equals("")) && (!col.getText().equals("")) && (!mil.getText().equals("")) && (!rDateMot.getText().equals("")) && (!dLastServiced.getText().equals("")) && (!cByWarranty.getSelectionModel().getSelectedItem().toString().equals("")))
            {
                check = true;
                if (cByWarranty.getSelectionModel().getSelectedItem().toString().equals("True"))
                {
                    if (wName.getText().equals("") || wCompAddress.getText().equals("") || wExpirationDate.getText().equals(""))
                    {
                        check = false;
                    }
                }
                if (check) {
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                    Date rdm = format.parse(rDateMot.getText());
                    Date dls = format.parse(dLastServiced.getText());
                    Date wed = format.parse(wExpirationDate.getText());
                    VehicleType vT;
                    if (vType.getSelectionModel().getSelectedItem().toString().equals("Car")) {
                        vT = VehicleType.Car;
                    } else if (vType.getSelectionModel().getSelectedItem().toString().equals("Van")) {
                        vT = VehicleType.Van;
                    } else {
                        vT = VehicleType.Truck;
                    }
                    FuelType fT;
                    if (fType.getSelectionModel().getSelectedItem().toString().equals("Diesel")) {
                        fT = FuelType.diesel;
                    } else {
                        fT = FuelType.petrol;
                    }
                    Boolean W;
                    if (cByWarranty.getSelectionModel().getSelectedItem().toString().equals("True")) {
                        W = true;
                    } else {
                        W = false;
                    }

                    boolean checker = vSys.addVehicle(reg.getText(), Integer.parseInt(cID.getText()), vT, mod.getText(), manuf.getText(), Double.parseDouble(eSize.getText()), fT, col.getText(), Integer.parseInt(mil.getText()), rdm, dls, W, wName.getText(), wCompAddress.getText(), wed);
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Message");
                    alert.setHeaderText("Vehicle Added: " + checker);
                    alert.showAndWait();
                    Stage addStage =   (Stage) cByWarranty.getScene().getWindow();
                    addStage.close();
                }
            }
            if (!check) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("All Fields required are not complete!");
                alert.showAndWait();
            }
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
            if (check)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("success");
                alert.setHeaderText("vehicle deleted");
                alert.showAndWait();
            }
            else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Cant find Reg Plate");
            alert.showAndWait(); }
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
            if (vehicle == null)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Registration Number invalid");
                alert.showAndWait();
            }
            else {
                setVehicleDets(vehicle);
            }
        }
        catch (Exception e)
        {
            System.out.println("cant open a");
            System.out.println(e);
        }

    }

    public void editVehicle()
    {
        try
        {
            boolean check = false;
            if ((!eVR.getText().equals("")) && (!eCID.getText().equals("")) && (!eVT.getSelectionModel().getSelectedItem().toString().equals("")) && (!eModel.getText().equals("")) && (!eM.getText().equals("")) && (!eSize.getText().equals("")) && (!eFT.getSelectionModel().getSelectedItem().toString().equals("")) && (!eC.getText().equals("")) && (!eMi.getText().equals("")) && (!eRDM.getText().equals("")) && (!eDLS.getText().equals("")) && (!eCBW.getSelectionModel().getSelectedItem().toString().equals("")))
            {
                check = true;
                if (eCBW.getSelectionModel().getSelectedItem().toString().equals("True"))
                {
                    if (eWN.getText().equals("") || eWCA.getText().equals("") || eWED.getText().equals(""))
                    {
                        check = false;
                    }
                }
                if (check) {
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                    Date rdm = format.parse(eRDM.getText());
                    Date dls = format.parse(eDLS.getText());
                    Date wed = format.parse(eWED.getText());
                    VehicleType vT;
                    if (eVT.getSelectionModel().getSelectedItem().toString().equals("Car")) {
                        vT = VehicleType.Car;
                    } else if (eVT.getSelectionModel().getSelectedItem().toString().equals("Van")) {
                        vT = VehicleType.Van;
                    } else {
                        vT = VehicleType.Truck;
                    }
                    FuelType fT;
                    if (eFT.getSelectionModel().getSelectedItem().toString().equals("Diesel")) {
                        fT = FuelType.diesel;
                    } else {
                        fT = FuelType.petrol;
                    }
                    Boolean W;
                    if (eCBW.getSelectionModel().getSelectedItem().toString().equals("True")) {
                        W = true;
                    } else {
                        W = false;
                    }

                    boolean checker = vSys.editVehicle(eVR.getText(), Integer.parseInt(eCID.getText()), vT, eModel.getText(), eM.getText(), Double.parseDouble(eES.getText()), fT, eC.getText(), Integer.parseInt(eMi.getText()), rdm, dls, W, eWN.getText(), eWCA.getText(), wed);
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Message");
                    alert.setHeaderText("Vehicle Added: " + checker);
                    alert.showAndWait();
                    Stage addStage =   (Stage) eVR.getScene().getWindow();
                    addStage.close();
                }
            }
            if (!check) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("All Fields required are not complete!");
                alert.showAndWait();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace(  );
        }
    }

    public void setVehicleDets(Vehicle vehicle)
    {

        eVR.setText(vehicle.getRegNumber());
        eCID.setText( Integer.toString(vehicle.getCustomerID()));
        eVT.setValue(vehicle.getVehicleType().toString());
        eModel.setText(vehicle.getModel());
        eM.setText(vehicle.getManufacturer());
        eES.setText(Double.toString(vehicle.getEngineSize()));
        eFT.setValue(vehicle.getFuelType().toString());
        eC.setText(vehicle.getColour());
        eMi.setText(Integer.toString(vehicle.getMileage()));
        eRDM.setText(vehicle.getRenewalDateMot().toString());
        eDLS.setText(vehicle.getDateLastServiced().toString());
        eCBW.setValue(vehicle.isCoveredByWarranty());
        eWN.setText(vehicle.getWarrantyName());
        eWCA.setText(vehicle.getWarrantyCompAddress());
        eWED.setText(vehicle.getWarrantyExpirationDate().toString());

    }

    public void searchVehicle()
    {
        List<Vehicle> arrayList = vSys.getVehiclesList();

    }

    public void hiddenWarranty()
    {
        if (sCBW.getSelectionModel().getSelectedItem().equals("True")) {
            sWN.setDisable(false);
            sWCA.setDisable(false);
            sWED.setDisable(false);
        }
        else {
            sWN.setDisable(true);
            sWCA.setDisable(true);
            sWED.setDisable(true);

        }
    }


}
