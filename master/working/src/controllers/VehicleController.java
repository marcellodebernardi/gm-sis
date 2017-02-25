package controllers;

import domain.CustomerType;
import domain.FuelType;
import domain.Vehicle;
import domain.VehicleType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.BooleanStringConverter;
import javafx.util.converter.DateStringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import logic.VehicleSys;
import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by DillonVaghela on 2/9/17.
 */
public class VehicleController {


    @FXML
    private VehicleSys vSys = VehicleSys.getInstance();
    @FXML
    private TextField reg, cID, mod, manuf, eSize, col, mil, rDateMot, dLastServiced, wName, wCompAddress, wExpirationDate, regS, manufS;
    @FXML
    private ComboBox vType, fType, cByWarranty, VehicleS;
    @FXML
    private TableView<Vehicle> searchTable;
    @FXML
    private TableColumn<Vehicle, String> tReg, tMod, tManu, tCol, tWn, tA, tFN, tLN, tCA, tCP, tCPN, tCE;
    @FXML
    private TableColumn<Vehicle, CustomerType> tCT;
    @FXML
    private TableColumn<Vehicle, DateTime> tDS, tRS;
    @FXML
    private TableColumn<Vehicle, Integer> tCID, tMil;
    @FXML
    private TableColumn<Vehicle, VehicleType> tVT;
    @FXML
    private TableColumn<Vehicle, Double> tEs;
    @FXML
    private TableColumn<Vehicle, FuelType> tFT;
    @FXML
    private TableColumn<Vehicle, Date> tMOT, tDLS, tD;
    @FXML
    private TableColumn<Vehicle, Boolean> tW;
    final ObservableList<Vehicle> tableEntries = FXCollections.observableArrayList();
    @FXML
    private CheckBox cReg, cCID, cVT, cMod, cManu, cES, cFT, cC, cMil, cMOT, cDLS, cW, cWN, cA, cD, cFN, cLN, cCA, cCPC, cCP, cCE, cCT, cDS, cRS;
    @FXML
    private Label AddEditL;
    @FXML
    private Button deleteV, ClearV, addV;


    public void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Message");
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    public boolean showAlertC(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(message);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkFields() {
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

    public void addEditVehicle() {
        String addOrEdit = "";
        if (AddEditL.getText().equals("Edit Vehicle")) {
            addOrEdit = "edit";
            // true is edit
        } else {
            addOrEdit = "add";
            // false is add
        }
        showAlert(reg.getText());
        try {
            boolean check = checkFields();
            if (check) {
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                Date rdm = format.parse(rDateMot.getText());
                Date dls = format.parse(dLastServiced.getText());
                Date wed = new Date();
                if ((!wExpirationDate.getText().equals(""))) {
                    wed = format.parse(wExpirationDate.getText());
                }
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
                boolean add = showAlertC("Are you sure you want to " + addOrEdit + " this Vehicle, have you checked the vehicle details?");
                if (add) {
                    boolean checker = vSys.addEditVehicle(reg.getText(), Integer.parseInt(cID.getText()), vT, mod.getText(), manuf.getText(), Double.parseDouble(eSize.getText()), fT, col.getText(), Integer.parseInt(mil.getText()), rdm, dls, W, wName.getText(), wCompAddress.getText(), wed);
                    showAlert("vehicle " + addOrEdit + ": " + Boolean.toString(checker));
                    if (checker) {
                        Stage addStage = (Stage) cByWarranty.getScene().getWindow();
                        addStage.close();
                    }
                }
            } else {
                showAlert("Can't " + addOrEdit + " as all fields required are incomplete");
            }
        } catch (Exception e) {
            System.out.println("Vehicle " + addOrEdit + " error");
            e.printStackTrace();
        }
    }

    public void deleteVehicle() {
        try {
            if (reg.getText().equals("")) {
                showAlert("No Vehicle entered to delete");
                return;
            }
            if ((!showAlertC("Sure you want to delete this Vehicle?"))) {
                return;
            }
            boolean check = vSys.deleteVehicle(reg.getText());
            if (check) {
                showAlert("Vehicle Found and Deleted: " + Boolean.toString(check));
            } else {
                showAlert("Cant find Reg Plate");
            }
        } catch (Exception e) {
            System.out.println("Vehicle delete error");
            e.printStackTrace();
        }
    }

    @FXML
    public void VehicleEditS()  {
        try {
            Vehicle vehicle = searchTable.getSelectionModel().getSelectedItem();
            if (vehicle == null)
            {
                throw new  Exception();
            }
                setVehicleDets(vehicle);
        } catch (Exception e) {
            showAlert("No Vehicle Selected");
            System.out.println(e);
            e.printStackTrace();
        }

    }

    public void DeleteSelectedVehicle()
    {
        try {
            Vehicle vehicle = searchTable.getSelectionModel().getSelectedItem();
            if (vehicle == null)
            {
                throw new  Exception();
            }
            System.out.println(vehicle.getRegNumber());
            if ((!showAlertC("Sure you want to delete this Vehicle?"))) {
                return;
            }
            boolean check = vSys.deleteVehicle(vehicle.getRegNumber());
            if (check) {
                showAlert("Vehicle Found and Deleted: " + Boolean.toString(check));
            } else {
                showAlert("Not deleted");
            }
        } catch (Exception e) {
            showAlert("no Vehicle Selected");
            //e.printStackTrace();
        }
    }


    public void setVehicleDets(Vehicle vehicle) {
        regS.setText(vehicle.getRegNumber());
        AddEditL.setText("Edit Vehicle");
        reg.setText(vehicle.getRegNumber());
        addV.setText("Edit");
        VehicleS.setDisable(true);
        deleteV.setDisable(false);
        reg.setText(vehicle.getRegNumber());
        cID.setText(Integer.toString(vehicle.getCustomerID()));
        vType.setValue(vehicle.getVehicleType().toString());
        mod.setText(vehicle.getModel());
        manuf.setText(vehicle.getManufacturer());
        eSize.setText(Double.toString(vehicle.getEngineSize()));
        String VT = vehicle.getFuelType().toString();
        fType.setValue(VT.substring(0, 1).toUpperCase() + VT.substring(1, VT.length()));
        mil.setText(Integer.toString(vehicle.getMileage()));
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        rDateMot.setText(format.format(vehicle.getRenewalDateMot()));
        dLastServiced.setText(format.format(vehicle.getDateLastServiced()));
        if (vehicle.isCoveredByWarranty()) {
            cByWarranty.setValue("True");
        } else {
            cByWarranty.setValue("False");
        }
        wName.setText(vehicle.getWarrantyName());
        wCompAddress.setText(vehicle.getWarrantyCompAddress());
        wExpirationDate.setText(format.format(vehicle.getWarrantyExpirationDate()));
        hiddenWarranty();

    }

    public void searchVehicle() {
        try {

                List<Vehicle> arrayList = vSys.searchVehicle(regS.getText(), manufS.getText());
                DisplayTable(arrayList);



        } catch (Exception e) {
            System.out.println("Search Vehicle Error");
            e.printStackTrace();
        }
    }

    public void searchVehicleA() {
        List<Vehicle> arrayList = vSys.getVehiclesList();
        DisplayTable(arrayList);

    }


    public void DisplayTable(List<Vehicle> arrayList) {
        try {
            searchTable.setDisable(false);
            tableEntries.removeAll(tableEntries);
            for (int i = 0; i < arrayList.size(); i++) {

                tableEntries.add(arrayList.get(i));
            }

            tReg.setCellValueFactory(new PropertyValueFactory<Vehicle, String>("regNumber"));
            tReg.setCellFactory(TextFieldTableCell.<Vehicle>forTableColumn());
            tReg.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Vehicle, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Vehicle, String> event) {
                    (event.getTableView().getItems().get(event.getTablePosition().getRow())).setRegNumber(event.getNewValue());
                }
            });

            tCID.setCellValueFactory(new PropertyValueFactory<Vehicle, Integer>("customerID"));
            tCID.setCellFactory(TextFieldTableCell.<Vehicle, Integer>forTableColumn(new IntegerStringConverter()));
            tCID.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Vehicle, Integer>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Vehicle, Integer> event) {
                    (event.getTableView().getItems().get(event.getTablePosition().getRow())).setCustomerID(event.getNewValue());
                }
            });


            tVT.setCellValueFactory(new PropertyValueFactory<Vehicle, VehicleType>("vehicleType"));
            tVT.setCellFactory(TextFieldTableCell.<Vehicle, VehicleType>forTableColumn(new StringConverter<VehicleType>() {
                @Override
                public String toString(VehicleType object) {
                    return object.toString();
                }

                @Override
                public VehicleType fromString(String string) {
                    if (string.equals("car"))
                    {
                        return VehicleType.Car;
                    }
                    else  if (string.equals("Truck"))
                    {
                        return VehicleType.Truck;
                    }
                    return VehicleType.Van;
                }
            }));
            tVT.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Vehicle, VehicleType>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Vehicle, VehicleType> event) {
                    VehicleType vehicle;
                    if (event.getNewValue().equals("Car")) {
                        vehicle = VehicleType.Car;
                    } else {
                        vehicle = VehicleType.Truck;
                    }
                    (event.getTableView().getItems().get(event.getTablePosition().getRow())).setVehicleType(vehicle);
                }
            });

            tMod.setCellValueFactory(new PropertyValueFactory<Vehicle, String>("model"));
            tMod.setCellFactory(TextFieldTableCell.<Vehicle>forTableColumn());
            tMod.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Vehicle, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Vehicle, String> event) {
                    (event.getTableView().getItems().get(event.getTablePosition().getRow())).setModel(event.getNewValue());
                }
            });


            tManu.setCellValueFactory(new PropertyValueFactory<Vehicle, String>("manufacturer"));
            tManu.setCellFactory(TextFieldTableCell.<Vehicle>forTableColumn());
            tManu.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Vehicle, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Vehicle, String> event) {
                    (event.getTableView().getItems().get(event.getTablePosition().getRow())).setManufacturer(event.getNewValue());
                }
            });

            tEs.setCellValueFactory(new PropertyValueFactory<Vehicle, Double>("engineSize"));
            tEs.setCellFactory(TextFieldTableCell.<Vehicle, Double>forTableColumn(new DoubleStringConverter()));
            tEs.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Vehicle, Double>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Vehicle, Double> event) {
                    (event.getTableView().getItems().get(event.getTablePosition().getRow())).setEngineSize(event.getNewValue());
                }
            });

            tFT.setCellValueFactory(new PropertyValueFactory<Vehicle, FuelType>("fuelType"));
            tFT.setCellFactory(TextFieldTableCell.<Vehicle, FuelType>forTableColumn(new StringConverter<FuelType>() {
                @Override
                public String toString(FuelType object) {
                    return object.toString();
                }

                @Override
                public FuelType fromString(String string) {
                    if (string.equals("diesel"))
                    {
                        return FuelType.diesel;
                    }
                    return FuelType.petrol;
                }
            }));
            tFT.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Vehicle, FuelType>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Vehicle, FuelType> event) {
                    (event.getTableView().getItems().get(event.getTablePosition().getRow())).setFuelType(event.getNewValue());
                }
            });


            tCol.setCellValueFactory(new PropertyValueFactory<Vehicle, String>("colour"));
            tCol.setCellFactory(TextFieldTableCell.<Vehicle>forTableColumn());
            tCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Vehicle, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Vehicle, String> event) {
                    (event.getTableView().getItems().get(event.getTablePosition().getRow())).setColour(event.getNewValue());
                }
            });

            tMil.setCellValueFactory(new PropertyValueFactory<Vehicle, Integer>("mileage"));
            tMil.setCellFactory(TextFieldTableCell.<Vehicle, Integer>forTableColumn(new IntegerStringConverter()));
            tMil.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Vehicle, Integer>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Vehicle, Integer> event) {
                    (event.getTableView().getItems().get(event.getTablePosition().getRow())).setMileage(event.getNewValue());
                }
            });

            tMOT.setCellValueFactory(new PropertyValueFactory<Vehicle, Date>("renewalDateMot"));
            tMOT.setCellFactory(TextFieldTableCell.<Vehicle, Date>forTableColumn(new DateStringConverter()));
            tMOT.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Vehicle, Date>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Vehicle, Date> event) {
                    (event.getTableView().getItems().get(event.getTablePosition().getRow())).setRenewalDateMot(event.getNewValue());
                }
            });

            tDLS.setCellValueFactory(new PropertyValueFactory<Vehicle, Date>("dateLastServiced"));
            tDLS.setCellFactory(TextFieldTableCell.<Vehicle, Date>forTableColumn(new DateStringConverter()));
            tDLS.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Vehicle, Date>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Vehicle, Date> event) {
                    (event.getTableView().getItems().get(event.getTablePosition().getRow())).setDateLastServiced(event.getNewValue());
                }
            });

            tW.setCellValueFactory(new PropertyValueFactory<Vehicle, Boolean>("coveredByWarranty"));
            tW.setCellFactory(TextFieldTableCell.<Vehicle, Boolean>forTableColumn(new BooleanStringConverter()));
            tW.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Vehicle, Boolean>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Vehicle, Boolean> event) {
                    (event.getTableView().getItems().get(event.getTablePosition().getRow())).setCoveredByWarranty(event.getNewValue());
                }
            });

            tWn.setCellValueFactory(new PropertyValueFactory<Vehicle, String>("warrantyName"));
            tWn.setCellFactory(TextFieldTableCell.<Vehicle>forTableColumn());
            tWn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Vehicle, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Vehicle, String> event) {
                    (event.getTableView().getItems().get(event.getTablePosition().getRow())).setWarrantyName(event.getNewValue());
                }
            });

            tA.setCellValueFactory(new PropertyValueFactory<Vehicle, String>("warrantyCompAddress"));
            tA.setCellFactory(TextFieldTableCell.<Vehicle>forTableColumn());
            tA.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Vehicle, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Vehicle, String> event) {
                    (event.getTableView().getItems().get(event.getTablePosition().getRow())).setWarrantyCompAddress(event.getNewValue());
                }
            });

            tD.setCellValueFactory(new PropertyValueFactory<Vehicle, Date>("warrantyExpirationDate"));
            tD.setCellFactory(TextFieldTableCell.<Vehicle, Date>forTableColumn(new DateStringConverter()));
            tD.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Vehicle, Date>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Vehicle, Date> event) {
                    (event.getTableView().getItems().get(event.getTablePosition().getRow())).setWarrantyExpirationDate(event.getNewValue());
                }
            });

            searchTable.setItems(tableEntries);
        } catch (Exception e) {
            System.out.println("not working");
            e.printStackTrace();
            System.out.println(e);
        }

    }

    public void hiddenWarranty()  {
        try {


        if (cByWarranty.getSelectionModel().getSelectedItem().toString().equals("True") || cByWarranty.getSelectionModel().getSelectedItem().equals(null)) {
            wName.setDisable(false);
            wCompAddress.setDisable(false);
            wExpirationDate.setDisable(false);
        } else {
            wName.setDisable(true);
            wCompAddress.setDisable(true);
            wExpirationDate.setDisable(true);
            wName.clear();
            wCompAddress.clear();
            wExpirationDate.clear();
        }}
        catch (Exception e)
        {
            wName.setDisable(false);
            wCompAddress.setDisable(false);
            wExpirationDate.setDisable(false);
        }
    }

    public void changeColumns()
    {
        if (cReg.isSelected())
        {
            tReg.setVisible(true);
        }
        else
        {
            tReg.setVisible(false);
        }
        if (cCID.isSelected())
        {
            tCID.setVisible(true);
        }
        else
        {
            tCID.setVisible(false);
        }
        if (cVT.isSelected())
        {
            tVT.setVisible(true);
        }
        else
        {
            tVT.setVisible(false);
        }
        if (cMod.isSelected())
        {
            tMod.setVisible(true);
        }
        else
        {
            tMod.setVisible(false);
        }
        if (cManu.isSelected())
        {
            tManu.setVisible(true);
        }
        else
        {
            tManu.setVisible(false);
        }
        if (cES.isSelected())
        {
            tEs.setVisible(true);
        }
        else
        {
            tEs.setVisible(false);
        }
        if (cFT.isSelected())
        {
            tFT.setVisible(true);
        }
        else
        {
            tFT.setVisible(false);
        }
        if (cC.isSelected())
        {
            tCol.setVisible(true);
        }
        else
        {
            tCol.setVisible(false);
        }
        if (cMil.isSelected())
        {
            tMil.setVisible(true);
        }
        else
        {
            tMil.setVisible(false);
        }
        if (cMOT.isSelected())
        {
            tMOT.setVisible(true);
        }
        else
        {
            tMOT.setVisible(false);
        }
        if (cDLS.isSelected())
        {
            tDLS.setVisible(true);
        }
        else
        {
            tDLS.setVisible(false);
        }
        if (cW.isSelected())
        {
            tW.setVisible(true);
        }
        else
        {
            tW.setVisible(false);
        }
        if (cWN.isSelected())
        {
            tWn.setVisible(true);
        }
        else
        {
            tWn.setVisible(false);
        }
        if (cA.isSelected())
        {
            tA.setVisible(true);
        }
        else
        {
            tA.setVisible(false);
        }
        if (cD.isSelected())
        {
            tD.setVisible(true);
        }
        else
        {
            tD.setVisible(false);
        }
        if (cFN.isSelected())
        {
            tFN.setVisible(true);
        }
        else
        {
            tFN.setVisible(false);
        }
        if (cLN.isSelected())
        {
            tLN.setVisible(true);
        }
        else
        {
            tLN.setVisible(false);
        }
        if (cCA.isSelected())
        {
            tCA.setVisible(true);
        }
        else
        {
            tCA.setVisible(false);
        }
         if (cCPC.isSelected())
         {
             tCP.setVisible(true);
         }
         else
         {
             tCP.setVisible(false);
         }
         if (cCP.isSelected())
         {
             tCPN.setVisible(true);
         }
         else
         {
             tCPN.setVisible(false);
         }
         if (cCE.isSelected())
         {
             tCE.setVisible(true);
         }
         else
         {
             tCE.setVisible(false);
         }
         if (cCT.isSelected())
         {
             tCT.setVisible(true);
         }
         else
         {
             tCT.setVisible(false);
         }
         if (cDS.isSelected())
         {
             tDS.setVisible(true);
         }
         else
         {
             tDS.setVisible(false);
         }
         if (cRS.isSelected())
         {
             tRS.setVisible(true);
         }
         else
         {
             tRS.setVisible(false);
         }

    }

    public void selectVehicle()
    {
        if (VehicleS.getSelectionModel().isSelected(0))
        {
            setVehicle("Civic", "Honda", "1.6", "Car", "Petrol");
        }
        else if (VehicleS.getSelectionModel().isSelected(1))
        {
            setVehicle("Focus", "Ford", "1.2", "Car", "Diesel");
        }
        else if (VehicleS.getSelectionModel().isSelected(2))
        {
            setVehicle("5 Series", "BMw", "2.2", "Car", "Petrol");
        }
        else if (VehicleS.getSelectionModel().isSelected(3))
        {
            setVehicle("3 Series", "BMw", "2.9", "Car", "Diesel");
        }
        else if (VehicleS.getSelectionModel().isSelected(4))
        {
            setVehicle("A Class", "Mercedes", "3.0", "Car", "Petrol");
        }
        else if (VehicleS.getSelectionModel().isSelected(5))
        {
            setVehicle("Transit", "Ford", "2.2", "Van", "Petrol");
        }
        else if (VehicleS.getSelectionModel().isSelected(6))
        {
            setVehicle("Roadster", "Nissan", "1.2", "Truck", "Diesel");
        }
        else if (VehicleS.getSelectionModel().isSelected(7))
        {
            setVehicle("Y-8 Van", "Audi", "3.6", "Van", "Petrol");
        }
        else if (VehicleS.getSelectionModel().isSelected(8))
        {
            setVehicle("Enzo", "Ferrari", "4.4", "Car", "Petrol");
        }
        else if (VehicleS.getSelectionModel().isSelected(9))
        {
            setVehicle("Truckster", "Ford", "2.8", "Truck", "Diesel");
        }
        else if (VehicleS.getSelectionModel().isSelected(10))
        {
            setVehicle("Hybrid Van", "Renault", "2.3", "Can", "Petrol");
        }
        else if (VehicleS.getSelectionModel().isSelected(11))
        {
            setVehicle("Sport", "MG", "2.0", "Car", "Diesel");
        }
        else if (VehicleS.getSelectionModel().isSelected(12))
        {
            setVehicle("Model S", "Acura", "2.2", "Car", "Petrol");
        }
        else if (VehicleS.getSelectionModel().isSelected(13))
        {
            setVehicle("Arnage", "Bentley", "4.0", "Car", "Petrol");
        }
        else
        {
            setVehicle("Fire Truck", "DAF", "3.8", "Truck", "Diesel");
        }
    }

    public void setVehicle(String model, String manufacturer, String engineSize, String vehicleType, String fuelType)
    {
        mod.setText(model);
        manuf.setText(manufacturer);
        eSize.setText(engineSize);
        vType.setValue(vehicleType);
        fType.setValue(fuelType);
    }

    public void ClearFields()
    {
        reg.clear();
        cID.clear();
        mod.clear();
        manuf.clear();
        eSize.clear();
        col.clear();
        mil.clear();
        rDateMot.clear();
        dLastServiced.clear();
        wName.clear();
        wCompAddress.clear();
        wExpirationDate.clear();
        vType.setValue(null);
        fType.setValue(null);
        cByWarranty.setValue(null);
        hiddenWarranty();
    }

    public void ResetColumns()
    {

    }

}
