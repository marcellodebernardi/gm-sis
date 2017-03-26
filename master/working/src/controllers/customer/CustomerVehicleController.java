package controllers.customer;

import controllers.booking.BookingController;
import controllers.common.MenuController;
import domain.Customer;
import domain.FuelType;
import domain.Vehicle;
import domain.VehicleType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogEvent;
import javafx.util.Callback;
import javafx.util.StringConverter;
import logic.booking.BookingSystem;
import logic.customer.CustomerSystem;
import logic.vehicle.VehicleSys;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

//import logic.*;

/**
 * Created by EBUBECHUKWU on 21/03/2017.
 */

public class CustomerVehicleController implements Initializable {
    private static CustomerVehicleController instance;
    final ObservableList DisplayTable = FXCollections.observableArrayList();
    @FXML
    public CustomerSystem cSystem = CustomerSystem.getInstance();
    ////for 'AddCustomerVehiclePopupView.fxml' instance variables
    Stage addVehicleStage;
    private VehicleSys vSys = VehicleSys.getInstance();
    private BookingSystem bSystem = BookingSystem.getInstance();
    @FXML
    private Label addVehicleLabel = new Label();
    @FXML
    //private TextField cvRegistrationNumber, cvManufacturer, cvModel, cvEngineSize, cvColour, cvMileage, cvCompanyName, cvCompanyAddress = new TextField();
    private TextField cID, reg, mod, manuf, eSize, col, mil, wName, wCompAddress, regS, manufS = new TextField();
    @FXML
    //private ComboBox cvSelectVehicle, cvCustomerID, cvVehicleType, cvFuelType, cvWarranty = new ComboBox();
    private ComboBox vType, fType, cByWarranty, VehicleS, typeS, VehicleTS = new ComboBox();
    @FXML
    //private DatePicker cvMOTRenewalDate, cvDateLastServiced, CVExpiationDate = new DatePicker();
    private DatePicker rDateMot, dLastServiced, wExpirationDate = new DatePicker();
    @FXML
    private Button addV, clearV = new Button();
    @FXML
    private Button cvSaveVehicleButton, cvClearVehicleFieldsButton, cvSaveAndMakeBookingButton = new Button();
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

    public static CustomerVehicleController getInstance() {
        if (instance == null) instance = new CustomerVehicleController();
        return instance;
    }

    public Stage showVehiclePopup() throws Exception {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/customer/AddCustomerVehiclePopupView.fxml"));
            Parent menu = fxmlLoader.load();
            addVehicleStage = new Stage();
            addVehicleStage.setTitle("Add Vehicle");
            addVehicleStage.setScene(new Scene(menu));
            addVehicleStage.initModality(Modality.APPLICATION_MODAL);
            addVehicleStage.showAndWait();

            List<Customer> customerList = cSystem.getAllCustomers();
            int tempCustomerID = 0;
            for(int i=customerList.size()-1; i>=0; i++)
            {
                tempCustomerID = customerList.get(i).getCustomerID();
                break;
            }
            List<Vehicle> vehicleList = cSystem.searchCustomerVehicles(tempCustomerID);
            if(vehicleList.size() == 0)
            {
                boolean deleteCustomer = cSystem.deleteCustomer(tempCustomerID);
                noVehicleAdded();
                MenuController.getInstance().reopenCustomerTab();
            }
        }
        catch (IOException e) {
            System.out.println("Show Vehicle Popup Error");
            e.printStackTrace();
        }
        return addVehicleStage;
    }

    public void noVehicleAdded() throws Exception {
        Dialog<Void> dialog = new Dialog<>();
        CheckBox preventClosing = new CheckBox("ERROR: Vehicle must be added to new Customer's record\n\nPress OK and add customer again");
        preventClosing.setSelected(true);
        preventClosing.setDisable(true);

        dialog.setOnCloseRequest(new EventHandler<DialogEvent>() {
            @Override
            public void handle(DialogEvent event) {
                if (!preventClosing.isSelected()) {
                    event.consume();
                }
                System.out.println("Consumed event: " + event.isConsumed());
            }
        });
        dialog.getDialogPane().setContent(preventClosing);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.show();
    }

    public boolean addVehicle(ActionEvent event) throws Exception {
        if (!checkVehicleFormat()) {
            return false;
        }

        try {
            boolean check = checkVehicleFields();
            if (check) {
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                Date rdm = fromLocalDate(rDateMot.getValue());
                Date dls = fromLocalDate(dLastServiced.getValue());
                Date wed = new Date();
                if ((!(wExpirationDate.getValue() == null))) {
                    wed = fromLocalDate(wExpirationDate.getValue());
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
                }

                Vehicle vehicle = vSys.searchAVehicle(reg.getText());
                if (vehicle != null) {
                    errorAlert("Cant add this vehicle as Registrations exists");
                    return false;
                }

                int customerID = Integer.parseInt(cID.getText());
                boolean checker = vSys.addEditVehicle(reg.getText().toUpperCase(), customerID, vT, mod.getText(), manuf.getText(), Double.parseDouble(eSize.getText()), fT, col.getText(), Integer.parseInt(mil.getText()), rdm, dls, W, wName.getText(), wCompAddress.getText(), wed);

                if (checker) {
                    Stage stage = null;
                    stage = (Stage) cvSaveVehicleButton.getScene().getWindow();
                    stage.close();
                }
            }
            return true;
        }
        catch (Exception e) {
            System.out.println("Add Customer's Vehicle Error");
            e.printStackTrace();
        }
        return false;
    }

    public void addVehicleAndMakeBooking(ActionEvent event) throws Exception {
        if (!checkVehicleFormat()) {
            return;
        }

        try {
            boolean check = checkVehicleFields();
            boolean checker = false;
            if (check) {
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                Date rdm = fromLocalDate(rDateMot.getValue());
                Date dls = fromLocalDate(dLastServiced.getValue());
                Date wed = new Date();
                if ((!(wExpirationDate.getValue() == null))) {
                    wed = fromLocalDate(wExpirationDate.getValue());
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
                }

                Vehicle vehicle = vSys.searchAVehicle(reg.getText());
                if (vehicle != null) {
                    errorAlert("Cant add this vehicle as Registrations exists");
                    return;
                }

                int customerID = Integer.parseInt(cID.getText());
                checker = vSys.addEditVehicle(reg.getText().toUpperCase(), customerID, vT, mod.getText(), manuf.getText(), Double.parseDouble(eSize.getText()), fT, col.getText(), Integer.parseInt(mil.getText()), rdm, dls, W, wName.getText(), wCompAddress.getText(), wed);

                if (checker) {
                    BookingController.getInstance().show();
                    Stage stage = null;
                    stage = (Stage) cvSaveVehicleButton.getScene().getWindow();
                    stage.close();
                }
            }
        }
        catch (Exception e) {
            System.out.println("Add Customer's Vehicle Error");
            e.printStackTrace();
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

    public void clearVehicleFields() {
        mod.setDisable(false);
        manuf.setDisable(false);
        eSize.setDisable(false);
        vType.setDisable(false);
        fType.setDisable(false);

        reg.clear();
        mod.clear();
        manuf.clear();
        eSize.clear();
        col.clear();
        mil.clear();
        rDateMot.setValue(null);
        dLastServiced.setValue(null);
        wName.clear();
        wCompAddress.clear();
        wExpirationDate.setValue(null);
        vType.setValue(null);
        fType.setValue(null);
        cByWarranty.setValue(null);
        VehicleS.setValue(null);
        hiddenWarranty();


    }

    public boolean checkVehicleFields() {
        boolean check = false;
        if ((!reg.getText().equals("")) && (!cID.equals("")) && (!vType.getSelectionModel().getSelectedItem().toString().equals("")) && (!mod.getText().equals("")) && (!manuf.getText().equals("")) && (!eSize.getText().equals("")) && (!fType.getSelectionModel().getSelectedItem().toString().equals("")) && (!col.getText().equals("")) && (!mil.getText().equals("")) && (!rDateMot.getValue().equals("")) && (!dLastServiced.getValue().equals("")) && (!cByWarranty.getSelectionModel().getSelectedItem().toString().equals(""))) {
            check = true;
            if (cByWarranty.getSelectionModel().getSelectedItem().toString().equals("True")) {
                if (wName.getText().equals("") || wCompAddress.getText().equals("") || wExpirationDate.getValue().equals("")) {
                    check = false;
                }
            }
        }
        else{
            errorAlert("Make sure all fields required are not blank except for Warranty Company \nname, address and expiry date when Warranty 'false' is selected");
        }
        return check;
    }

    public boolean checkVehicleFormat() {
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
            if (col.getText().matches(".*\\d+.*")) {
                errorAlert("Colour must be a string");
                return false;
            }

            if (vType == null || fType == null) {
                errorAlert("Pick fuel type and vehicle Type");
                return false;
            }
            return true;
        }
        catch (Exception e) {
            errorAlert("Make sure all fields required are not blank");
            e.printStackTrace();
            return false;
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
        else if (VehicleS.getSelectionModel().isSelected(14)) {
            setVehicle("Fire Truck", "DAF", "3.8", "Truck", "Diesel");
        }
    }

    public void setVehicle(String model, String manufacturer, String engineSize, String vehicleType, String fuelType) {
        mod.setText(model);
        manuf.setText(manufacturer);
        eSize.setText(engineSize);
        vType.setValue(vehicleType);
        fType.setValue(fuelType);

        mod.setDisable(true);
        manuf.setDisable(true);
        eSize.setDisable(true);
        vType.setDisable(true);
        fType.setDisable(true);
    }

    public void setVehicleCustomerID() {
        List<Customer> customerList = cSystem.getAllCustomers();
        for (int i = customerList.size() - 1; i >= 0; i--) {
            Customer customer = customerList.get(i);
            cID.setText(Integer.toString(customer.getCustomerID()));

            //addVehicleLabel.setText("Add Vehicle new Customer: " + customer.getCustomerFirstname() + " " + customer.getCustomerSurname());
            addVehicleLabel.setText(customer.getCustomerFirstname() + " " + customer.getCustomerSurname() + "'s new Vehicle");

            break;
        }
    }

    public void errorAlert(String message) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("Error Message");
        errorAlert.setHeaderText(message);
        errorAlert.showAndWait();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            setVehicleCustomerID();
            rDateMot.setDayCellFactory(motDayFactory);
            dLastServiced.setDayCellFactory(dlsDayFactory);
            wExpirationDate.setDayCellFactory(weDayFactory);
        }
        catch (Exception e) {
            System.out.println("Initialize CustomerVehicleController.java class Error");
            e.printStackTrace();
        }
    }

    private Date fromLocalDate(java.time.LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
