package controllers.customer;

import controllers.booking.BookingController;
import controllers.login.LoginController;
import controllers.user.UserController;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.collections.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.stage.WindowEvent;
import javafx.util.*;
import javafx.fxml.*;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.BooleanStringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.stage.Stage;
import javafx.fxml.FXML;

import java.io.IOException;
import java.util.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.lang.*;
import java.text.ParseException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
//import logic.*;
import logic.criterion.Criterion;
import logic.criterion.CriterionOperator;
import logic.customer.CustomerSystem;
import logic.vehicle.*;
import logic.booking.*;
import logic.criterion.Criterion;
import domain.*;
import domain.DiagRepBooking;
import persistence.DatabaseRepository;
import static javafx.scene.control.cell.TextFieldTableCell.forTableColumn;

/**
 * Created by EBUBECHUKWU on 21/03/2017.
 */
public class CustomerVehicleController implements Initializable
{
    private static CustomerVehicleController instance;

    ////for 'AddCustomerVehiclePopupView.fxml' instance variables
    Stage addVehicleStage;

    @FXML
    public CustomerSystem cSystem = CustomerSystem.getInstance();
    private VehicleSys vSys = VehicleSys.getInstance();
    private BookingSystem bSystem = BookingSystem.getInstance();

    final ObservableList DisplayTable = FXCollections.observableArrayList();

    @FXML
    private Label AddEditL = new Label();
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




    public static CustomerVehicleController getInstance()
    {
        if (instance == null) instance = new CustomerVehicleController();
        return instance;
    }

    public Stage showVehiclePopup() throws Exception
    {
        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/customer/AddCustomerVehiclePopupView.fxml"));
            Parent menu = fxmlLoader.load();
            addVehicleStage = new Stage();
            addVehicleStage.setTitle("Add Vehicle");
            addVehicleStage.setScene(new Scene(menu));

            addVehicleStage.initModality(Modality.APPLICATION_MODAL);
            addVehicleStage.showAndWait();
        }
        catch(IOException e)
        {
            System.out.println("Show Vehicle Popup Error");
            e.printStackTrace();
        }
        return addVehicleStage;
    }


    //ALL THE LOGIC FOR ADD VEHICLE TO NEW CUSTOMER RECORD


    public void addVehicle() throws Exception
    {


        if(!checkVehicleFormat())
        {
            return;
        }

        try
        {
            boolean check = checkVehicleFields();
            if(check)
            {
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                Date rdm = java.sql.Date.valueOf(rDateMot.getValue());
                Date dls = java.sql.Date.valueOf(dLastServiced.getValue());
                Date wed = new Date();
                if ((!(wExpirationDate.getValue() ==  null))) {
                    wed = java.sql.Date.valueOf(wExpirationDate.getValue());
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

                Vehicle vehicle = vSys.searchAVehicle(reg.getText());
                if (vehicle != null)
                {
                    errorAlert("Cant add this vehicle as Registrations exists");
                    return;
                }

                int customerID = Integer.parseInt(cID.getText());
                boolean checker = vSys.addEditVehicle(reg.getText(),customerID, vT, mod.getText(), manuf.getText(), Double.parseDouble(eSize.getText()), fT, col.getText(), Integer.parseInt(mil.getText()), rdm, dls, W, wName.getText(), wCompAddress.getText(), wed);



                if(checker)
                {
                    ////
                    //clearCustomerFields();
                    //addVehicleStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    //    @Override
                    //    public void handle(WindowEvent event) {
                    //        System.out.println("Add Vehicle stage is closing");
                    //    }
                    //});
                    addVehicleStage.close();
                }
            }
        }
        catch(Exception e)
        {
            System.out.println("Add Customer's Vehicle Error");
            e.printStackTrace();
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
                wExpirationDate.setValue(null);
            }}
        catch (Exception e)
        {
            wName.setDisable(false);
            wCompAddress.setDisable(false);
            wExpirationDate.setDisable(false);
        }
    }


    public StringConverter<LocalDate> SC()
    {
        StringConverter<LocalDate> SC= new StringConverter<LocalDate>()
        {
            private DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("dd/MM/yyyy");

            @Override
            public String toString(LocalDate localDate)
            {
                if(localDate==null)
                    return "";
                return dateTimeFormatter.format(localDate);
            }

            @Override
            public LocalDate fromString(String dateString)
            {
                if(dateString==null || dateString.trim().isEmpty())
                {
                    return null;
                }
                return LocalDate.parse(dateString,dateTimeFormatter);
            }
        };
        return SC;
    }

    public void clearVehicleFields()
    {
        reg.clear();
        //cID.setValue(null);
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
        //customerTypeSearch.setValue(null);//set Search by Customer Type ComboBox to null
    }

    public boolean checkVehicleFields()
    {
        boolean check = false;
        if ((!reg.getText().equals("")) && (!cID.equals(""))  && (!vType.getSelectionModel().getSelectedItem().toString().equals("")) && (!mod.getText().equals("")) && (!manuf.getText().equals("")) && (!eSize.getText().equals("")) && (!fType.getSelectionModel().getSelectedItem().toString().equals("")) && (!col.getText().equals("")) && (!mil.getText().equals("")) && (!rDateMot.getValue().equals("")) && (!dLastServiced.getValue().equals("")) && (!cByWarranty.getSelectionModel().getSelectedItem().toString().equals("")))
        {
            check = true;
            if (cByWarranty.getSelectionModel().getSelectedItem().toString().equals("True"))
            {
                if (wName.getText().equals("") || wCompAddress.getText().equals("") || wExpirationDate.getValue().equals(""))
                {
                    check = false;
                }
            }
        }
        return check;
    }

    public boolean checkVehicleFormat()
    {
        try {
            Date date = java.sql.Date.valueOf(dLastServiced.getValue());
            date = java.sql.Date.valueOf(rDateMot.getValue());
            if (cByWarranty != null)
            {
                if (cByWarranty.getSelectionModel().getSelectedItem().toString().equals("True") || cByWarranty.getSelectionModel().getSelectedItem().equals(null))
                {
                    date = java.sql.Date.valueOf(wExpirationDate.getValue());
                }}

            Double engineSize = Double.parseDouble(eSize.getText());
            int mileage = Integer.parseInt(mil.getText());
            if (col.getText().matches(".*\\d+.*"))
            {
                errorAlert("Colour must be a string");
                return false;
            }

            if (vType == null || fType == null)
            {
                errorAlert("Pick fuel type and vehicle Type");
                return false;
            }
            return true;
        }
        catch (Exception e)
        {
            errorAlert(e.getMessage() + " and make sure all fields required are not blank");
            return false;
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

    public void selectVehicleS()
    {
        List<Vehicle> arrayList;
        if (VehicleTS.getSelectionModel().isSelected(0))
        {
            arrayList = vSys.searchByTemplate("Civic", "Honda", 1.6, VehicleType.Car, FuelType.petrol);
        }
        else if (VehicleTS.getSelectionModel().isSelected(1))
        {
            arrayList = vSys.searchByTemplate("Focus", "Ford", 1.2,  VehicleType.Car, FuelType.diesel);
        }
        else if (VehicleTS.getSelectionModel().isSelected(2))
        {
            arrayList = vSys.searchByTemplate("5 Series", "BMw", 2.2,  VehicleType.Car, FuelType.petrol);
        }
        else if (VehicleTS.getSelectionModel().isSelected(3))
        {
            arrayList = vSys.searchByTemplate("3 Series", "BMw", 2.9, VehicleType.Car, FuelType.diesel);
        }
        else if (VehicleTS.getSelectionModel().isSelected(4))
        {
            arrayList = vSys.searchByTemplate("A Class", "Mercedes", 3.0,  VehicleType.Car, FuelType.petrol);
        }
        else if (VehicleTS.getSelectionModel().isSelected(5))
        {
            arrayList = vSys.searchByTemplate("Transit", "Ford", 2.2,  VehicleType.Van, FuelType.petrol);
        }
        else if (VehicleTS.getSelectionModel().isSelected(6))
        {
            arrayList = vSys.searchByTemplate("Roadster", "Nissan", 1.2, VehicleType.Truck, FuelType.diesel);
        }
        else if (VehicleTS.getSelectionModel().isSelected(7))
        {
            arrayList = vSys.searchByTemplate("Y-8 Van", "Audi", 3.6, VehicleType.Van, FuelType.petrol);
        }
        else if (VehicleTS.getSelectionModel().isSelected(8))
        {
            arrayList = vSys.searchByTemplate("Enzo", "Ferrari", 4.4, VehicleType.Car,FuelType.petrol);
        }
        else if (VehicleTS.getSelectionModel().isSelected(9))
        {
            arrayList = vSys.searchByTemplate("Truckster", "Ford", 2.8, VehicleType.Truck, FuelType.diesel);
        }
        else if (VehicleTS.getSelectionModel().isSelected(10))
        {
            arrayList = vSys.searchByTemplate("Hybrid Van", "Renault", 2.3, VehicleType.Car, FuelType.petrol);
        }
        else if (VehicleTS.getSelectionModel().isSelected(11))
        {
            arrayList = vSys.searchByTemplate("Sport", "MG", 2.0, VehicleType.Car, FuelType.diesel);
        }
        else if (VehicleTS.getSelectionModel().isSelected(12))
        {
            arrayList = vSys.searchByTemplate("Model S", "Acura", 2.2, VehicleType.Car, FuelType.petrol);
        }
        else if (VehicleTS.getSelectionModel().isSelected(13))
        {
            arrayList = vSys.searchByTemplate("Arnage", "Bentley", 4.0, VehicleType.Car, FuelType.petrol);
        }
        else
        {
            arrayList = vSys.searchByTemplate("Fire Truck", "DAF", 3.8, VehicleType.Truck,FuelType.diesel);
        }
        //DisplayTable(arrayList);
    }

    public void setVehicle(String model, String manufacturer, String engineSize, String vehicleType, String fuelType)
    {
        mod.setText(model);
        manuf.setText(manufacturer);
        eSize.setText(engineSize);
        vType.setValue(vehicleType);
        fType.setValue(fuelType);
    }

    public void setVehicleCustomerID()
    {
        List<Customer> customerList = cSystem.getAllCustomers();
        for(int i=customerList.size()-1; i>=0; i--)
        {
            Customer customer = customerList.get(i);
            cID.setText(Integer.toString(customer.getCustomerID()));
            //cID.setItems(vehicleCustomerID);
            break;
        }
    }

    public void saveVehicleAndMakeBooking()
    {
        try
        {
            /////ADD SAVE VEHICLE LOGIC

            BookingController.getInstance().show();

            /////ADD CLOSE VEHICLE POPUP WINDOW
        }
        catch(Exception e)
        {
            System.out.println("Save and Make Booking Error");
            e.printStackTrace();
        }
    }

    public void errorAlert(String message)
    {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("Error Message");
        errorAlert.setHeaderText(message);
        errorAlert.showAndWait();
    }


    public boolean confirmationAlert(String title, String message)
    {
        Alert deleteAlert = new Alert(Alert.AlertType.CONFIRMATION);
        deleteAlert.setTitle(title);
        deleteAlert.setHeaderText(message);
        deleteAlert.showAndWait();
        if(deleteAlert.getResult() == ButtonType.OK)
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        setVehicleCustomerID();

        rDateMot.setDayCellFactory(motDayFactory);
        dLastServiced.setDayCellFactory(dlsDayFactory);
        wExpirationDate.setDayCellFactory(weDayFactory);

    }

    private Callback<DatePicker, DateCell> motDayFactory = dp1 -> new DateCell()
    {
        @Override
        public void updateItem( LocalDate item , boolean empty )
        {

            // Must call super
            super.updateItem(item, empty);
            if (item.isBefore(LocalDate.now()))
            {
                this.setStyle(" -fx-background-color: #ff6666; ");
                this.setDisable ( true );
            }
        }
    };

    private Callback<DatePicker, DateCell> dlsDayFactory = dp2 -> new DateCell()
    {
        @Override
        public void updateItem( LocalDate item , boolean empty )
        {

            // Must call super
            super.updateItem(item, empty);
            if (item.isAfter(LocalDate.now()))
            {
                this.setStyle(" -fx-background-color: #ff6666; ");
                this.setDisable ( true );
            }
        }
    };

    private Callback<DatePicker, DateCell> weDayFactory = dp -> new DateCell()
    {
        @Override
        public void updateItem( LocalDate item , boolean empty )
        {

            // Must call super
            super.updateItem(item, empty);
            if (item.isBefore(LocalDate.now()))
            {
                this.setStyle(" -fx-background-color: #ff6666; ");
                this.setDisable ( true );
            }
        }
    };
}
