package controllers;

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

import java.text.SimpleDateFormat;
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
    public TableColumn<Vehicle, VehicleType> tVT;
    public TableColumn<Vehicle, String> tMod;
    public TableColumn<Vehicle, String> tManu;
    public TableColumn<Vehicle, Double> tEs;
    public TableColumn<Vehicle, FuelType> tFT;
    public TableColumn<Vehicle, String> tCol;
    public TableColumn<Vehicle, Integer> tMil;
    public TableColumn<Vehicle, Date> tMOT;
    public TableColumn<Vehicle, Date> tDLS;
    public TableColumn<Vehicle, Boolean> tW;
    public TableColumn<Vehicle, String> tWn;
    public TableColumn<Vehicle, String> tA;
    public TableColumn<Vehicle, Date> tD;
    final ObservableList<Vehicle> tableEntries = FXCollections.observableArrayList();

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
        String addOrEdit;
        if (eReg != null) {
            addOrEdit = "edit";
            // true is edit
        } else {
            addOrEdit = "add";
            // false is add
        }
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
            if ((!showAlertC("Sure you want to delete this Vehicle"))) {
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
    public void VehicleEditS() throws Exception {
        try {
            Vehicle vehicle = vSys.searchAVehicle(eReg.getText());
            if (vehicle == null) {
                showAlert("Registration Number invalid");
            } else {
                showAlert("Vehicle Found!");
                setVehicleDets(vehicle);
            }
        } catch (Exception e) {
            showAlert("Registration Number invalid");
            System.out.println(e);
        }

    }


    public void setVehicleDets(Vehicle vehicle) {
        reg.setText(vehicle.getRegNumber());
        reg.setDisable(false);
        cID.setText(Integer.toString(vehicle.getCustomerID()));
        cID.setDisable(false);
        vType.setValue(vehicle.getVehicleType().toString());
        vType.setDisable(false);
        mod.setText(vehicle.getModel());
        mod.setDisable(false);
        manuf.setText(vehicle.getManufacturer());
        manuf.setDisable(false);
        eSize.setText(Double.toString(vehicle.getEngineSize()));
        eSize.setDisable(false);
        String VT = vehicle.getFuelType().toString();
        fType.setValue(VT.substring(0, 1).toUpperCase() + VT.substring(1, VT.length()));
        fType.setDisable(false);
        col.setText(vehicle.getColour());
        col.setDisable(false);
        mil.setText(Integer.toString(vehicle.getMileage()));
        mil.setDisable(false);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        rDateMot.setText(format.format(vehicle.getRenewalDateMot()));
        rDateMot.setDisable(false);
        dLastServiced.setText(format.format(vehicle.getDateLastServiced()));
        dLastServiced.setDisable(false);
        cByWarranty.setDisable(false);
        if (vehicle.isCoveredByWarranty()) {
            cByWarranty.setValue("True");
        } else {
            cByWarranty.setValue("False");
        }
        cByWarranty.setDisable(false);
        wName.setText(vehicle.getWarrantyName());
        wName.setDisable(false);
        wCompAddress.setText(vehicle.getWarrantyCompAddress());
        wCompAddress.setDisable(false);
        wExpirationDate.setText(format.format(vehicle.getWarrantyExpirationDate()));
        wExpirationDate.setDisable(false);
        hiddenWarranty();
    }

    public void searchVehicle() {
        try {
            boolean add = showAlertC("Are you sure you want to search for this Vehicle, have you checked the vehicle details?");
            if (add) {
                List<Vehicle> arrayList = vSys.searchVehicle(reg.getText(), manuf.getText());
                DisplayTable(arrayList);
            }


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

    public void hiddenWarranty() {
        if (cByWarranty.getSelectionModel().getSelectedItem().equals("True") || cByWarranty.getSelectionModel().getSelectedItem().equals("true")) {
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
        }
    }


}
