package controllers.vehicle;

import domain.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.BooleanStringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import logic.booking.BookingSystem;
import logic.customer.CustomerSystem;
import logic.parts.PartsSystem;
import logic.vehicle.VehicleSys;
import persistence.DatabaseRepository;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by DillonVaghela on 2/9/17.
 */
public class VehicleController implements Initializable {


    private VehicleSys vSys = VehicleSys.getInstance();
    @FXML
    private CustomerSystem cSys = CustomerSystem.getInstance();
    @FXML
    private BookingSystem bSys = BookingSystem.getInstance();
    @FXML
    private PartsSystem pSys = PartsSystem.getInstance(DatabaseRepository.getInstance());
    @FXML
    private TextField reg, mod, manuf, eSize, col, mil, wName, wCompAddress, regS, manufS;
    @FXML
    private DatePicker rDateMot, dLastServiced, wExpirationDate;
    @FXML
    private ComboBox vType, fType, cByWarranty, VehicleS, cID, typeS, VehicleTS;
    @FXML
    private TableView<Vehicle> searchTable;
    @FXML
    private TableView<DiagRepBooking> BookingsTable;
    @FXML
    private TableView<Customer> CustomerTable;
    @FXML
    private TableColumn<Vehicle, String> tReg, tMod, tManu, tCol, tWn, tA;
    @FXML
    private TableColumn<Vehicle, Integer> tMil, tCID;
    @FXML
    private TableColumn<DiagRepBooking, ZonedDateTime> rRS, tDS;
    @FXML
    private TableColumn<DiagRepBooking, Double> tTC;
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
    @FXML
    private TableColumn<Customer, Integer> ctID;
    @FXML
    private TableColumn<Customer, String> ctFN, ctSN, ctA, ctP, ctPN, ctE;
    @FXML
    private CheckBox cReg, cCID, cVT, cMod, cManu, cES, cFT, cC, cMil, cMOT, cDLS, cW, cWN, cA, cD, ccID, cFN, cLN, cCA, cCPC, cCP, cCE, cCT;
    @FXML
    private Label AddEditL, SelectedVehicle, RCL, vehicleS, DSL, RSL, RSLL, DSLL, VehiclePartSelected, PartLabel;
    @FXML
    private Button deleteV, ClearV, addV, PartsUsed, newVB, addCustomerB, bookingsB, DeleteVT, EditVehicle, ViewPartsB;
    @FXML
    private ListView ListParts;

    @FXML
    private TextArea custInfo, custInfo2;

    private ObservableList tableEntries = FXCollections.observableArrayList(), tableEntriesB = FXCollections.observableArrayList(), comboEntriesC = FXCollections.observableArrayList();
    private Callback<DatePicker, DateCell> motDayFactory = dp1 -> new DateCell() {
        @Override
        public void updateItem(LocalDate item, boolean empty) {

            // Must call super
            super.updateItem(item, empty);
            if (item.isBefore(LocalDate.now())) {
                this.setStyle(" -fx-background-color: #ff6666; ");
                this.setDisable(true);
            }
        }
    };
    private Callback<DatePicker, DateCell> dlsDayFactory = dp2 -> new DateCell() {
        @Override
        public void updateItem(LocalDate item, boolean empty) {

            // Must call super
            super.updateItem(item, empty);
            if (item.isAfter(LocalDate.now())) {
                this.setStyle(" -fx-background-color: #ff6666; ");
                this.setDisable(true);
            }
        }
    };
    private Callback<DatePicker, DateCell> weDayFactory = dp -> new DateCell() {
        @Override
        public void updateItem(LocalDate item, boolean empty) {

            // Must call super
            super.updateItem(item, empty);
            if (item.isBefore(LocalDate.now())) {
                this.setStyle(" -fx-background-color: #ff6666; ");
                this.setDisable(true);
            }
        }
    };

    private Date fromLocalDate(java.time.LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

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
        }
        else {
            return false;
        }
    }

    public boolean checkFields() {
        boolean check = false;

        if ((!reg.getText().equals("")) && (!cID.getSelectionModel().getSelectedItem().toString().equals("")) && (!vType.getSelectionModel().getSelectedItem().toString().equals("")) && (!mod.getText().equals("")) && (!manuf.getText().equals("")) && (!eSize.getText().equals("")) && (!fType.getSelectionModel().getSelectedItem().toString().equals("")) && (!col.getText().equals("")) && (!mil.getText().equals("")) && (!rDateMot.getValue().equals("")) && (!dLastServiced.getValue().equals("")) && (!cByWarranty.getSelectionModel().getSelectedItem().toString().equals(""))) {
            check = true;
            if (cByWarranty.getSelectionModel().getSelectedItem().toString().equals("True")) {
                if (wName.getText().equals("") || wCompAddress.getText().equals("") || wExpirationDate.getValue().equals("")) {
                    check = false;
                }
            }
        }
        return check;
    }

    public void addEditVehicle() {
        if (!CheckFormat()) {
            return;
        }
        String addOrEdit = "";
        if (AddEditL.getText().equals("Edit Vehicle")) {
            addOrEdit = "edit";
            // true is edit
        }
        else {
            addOrEdit = "add";
            // false is add
        }
        try {
            boolean check = checkFields();
            if (check) {
                Date rdm = fromLocalDate(rDateMot.getValue());
                Date dls = fromLocalDate(dLastServiced.getValue());
                Date wed = new Date();
                if ((!(wExpirationDate.getValue() == null))) {
                    wed = fromLocalDate(wExpirationDate.getValue());
                }
                else {
                    //showAlert("error");
                    wed = null;
                }
                VehicleType vT;
                if (vType.getSelectionModel().getSelectedItem().toString().equals("Car")) {
                    vT = VehicleType.Car;
                }
                else if (vType.getSelectionModel().getSelectedItem().toString().equals("Van")) {
                    vT = VehicleType.Van;
                }
                else {
                    vT = VehicleType.Truck;
                }
                FuelType fT;
                if (fType.getSelectionModel().getSelectedItem().toString().equals("Diesel")) {
                    fT = FuelType.diesel;
                }
                else {
                    fT = FuelType.petrol;
                }
                Boolean W;
                if (cByWarranty.getSelectionModel().getSelectedItem().toString().equals("True")) {
                    W = true;
                }
                else {
                    W = false;
                    wName.setText("");
                    wCompAddress.setText("");
                    //wExpirationDate = null;
                }
                if (addOrEdit.equals("add")) {
                    try {
                        Vehicle vehicle = vSys.searchAVehicle(reg.getText());
                        if (vehicle != null) {
                            showAlert("Cant add this vehicle as Registrations exists");
                            return;
                        }
                    }
                    catch (Exception e) {

                    }

                }
                int customerID = Character.getNumericValue(cID.getSelectionModel().getSelectedItem().toString().charAt(0));
                boolean add = showAlertC("Are you sure you want to " + addOrEdit + " this Vehicle, have you checked the vehicle details?");
                if (add) {
                    boolean checker = vSys.addEditVehicle(reg.getText(), customerID, vT, mod.getText(), manuf.getText(), Double.parseDouble(eSize.getText()), fT, col.getText(), Integer.parseInt(mil.getText()), rdm, dls, W, wName.getText(), wCompAddress.getText(), wed);
                    showAlert("vehicle " + addOrEdit + ": " + Boolean.toString(checker));
                    if (checker) {
                        searchVehicleA();
                        ClearFields();
                        if (addOrEdit.equals("edit")) {
                            newVehicle();
                        }
                    }
                }
            }
            else {
                showAlert("Can't " + addOrEdit + " as all fields required are incomplete");
            }
        }
        catch (Exception e) {
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
                searchVehicleA();
            }
            else {
                showAlert("Cant find Reg Plate");
            }
        }
        catch (Exception e) {
            System.out.println("Vehicle delete error");
            e.printStackTrace();
        }
    }

    @FXML
    public void VehicleEditS() {
        try {
            reg.setEditable(false);
            Vehicle vehicle = searchTable.getSelectionModel().getSelectedItem();
            if (vehicle == null) {
                throw new Exception();
            }
            setVehicleDets(vehicle);
        }
        catch (Exception e) {
            showAlert("No Vehicle Selected");
            //System.out.println(e);
            //e.printStackTrace();
        }

    }

    public void DeleteSelectedVehicle() {
        try {
            Vehicle vehicle = searchTable.getSelectionModel().getSelectedItem();
            if (vehicle == null) {
                throw new Exception();
            }
            System.out.println(vehicle.getVehicleRegNumber());
            if ((!showAlertC("Sure you want to delete this Vehicle?"))) {
                return;
            }
            boolean check = vSys.deleteVehicle(vehicle.getVehicleRegNumber());
            if (check) {
                showAlert("Vehicle Found and Deleted: " + Boolean.toString(check));
                searchVehicleA();
            }
            else {
                showAlert("Not deleted");
            }
        }
        catch (Exception e) {
            showAlert("no Vehicle Selected");
            //e.printStackTrace();
        }
    }

    public void setVehicleDets(Vehicle vehicle) {
        AddEditL.setText("Edit Vehicle");
        reg.setText(vehicle.getVehicleRegNumber());
        addV.setText("Edit");
        VehicleS.setDisable(true);
        deleteV.setDisable(false);
        newVB.setDisable(false);
        ClearV.setDisable(true);
        reg.setText(vehicle.getVehicleRegNumber());
        cID.setValue(Integer.toString(vehicle.getCustomerID()));
        vType.setValue(vehicle.getVehicleType().toString());
        mod.setText(vehicle.getModel());
        manuf.setText(vehicle.getManufacturer());
        eSize.setText(Double.toString(vehicle.getEngineSize()));
        String VT = vehicle.getFuelType().toString();
        fType.setValue(VT.substring(0, 1).toUpperCase() + VT.substring(1, VT.length()));
        col.setText(vehicle.getColour());
        mil.setText(Integer.toString(vehicle.getMileage()));
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        rDateMot.setValue(LocalDate.parse(df.format(vehicle.getRenewalDateMot()), formatter));
        rDateMot.setConverter(SC());

        dLastServiced.setValue(LocalDate.parse(df.format(vehicle.getDateLastServiced()), formatter));
        dLastServiced.setConverter(SC());
        if (vehicle.isCoveredByWarranty()) {
            cByWarranty.setValue("True");
        }
        else {
            cByWarranty.setValue("False");
        }
        wName.setText(vehicle.getWarrantyName());
        wCompAddress.setText(vehicle.getWarrantyCompAddress());
        if (vehicle.isCoveredByWarranty()) {
            wExpirationDate.setValue(LocalDate.parse(df.format(vehicle.getWarrantyExpirationDate()), formatter));
            wExpirationDate.setConverter(SC());
        }
        hiddenWarranty();

    }

    public StringConverter<LocalDate> SC() {
        StringConverter<LocalDate> SC = new StringConverter<LocalDate>() {
            private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            @Override
            public String toString(LocalDate localDate) {
                if (localDate == null)
                    return "";
                return dateTimeFormatter.format(localDate);
            }

            @Override
            public LocalDate fromString(String dateString) {
                if (dateString == null || dateString.trim().isEmpty()) {
                    return null;
                }
                return LocalDate.parse(dateString, dateTimeFormatter);
            }
        };
        return SC;
    }

    public void searchVehicle() {
        searchTable.setDisable(false);
        try {
            VehicleType vT;
            List<Vehicle> arrayList;
            if (typeS.getSelectionModel().getSelectedItem() != null) {
                if (typeS.getSelectionModel().getSelectedItem().toString().equals("Car")) {
                    vT = VehicleType.Car;
                }
                else if (typeS.getSelectionModel().getSelectedItem().toString().equals("Van")) {
                    vT = VehicleType.Van;
                }
                else {
                    vT = VehicleType.Truck;
                }
                arrayList = vSys.searchVehicleT(regS.getText(), manufS.getText(), vT);
            }
            else {
                arrayList = vSys.searchVehicle(regS.getText(), manufS.getText());
            }
            DisplayTable(arrayList);

        }
        catch (Exception e) {
            System.out.println("Search Vehicle Error");
            e.printStackTrace();
        }
    }

    public void searchVehicleA() {
        List<Vehicle> arrayList = vSys.getVehiclesList();
        regS.setText("");
        manufS.setText("");
        typeS.setValue(null);
        VehicleTS.setValue(null);
        DisplayTable(arrayList);
    }

    public void DisplayTable(List<Vehicle> arrayList) {
        try {
            DeleteVT.setDisable(false);
            EditVehicle.setDisable(false);
            searchTable.setDisable(false);
            tableEntries.removeAll(tableEntries);
            ArrayList<Boolean> b = new ArrayList<Boolean>();
            for (int i = 0; i < arrayList.size(); i++) {
                if (arrayList.get(i).isCoveredByWarranty()) {
                    b.add(true);
                }
                else {
                    b.add(false);
                }
                tableEntries.add(arrayList.get(i));
            }


            tReg.setCellValueFactory(new PropertyValueFactory<Vehicle, String>("vehicleRegNumber"));
            tReg.setCellFactory(TextFieldTableCell.<Vehicle>forTableColumn());
            tReg.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Vehicle, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Vehicle, String> event) {
                    (event.getTableView().getItems().get(event.getTablePosition().getRow())).setVehicleRegNumber(event.getNewValue());
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
                    if (string.equals("car")) {
                        return VehicleType.Car;
                    }
                    else if (string.equals("Truck")) {
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
                    }
                    else {
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
                    if (string.equals("diesel")) {
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
            tMOT.setCellFactory(TextFieldTableCell.<Vehicle, Date>forTableColumn(new StringConverter<Date>() {
                @Override
                public String toString(Date object) {
                    if (object == null) {
                        return "n/a";
                    }
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                    return format.format(object);
                }

                @Override
                public Date fromString(String string) {
                    return new Date(string);
                }
            }));
            tMOT.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Vehicle, Date>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Vehicle, Date> event) {
                    (event.getTableView().getItems().get(event.getTablePosition().getRow())).setRenewalDateMot(event.getNewValue());
                }
            });

            tDLS.setCellValueFactory(new PropertyValueFactory<Vehicle, Date>("dateLastServiced"));
            tDLS.setCellFactory(TextFieldTableCell.<Vehicle, Date>forTableColumn(new StringConverter<Date>() {
                @Override
                public String toString(Date object) {
                    if (object == null) {
                        return "n/a";
                    }
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                    return format.format(object);
                }

                @Override
                public Date fromString(String string) {
                    return new Date(string);
                }
            }));
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
            tD.setCellFactory(TextFieldTableCell.<Vehicle, Date>forTableColumn(new StringConverter<Date>() {
                @Override
                public String toString(Date object) {
                    if (object == null) {
                        return null;
                    }
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                    return format.format(object);
                }

                @Override
                public Date fromString(String string) {
                    return new Date(string);
                }
            }));
            tD.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Vehicle, Date>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Vehicle, Date> event) {
                    (event.getTableView().getItems().get(event.getTablePosition().getRow())).setWarrantyExpirationDate(event.getNewValue());
                }
            });

            searchTable.setItems(tableEntries);
        }
        catch (Exception e) {
            System.out.println("not working");
            e.printStackTrace();
            System.out.println(e);
        }

    }

    public void hiddenWarranty() {
        try {


            if (cByWarranty.getSelectionModel().getSelectedItem().toString().equals("True") || cByWarranty.getSelectionModel().getSelectedItem().equals(null)) {
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
                wExpirationDate.setValue(null);
            }
        }
        catch (Exception e) {
            wName.setDisable(false);
            wCompAddress.setDisable(false);
            wExpirationDate.setDisable(false);
        }
    }

    public void changeColumns() {
        if (cReg.isSelected()) {
            tReg.setVisible(true);
        }
        else {
            tReg.setVisible(false);
        }
        if (cCID.isSelected()) {
            tCID.setVisible(true);
        }
        else {
            tCID.setVisible(false);
        }
        if (cVT.isSelected()) {
            tVT.setVisible(true);
        }
        else {
            tVT.setVisible(false);
        }
        if (cMod.isSelected()) {
            tMod.setVisible(true);
        }
        else {
            tMod.setVisible(false);
        }
        if (cManu.isSelected()) {
            tManu.setVisible(true);
        }
        else {
            tManu.setVisible(false);
        }
        if (cES.isSelected()) {
            tEs.setVisible(true);
        }
        else {
            tEs.setVisible(false);
        }
        if (cFT.isSelected()) {
            tFT.setVisible(true);
        }
        else {
            tFT.setVisible(false);
        }
        if (cC.isSelected()) {
            tCol.setVisible(true);
        }
        else {
            tCol.setVisible(false);
        }
        if (cMil.isSelected()) {
            tMil.setVisible(true);
        }
        else {
            tMil.setVisible(false);
        }
        if (cMOT.isSelected()) {
            tMOT.setVisible(true);
        }
        else {
            tMOT.setVisible(false);
        }
        if (cDLS.isSelected()) {
            tDLS.setVisible(true);
        }
        else {
            tDLS.setVisible(false);
        }
        if (cW.isSelected()) {
            tW.setVisible(true);
        }
        else {
            tW.setVisible(false);
        }
        if (cWN.isSelected()) {
            tWn.setVisible(true);
        }
        else {
            tWn.setVisible(false);
        }
        if (cA.isSelected()) {
            tA.setVisible(true);
        }
        else {
            tA.setVisible(false);
        }
        if (cD.isSelected()) {
            tD.setVisible(true);
        }
        else {
            tD.setVisible(false);
        }
        if (ccID.isSelected()) {
            ctID.setVisible(true);
        }
        else {
            ctID.setVisible(false);
        }
        if (cFN.isSelected()) {
            ctFN.setVisible(true);
        }
        else {
            ctFN.setVisible(false);
        }
        if (cLN.isSelected()) {
            ctSN.setVisible(true);
        }
        else {
            ctSN.setVisible(false);
        }
        if (cCA.isSelected()) {
            ctA.setVisible(true);
        }
        else {
            ctA.setVisible(false);
        }
        if (cCPC.isSelected()) {
            ctP.setVisible(true);
        }
        if (cCP.isSelected()) {
            ctPN.setVisible(true);
        }
        else {
            ctPN.setVisible(false);
        }
        if (cCE.isSelected()) {
            ctE.setVisible(true);
        }
        else {
            ctE.setVisible(false);
        }


    }

    public void selectVehicle() {
        if (VehicleS.getSelectionModel().isSelected(0)) {
            setVehicle("Civic", "Honda", "1.6", "Car", "Petrol");
        }
        else if (VehicleS.getSelectionModel().isSelected(1)) {
            setVehicle("Focus", "Ford", "1.2", "Car", "Diesel");
        }
        else if (VehicleS.getSelectionModel().isSelected(2)) {
            setVehicle("5 Series", "BMw", "2.2", "Car", "Petrol");
        }
        else if (VehicleS.getSelectionModel().isSelected(3)) {
            setVehicle("3 Series", "BMw", "2.9", "Car", "Diesel");
        }
        else if (VehicleS.getSelectionModel().isSelected(4)) {
            setVehicle("A Class", "Mercedes", "3.0", "Car", "Petrol");
        }
        else if (VehicleS.getSelectionModel().isSelected(5)) {
            setVehicle("Transit", "Ford", "2.2", "Van", "Petrol");
        }
        else if (VehicleS.getSelectionModel().isSelected(6)) {
            setVehicle("Roadster", "Nissan", "1.2", "Truck", "Diesel");
        }
        else if (VehicleS.getSelectionModel().isSelected(7)) {
            setVehicle("Y-8 Van", "Audi", "3.6", "Van", "Petrol");
        }
        else if (VehicleS.getSelectionModel().isSelected(8)) {
            setVehicle("Enzo", "Ferrari", "4.4", "Car", "Petrol");
        }
        else if (VehicleS.getSelectionModel().isSelected(9)) {
            setVehicle("Truckster", "Ford", "2.8", "Truck", "Diesel");
        }
        else if (VehicleS.getSelectionModel().isSelected(10)) {
            setVehicle("Hybrid Van", "Renault", "2.3", "Can", "Petrol");
        }
        else if (VehicleS.getSelectionModel().isSelected(11)) {
            setVehicle("Sport", "MG", "2.0", "Car", "Diesel");
        }
        else if (VehicleS.getSelectionModel().isSelected(12)) {
            setVehicle("Model S", "Acura", "2.2", "Car", "Petrol");
        }
        else if (VehicleS.getSelectionModel().isSelected(13)) {
            setVehicle("Arnage", "Bentley", "4.0", "Car", "Petrol");
        }
        else {
            setVehicle("Fire Truck", "DAF", "3.8", "Truck", "Diesel");
        }
    }

    public void selectVehicleS() {
        List<Vehicle> arrayList;
        if (VehicleTS.getSelectionModel().isSelected(0)) {
            arrayList = vSys.searchByTemplate("Civic", "Honda", 1.6, VehicleType.Car, FuelType.petrol);
        }
        else if (VehicleTS.getSelectionModel().isSelected(1)) {
            arrayList = vSys.searchByTemplate("Focus", "Ford", 1.2, VehicleType.Car, FuelType.diesel);
        }
        else if (VehicleTS.getSelectionModel().isSelected(2)) {
            arrayList = vSys.searchByTemplate("5 Series", "BMw", 2.2, VehicleType.Car, FuelType.petrol);
        }
        else if (VehicleTS.getSelectionModel().isSelected(3)) {
            arrayList = vSys.searchByTemplate("3 Series", "BMw", 2.9, VehicleType.Car, FuelType.diesel);
        }
        else if (VehicleTS.getSelectionModel().isSelected(4)) {
            arrayList = vSys.searchByTemplate("A Class", "Mercedes", 3.0, VehicleType.Car, FuelType.petrol);
        }
        else if (VehicleTS.getSelectionModel().isSelected(5)) {
            arrayList = vSys.searchByTemplate("Transit", "Ford", 2.2, VehicleType.Van, FuelType.petrol);
        }
        else if (VehicleTS.getSelectionModel().isSelected(6)) {
            arrayList = vSys.searchByTemplate("Roadster", "Nissan", 1.2, VehicleType.Truck, FuelType.diesel);
        }
        else if (VehicleTS.getSelectionModel().isSelected(7)) {
            arrayList = vSys.searchByTemplate("Y-8 Van", "Audi", 3.6, VehicleType.Van, FuelType.petrol);
        }
        else if (VehicleTS.getSelectionModel().isSelected(8)) {
            arrayList = vSys.searchByTemplate("Enzo", "Ferrari", 4.4, VehicleType.Car, FuelType.petrol);
        }
        else if (VehicleTS.getSelectionModel().isSelected(9)) {
            arrayList = vSys.searchByTemplate("Truckster", "Ford", 2.8, VehicleType.Truck, FuelType.diesel);
        }
        else if (VehicleTS.getSelectionModel().isSelected(10)) {
            arrayList = vSys.searchByTemplate("Hybrid Van", "Renault", 2.3, VehicleType.Car, FuelType.petrol);
        }
        else if (VehicleTS.getSelectionModel().isSelected(11)) {
            arrayList = vSys.searchByTemplate("Sport", "MG", 2.0, VehicleType.Car, FuelType.diesel);
        }
        else if (VehicleTS.getSelectionModel().isSelected(12)) {
            arrayList = vSys.searchByTemplate("Model S", "Acura", 2.2, VehicleType.Car, FuelType.petrol);
        }
        else if (VehicleTS.getSelectionModel().isSelected(13)) {
            arrayList = vSys.searchByTemplate("Arnage", "Bentley", 4.0, VehicleType.Car, FuelType.petrol);
        }
        else {
            arrayList = vSys.searchByTemplate("Fire Truck", "DAF", 3.8, VehicleType.Truck, FuelType.diesel);
        }
        DisplayTable(arrayList);
    }

    public void setVehicle(String model, String manufacturer, String engineSize, String vehicleType, String fuelType) {
        mod.setText(model);
        manuf.setText(manufacturer);
        eSize.setText(engineSize);
        vType.setValue(vehicleType);
        fType.setValue(fuelType);
    }

    public void ClearFields() {
        reg.clear();
        cID.setValue(null);
        mod.clear();
        manuf.clear();
        eSize.clear();
        col.clear();
        mil.clear();
        rDateMot.setValue(null);
        dLastServiced.setValue(null);
        wName.clear();
        wCompAddress.clear();
        if (wExpirationDate != null) {
            wExpirationDate.setValue(null);
        }
        vType.setValue(null);
        fType.setValue(null);
        cByWarranty.setValue(null);
        hiddenWarranty();
    }

    public void ResetColumns() {
        cReg.setSelected(true);
        cVT.setSelected(true);
        cCID.setSelected(true);
        cVT.setSelected(true);
        cMod.setSelected(true);
        cManu.setSelected(true);
        cES.setSelected(false);
        cFT.setSelected(false);
        cC.setSelected(false);
        cMil.setSelected(false);
        cMOT.setSelected(true);
        cDLS.setSelected(true);
        cW.setSelected(true);
        cWN.setSelected(true);
        cA.setSelected(true);
        cD.setSelected(true);
        tReg.setVisible(true);
        tMod.setVisible(true);
        tManu.setVisible(true);
        tCol.setVisible(false);
        tWn.setVisible(true);
        tA.setVisible(true);
        tMil.setVisible(false);
        tVT.setVisible(true);
        tEs.setVisible(false);
        tFT.setVisible(false);
        tCID.setVisible(true);
        tMOT.setVisible(true);
        tDLS.setVisible(true);
        tD.setVisible(true);
        tW.setVisible(true);

    }

    public void ViewBookingDates() {
        Vehicle vehicle = ((Vehicle) searchTable.getSelectionModel().getSelectedItem());
        DateTimeFormatter dTFM = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        PartsUsed.setDisable(false);
        try {
            BookingsTable.setDisable(false);
            tableEntriesB.removeAll(tableEntriesB);
            List<DiagRepBooking> arrayList = bSys.getVehicleBookings(vehicle.getVehicleRegNumber());
            if (arrayList.size() == 0) {
                //showAlert("No Bookings");
                PartsUsed.setDisable(true);
                return;
            }
            for (int i = 0; i < arrayList.size(); i++) {

                tableEntriesB.add(arrayList.get(i));
            }


            tDS.setCellValueFactory(new PropertyValueFactory<DiagRepBooking, ZonedDateTime>("diagnosisStart"));
            tDS.setCellFactory(TextFieldTableCell.<DiagRepBooking, ZonedDateTime>forTableColumn(new StringConverter<ZonedDateTime>() {
                @Override
                public String toString(ZonedDateTime object) {
                    if (object == null) {
                        return null;
                    }
                    return object.format(dTFM);
                }

                @Override
                public ZonedDateTime fromString(String string) {
                    return ZonedDateTime.parse(string);
                }
            }));

            rRS.setCellValueFactory(new PropertyValueFactory<DiagRepBooking, ZonedDateTime>("repairStart"));
            rRS.setCellFactory(TextFieldTableCell.<DiagRepBooking, ZonedDateTime>forTableColumn(new StringConverter<ZonedDateTime>() {
                @Override
                public String toString(ZonedDateTime object) {
                    if (object == null) {
                        return null;
                    }
                    return object.format(dTFM);
                }

                @Override
                public ZonedDateTime fromString(String string) {
                    return ZonedDateTime.parse(string);
                }
            }));

            tTC.setCellValueFactory(new PropertyValueFactory<DiagRepBooking, Double>("billAmount"));
            tTC.setCellFactory(TextFieldTableCell.<DiagRepBooking, Double>forTableColumn(new DoubleStringConverter()));

            BookingsTable.setItems(tableEntriesB);
        }
        catch (Exception e) {
            System.out.println("not working");
            e.printStackTrace();
            System.out.println(e);
        }

    }

    public void ShowParts() {
        try {
            DiagRepBooking DRB = BookingsTable.getSelectionModel().getSelectedItem();
            List<PartOccurrence> parts = DRB.getRequiredPartsList();
            if (parts.size() == 0 || parts == null) {
                showAlert("No parts for this Booking");
                return;
            }
            ObservableList<String> items = FXCollections.observableArrayList();
            for (int i = 0; i < parts.size(); i++) {
                PartOccurrence PO = parts.get(i);
                PartAbstraction PA = pSys.getPartbyID(PO.getPartAbstractionID());
                items.add(PA.getPartName());
            }
            String date;
            if (DRB.getDiagnosisStart() == null) {
                date = null;
            }
            else {
                date = DRB.getDiagnosisStart().toString();
            }
            PartLabel.setText("Booking DiagStart: " + date);
            PartLabel.setVisible(true);
            ListParts.setItems(items);
        }
        catch (Exception e) {
            showAlert("No Booking selected");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void ShowCustomerDetails() {
        if (searchTable.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        try {
            setNextBookingDate();
            ViewBookingDates();
            Vehicle vehicle = searchTable.getSelectionModel().getSelectedItem();
            if (cSys.getACustomers(vehicle.getCustomerID()) == null) {
                return;
            }
            Customer customer = cSys.getACustomers(vehicle.getCustomerID());
            custInfo.clear();
            custInfo2.clear();
            custInfo.appendText("First name : " + customer.getCustomerFirstname() + "\n");
            custInfo.appendText("Surname : " + customer.getCustomerSurname() + "\n");
            custInfo.appendText("Phone number : " + customer.getCustomerPhone());
            custInfo2.appendText("Address : " + customer.getCustomerAddress() + "\n");
            custInfo2.appendText("Postcode : " + customer.getCustomerPostcode() + "\n");
            custInfo2.appendText("Type : " + customer.getCustomerType().toString());
            custInfo.setEditable(false);
            custInfo2.setEditable(false);

        }
        catch (Exception e) {
            showAlert("no Vehicle Selected");
            System.out.println(e);
            e.printStackTrace();
        }
    }

    public void ResetCustomerColumns() {
        ccID.setSelected(false);
        cFN.setSelected(true);
        cLN.setSelected(true);
        cCA.setSelected(true);
        cCPC.setSelected(true);
        cCP.setSelected(true);
        cCE.setSelected(true);
        cCT.setSelected(true);
        ctID.setVisible(false);
        ctFN.setVisible(true);
        ctSN.setVisible(true);
        ctA.setVisible(true);
        ctP.setVisible(true);
        ctPN.setVisible(true);
        ctE.setVisible(true);
        //ctT.setVisible(true);
    }

    public void newVehicle() {
        reg.setDisable(false);
        deleteV.setDisable(true);
        AddEditL.setText("Add Vehicle");
        VehicleS.setDisable(false);
        newVB.setDisable(true);
        addV.setText("add");
        ClearV.setDisable(false);
        ClearFields();
    }

    public void setNextBookingDate() {
        try {
            VehicleParts();
            Vehicle vehicle = ((Vehicle) searchTable.getSelectionModel().getSelectedItem());
            List<DiagRepBooking> arrayList = bSys.getVehicleBookings(vehicle.getVehicleRegNumber());
            if (arrayList.size() == 0) {
                return;
            }
            ZonedDateTime nextDiagBooking = arrayList.get(0).getDiagnosisStart();
            ZonedDateTime nextRepBooking = arrayList.get(0).getRepairStart();
            ZonedDateTime DTD;
            ZonedDateTime DTR;
            for (int i = 1; i < arrayList.size(); i++) {
                DiagRepBooking DRB = arrayList.get(i);
                DTD = DRB.getDiagnosisStart();
                DTR = DRB.getRepairStart();
                if (DTD != null) {
                    if (DTD.isAfter(ZonedDateTime.now())) {
                        if ((DTD.isBefore(nextDiagBooking))) {
                            nextDiagBooking = DTD;
                        }
                    }
                }
                if (DTR != null) {
                    if (DTR.isAfter(ZonedDateTime.now())) {
                        if ((DTR.isBefore(nextRepBooking))) {
                            nextRepBooking = DTR;
                        }
                    }
                }
            }
            DateTimeFormatter dTFM = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm");
            if (nextDiagBooking != null) {
                if (nextDiagBooking.isAfter(ZonedDateTime.now())) {
                    DSLL.setVisible(true);
                    DSL.setText(nextDiagBooking.format(dTFM));
                }
            }
            else {
                DSLL.setVisible(true);
                DSL.setText("N/A");
            }
            if (nextRepBooking != null) {
                if (nextRepBooking.isAfter(ZonedDateTime.now())) {
                    RSLL.setVisible(true);
                    RSL.setVisible(true);
                    RSL.setText(nextRepBooking.format(dTFM));
                }
            }
            else {
                RSLL.setVisible(true);
                RSL.setVisible(true);
                RSL.setText("N/A");
            }
        }
        catch (Exception e) {
            showAlert("No Bookings");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void VehicleParts() {
        try {
            Vehicle vehicle = ((Vehicle) searchTable.getSelectionModel().getSelectedItem());
            List<Installation> Installations = vehicle.getInstallationList();
            if (Installations.size() == 0 || Installations == null) {
                //showAlert("No parts installed for this Vehicle");
                PartLabel.setText("No parts");
                PartLabel.setVisible(true);
                ListParts.setItems(null);
                //showAlert("No parts");
                return;
            }
            ObservableList<String> items = FXCollections.observableArrayList();
            for (int i = 0; i < Installations.size(); i++) {
                Installation A = Installations.get(i);
                PartAbstraction PA = A.getPartAbstraction();
                items.add(PA.getPartName());
            }
            PartLabel.setText("Vehicle Reg: " + vehicle.getVehicleRegNumber());
            PartLabel.setVisible(true);
            ListParts.setItems(items);
        }
        catch (Exception e) {
            //showAlert("error");
            //e.printStackTrace();
        }
    }

    public boolean CheckFormat() {
        try {
            Date date = fromLocalDate(dLastServiced.getValue());
            date = fromLocalDate(rDateMot.getValue());
            if (cByWarranty != null) {
                if (cByWarranty.getSelectionModel().getSelectedItem().toString().equals("True") || cByWarranty.getSelectionModel().getSelectedItem().equals(null)) {
                    date = fromLocalDate(wExpirationDate.getValue());
                }
            }

            Double engineSize = Double.parseDouble(eSize.getText());
            int mileage = Integer.parseInt(mil.getText());
            if (mileage <0 || engineSize <=0)
            {
                showAlert("Check Number inputs are above 0");
                return false;
            }
            if (col.getText().matches(".*\\d+.*") || reg.getText().matches(".*\\d+.*") || manuf.getText().matches(".*\\d+.*") || mod.getText().matches(".*\\d+.*") || wName.getText().matches(".*\\d+.*") || wCompAddress.getText().matches(".*\\d+.*")) {
                showAlert("Fields must be a string");
                return false;
            }

            if (vType == null || fType == null) {
                showAlert("Pick fuel type and vehicle Type");
                return false;
            }
            return true;
        }
        catch (Exception e) {
            showAlert(e.getMessage() + " and make sure all fields required are not blank");
            return false;
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setCustomerCombo();
        searchVehicleA();
        rDateMot.setDayCellFactory(motDayFactory);
        dLastServiced.setDayCellFactory(dlsDayFactory);
        wExpirationDate.setDayCellFactory(weDayFactory);
        custInfo.setEditable(false);
    }

    public void setCustomerCombo() {
        List<Customer> list = cSys.getAllCustomers();
        for (int i = 0; i < list.size(); i++) {
            Customer customer = list.get(i);
            comboEntriesC.add(customer.getCustomerID() + " " + customer.getCustomerFirstname() + " " + customer.getCustomerSurname());
        }
        cID.setItems(comboEntriesC);
    }
}
