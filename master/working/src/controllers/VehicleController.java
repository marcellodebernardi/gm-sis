package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.BooleanStringConverter;
import javafx.util.converter.DateStringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import logic.VehicleSys;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.control.ComboBox;
import domain.*;

import java.text.*;

import java.util.Date;
import java.util.List;

/**
 * Created by DillonVaghela on 2/9/17.
 */
public class VehicleController {

    private VehicleSys vSys = VehicleSys.getInstance();
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
    public TextField eReg;
    public TableView<Vehicle> searchTable;
    public TableColumn<Vehicle, String> tReg;
    public TableColumn<Vehicle, Integer> tCID;
    public TableColumn<Vehicle, String> tVT;
    public TableColumn<Vehicle, String> tMod;
    public TableColumn<Vehicle, String> tManu;
    public TableColumn<Vehicle, Double> tEs;
    public TableColumn<Vehicle, String> tFT;
    public TableColumn<Vehicle, String> tCol;
    public TableColumn<Vehicle, Integer> tMil;
    public TableColumn<Vehicle, Date> tMOT;
    public TableColumn<Vehicle, Date> tDLS;
    public TableColumn<Vehicle, Boolean> tW;
    public TableColumn<Vehicle, String> tWn;
    public TableColumn<Vehicle, String> tA;
    public TableColumn<Vehicle, Date> tD;
    final ObservableList<Vehicle> tableEntries = FXCollections.observableArrayList();

    public void showAlert(String message)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Message");
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    public boolean showAlertC(String message)
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(message);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK){
            return true;
        } else {
            return false;
        }
    }

    public boolean checkFields()
    {
        boolean check = false;
        if ((!reg.getText().equals("")) && (!cID.getText().equals("")) && (!vType.getSelectionModel().getSelectedItem().toString().equals("")) && (!mod.getText().equals("")) && (!manuf.getText().equals("")) && (!eSize.getText().equals("")) && (!fType.getSelectionModel().getSelectedItem().toString().equals("")) && (!col.getText().equals("")) && (!mil.getText().equals("")) && (!rDateMot.getText().equals("")) && (!dLastServiced.getText().equals("")) && (!cByWarranty.getSelectionModel().getSelectedItem().toString().equals(""))) {
            check = true;
            if (cByWarranty.getSelectionModel().getSelectedItem().toString().equals("True")) {
                if (wName.getText().equals("") || wCompAddress.getText().equals("") || wExpirationDate.getText().equals("")) {
                    check = false;
                }
            }
        }
        return check;
    }

    public void addEditVehicle()
    {
        String addOrEdit;
        if (eReg != null)
        {
            addOrEdit = "edit";
            // true is edit
        }
        else
        {
            addOrEdit = "add";
            // false is add
        }
        try
        {
            boolean check = checkFields();
                if (check) {
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                    Date rdm = format.parse(rDateMot.getText());
                    Date dls = format.parse(dLastServiced.getText());
                    Date wed = new Date();
                    if ((!wExpirationDate.getText().equals(""))){
                    wed = format.parse(wExpirationDate.getText()); }
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
                    boolean add = showAlertC("Are you sure you want to " + addOrEdit +" this Vehicle, have you checked the vehicle details?");
                    if (add) {
                        boolean checker = vSys.addEditVehicle(reg.getText(), Integer.parseInt(cID.getText()), vT, mod.getText(), manuf.getText(), Double.parseDouble(eSize.getText()), fT, col.getText(), Integer.parseInt(mil.getText()), rdm, dls, W, wName.getText(), wCompAddress.getText(), wed);
                        showAlert("vehicle "+ addOrEdit+": " + Boolean.toString(checker));
                        if (checker) {
                            Stage addStage = (Stage) cByWarranty.getScene().getWindow();
                            addStage.close();
                        }
                    }
                }

            else  {
                showAlert("Can't " + addOrEdit +" as all fields required are incomplete");
            }
        }
        catch (Exception e)
        {
            System.out.println("Vehicle " + addOrEdit +" error");
            e.printStackTrace(  );
        }
    }

    public void deleteVehicle()
    {
        try
        {
            if (reg.getText().equals(""))
            {
                showAlert("No Vehicle entered to delete");
                return;
            }
            if ((!showAlertC("Sure you want to delete this Vehicle")))
            {
                return;
            }
            boolean check = vSys.deleteVehicle( reg.getText());
            if (check)
            {
                showAlert("Vehicle Found and Deleted: " + Boolean.toString(check));
            }
            else {
                showAlert("Cant find Reg Plate");
            }
        }
        catch (Exception e)
        {
            System.out.println("Vehicle delete error");
            e.printStackTrace(  );
        }
    }

    @FXML
    public void VehicleEditS() throws Exception {
        try{
            Vehicle vehicle = vSys.searchVehicle(eReg.getText());
            if (vehicle == null)
            {
                showAlert("Registration Number invalid");
            }
            else {
                showAlert("Vehicle Found!");
                setVehicleDets(vehicle);
            }
        }
        catch (Exception e)
        {
            System.out.println("Vehicle editSearch error");
            System.out.println(e);
        }

    }



    public void setVehicleDets(Vehicle vehicle)
    {
        reg.setText(vehicle.getRegNumber());
        reg.setDisable(false);
        cID.setText( Integer.toString(vehicle.getCustomerID()));
        cID.setDisable(false);
        vType.setValue(vehicle.getVehicleType().toString());
        vType.setDisable(false);
        mod.setText(vehicle.getModel());
        mod.setDisable(false);
        manuf.setText(vehicle.getManufacturer());
        manuf.setDisable(false);
        eSize.setText(Double.toString(vehicle.getEngineSize()));
        eSize.setDisable(false);
        String VT= vehicle.getFuelType().toString();
        fType.setValue(VT.substring(0, 1).toUpperCase() + VT.substring(1,VT.length()));
        fType.setDisable(false);
        col.setText(vehicle.getColour());
        col.setDisable(false);
        mil.setText(Integer.toString(vehicle.getMileage()));
        mil.setDisable(false);
        rDateMot.setText(vehicle.getRenewalDateMot().toString());
        rDateMot.setDisable(false);
        dLastServiced.setText(vehicle.getDateLastServiced().toString());
        dLastServiced.setDisable(false);
        cByWarranty.setDisable(false);
        if (vehicle.isCoveredByWarranty())
        {
            cByWarranty.setValue("True");
        }
        else {
            cByWarranty.setValue("False");
        }
        cByWarranty.setDisable(false);
        wName.setText(vehicle.getWarrantyName());
        wName.setDisable(false);
        wCompAddress.setText(vehicle.getWarrantyCompAddress());
        wCompAddress.setDisable(false);
        wExpirationDate.setText(vehicle.getWarrantyExpirationDate().toString());
        wExpirationDate.setDisable(false);
        hiddenWarranty();
    }

    public void searchVehicle()
    {
        List<Vehicle> arrayList = vSys.getVehiclesList();

    }

    public void searchVehicleA()
    {
        List<Vehicle> arrayList = vSys.getVehiclesList();
        DisplayTable(arrayList);

    }

    public void DisplayTable(List<Vehicle> arrayList)
    {
        searchTable.setDisable(false);
        tableEntries.removeAll(tableEntries);
        for(int i =0; i<arrayList.size(); i++){

            tableEntries.add(arrayList.get(i));
        }

        tReg.setCellValueFactory(new PropertyValueFactory<Vehicle, String>("tReg"));
        tReg.setCellFactory(TextFieldTableCell.<Vehicle>forTableColumn());

        tCID.setCellValueFactory(new PropertyValueFactory<Vehicle, Integer>("tCID"));
        tCID.setCellFactory(TextFieldTableCell.<Vehicle, Integer>forTableColumn(new IntegerStringConverter()));

        tVT.setCellValueFactory(new PropertyValueFactory<Vehicle, String>("tVT"));
        tVT.setCellFactory(TextFieldTableCell.<Vehicle>forTableColumn( ));

        tMod.setCellValueFactory(new PropertyValueFactory<Vehicle, String>("tMod"));
        tMod.setCellFactory(TextFieldTableCell.<Vehicle>forTableColumn( ));

        tManu.setCellValueFactory(new PropertyValueFactory<Vehicle, String>("tManu"));
        tManu.setCellFactory(TextFieldTableCell.<Vehicle>forTableColumn( ));

        tEs.setCellValueFactory(new PropertyValueFactory<Vehicle, Double>("tManu"));
        tEs.setCellFactory(TextFieldTableCell.<Vehicle, Double>forTableColumn( new DoubleStringConverter()));

        tFT.setCellValueFactory(new PropertyValueFactory<Vehicle, String>("tFT"));
        tFT.setCellFactory(TextFieldTableCell.<Vehicle>forTableColumn( ));

        tCol.setCellValueFactory(new PropertyValueFactory<Vehicle, String>("tCol"));
        tCol.setCellFactory(TextFieldTableCell.<Vehicle>forTableColumn( ));

        tMil.setCellValueFactory(new PropertyValueFactory<Vehicle, Integer>("tMil"));
        tMil.setCellFactory(TextFieldTableCell.<Vehicle, Integer>forTableColumn(new IntegerStringConverter() ));

        tMOT.setCellValueFactory(new PropertyValueFactory<Vehicle, Date>("tMOT"));
        tMOT.setCellFactory(TextFieldTableCell.<Vehicle, Date>forTableColumn(new DateStringConverter() ));

        tDLS.setCellValueFactory(new PropertyValueFactory<Vehicle, Date>("tMOT"));
        tDLS.setCellFactory(TextFieldTableCell.<Vehicle, Date>forTableColumn(new DateStringConverter() ));

        tW.setCellValueFactory(new PropertyValueFactory<Vehicle, Boolean>("tW"));
        tW.setCellFactory(TextFieldTableCell.<Vehicle, Boolean>forTableColumn(new BooleanStringConverter()));

        tWn.setCellValueFactory(new PropertyValueFactory<Vehicle, String>("tWn"));
        tWn.setCellFactory(TextFieldTableCell.<Vehicle>forTableColumn( ));

        tA.setCellValueFactory(new PropertyValueFactory<Vehicle, String>("tA"));
        tA.setCellFactory(TextFieldTableCell.<Vehicle>forTableColumn( ));

        tD.setCellValueFactory(new PropertyValueFactory<Vehicle, Date>("tD"));
        tD.setCellFactory(TextFieldTableCell.<Vehicle, Date>forTableColumn(new DateStringConverter() ));

        searchTable.setItems(tableEntries);
    }

    public void hiddenWarranty()
    {
        if (cByWarranty.getSelectionModel().getSelectedItem().equals("True") || cByWarranty.getSelectionModel().getSelectedItem().equals("true") ) {
            wName.setDisable(false);
            wCompAddress.setDisable(false);
            wExpirationDate.setDisable(false);
        }
        else {
            wName.setDisable(true);
            wCompAddress.setDisable(true);
            wExpirationDate.setDisable(true);
            wName.clear();
            wCompAddress.clear();
            wExpirationDate.clear();
        }
    }



}
