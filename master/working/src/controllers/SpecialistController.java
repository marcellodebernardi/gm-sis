package controllers;

import com.sun.tools.corba.se.idl.InterfaceGen;
import com.sun.tools.internal.xjc.reader.dtd.bindinfo.BIAttribute;
import domain.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.BooleanStringConverter;
import javafx.util.converter.DateStringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import logic.BookingSystem;
import logic.SpecRepairSystem;
import org.joda.time.LocalDate;
import persistence.DatabaseRepository;
import domain.PartRepair;
import java.net.URL;
import java.text.SimpleDateFormat;
//import java.time.LocalDate;
import java.util.Date;
import javax.xml.crypto.Data;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author: Muhammad Murad Ahmed 11/03/2017.
 * project: SE31
 */
public class SpecialistController implements Initializable{

    private DatabaseRepository databaseRepository = DatabaseRepository.getInstance();
    private SpecRepairSystem specRepairSystem =  SpecRepairSystem.getInstance(databaseRepository);
    private BookingSystem bookingSystem = BookingSystem.getInstance();
    private int spcRepID, installationID;

    public void initialize(URL location, ResourceBundle resources){

        try {
           setValues();

        }catch(NullPointerException e){e.printStackTrace();}

    }
    @FXML
    private ComboBox<String> bookingType;
    public void setValues()
    {
        bookingType.getItems().addAll("Vehicle", "Part");
        bookingType.setValue("Vehicle");
    }
    //required fields for searching a booking
    @FXML
    private TextField idOfBookingItem;
    @FXML
    private Button searchForBookings,viewVehicleInstallations,addSRBooking, cancelEdit,updateSRBooking;
    @FXML
    private TableView<VehicleRepair> specialistBookings;
    @FXML
    private TableColumn <VehicleRepair, String>itemID;
    @FXML
    private TableColumn <VehicleRepair, Date>deliveryDateOfBooking;
    @FXML
    private TableColumn <VehicleRepair,Date>returnDateOfBooking;
    @FXML
    private TableColumn <VehicleRepair, Double>costOfBooking;
    @FXML
    private TableColumn <VehicleRepair, Integer>spcIDOfBooking;
    @FXML
    private TableColumn <VehicleRepair, Integer>bookingIDOfBooking;
    @FXML
    private ObservableList<VehicleRepair> vehicleRepairsObservableList = FXCollections.observableArrayList();

    //for displaying details onto text box
    @FXML
    private TextField bookingID,bookingItemID,bookingSPCID,bookingSPCName,bookingCost;
    @FXML
    private DatePicker bookingDeliveryDate = new DatePicker();
    private DatePicker bookingReturnDate = new DatePicker();

    @FXML
    public void showDetails()
    {
        try {
            VehicleRepair vehicleRepair = specialistBookings.getSelectionModel().getSelectedItem();
            bookingID.setText(Integer.toString(vehicleRepair.getBookingID()));
            bookingID.setEditable(false);
            bookingItemID.setText(vehicleRepair.getVehicleRegNumber());
            bookingItemID.setEditable(false);
            //bookingDeliveryDate.setValue(new LocalDate());
            //bookingReturnDate.setValue(new LocalDate());
            SpecialistRepairCenter specialistRepairCenter = specRepairSystem.getByID(vehicleRepair.getSpcID());
            bookingSPCID.setText(Integer.toString(specialistRepairCenter.getSpcID()));
            bookingSPCID.setEditable(false);
            bookingSPCName.setText(specialistRepairCenter.getName());
            //bookingSPCName.setEditable(false);
            bookingCost.setText(Double.toString(vehicleRepair.getCost()));
            bookingCost.setEditable(false);
            spcRepID = vehicleRepair.getSpcRepID();

        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }
    }

    @FXML
    public void findSRCBookings()
    {
        specialistBookings.getItems().clear();
        List<VehicleRepair> vehicleRepairs = specRepairSystem.getVehicleBookings(idOfBookingItem.getText());
        displaySpecRepBookings(vehicleRepairs);


    }

    public void cancelEditing()
    {
        bookingID.setEditable(false);
        bookingItemID.setEditable(false);
        bookingSPCID.setEditable(false);
        //bookingSPCName.setEditable(false);
        bookingCost.setEditable(false);
        cancelEdit.setVisible(false);
        bookingType.setVisible(true);
        updateSRBooking.setVisible(false);
        addSRBooking.setVisible(true);

    }

    public void editBooking()
    {
        cancelEdit.setVisible(true);
        bookingID.setEditable(true);
        bookingItemID.setEditable(true);
        bookingSPCID.setEditable(true);
        //bookingSPCName.setEditable(false);
        bookingCost.setEditable(true);
        bookingType.setVisible(false);
        addSRBooking.setVisible(false);
        updateSRBooking.setVisible(true);
    }

    public void updateBooking()
    {
        if(bookingType.getSelectionModel().getSelectedItem().equals("Vehicle"))
        {
            VehicleRepair vehicleRepair = specRepairSystem.findVehicleRepairBooking(spcRepID);
            if(vehicleRepair!=null)
            {
                vehicleRepair.setVehicleRegNumber(bookingItemID.getText());
                vehicleRepair.setCost(Double.parseDouble(bookingCost.getText()));
                vehicleRepair.setSpcID(Integer.parseInt(bookingSPCID.getText()));
               // vehicleRepair.setReturnDate();
               // vehicleRepair.setDeliveryDate();
                specRepairSystem.updateBookings(vehicleRepair);
            }

        }
        if(bookingType.getSelectionModel().getSelectedItem().equals("Part"))
        {
            PartRepair partRepair = specRepairSystem.findPartRepairBooking(spcRepID);
            if(partRepair!=null)
            {
                partRepair.setPartOccurrenceID(Integer.parseInt(bookingItemID.getText()));
                partRepair.setCost(Double.parseDouble(bookingCost.getText()));
                partRepair.setSpcID(Integer.parseInt(bookingSPCID.getText()));
                //partRepair.setReturnDate();
                //partRepair.setDeliveryDate();
                specRepairSystem.updateBookings(partRepair);
            }
        }
        else
        {
            System.out.println("No booking was found of this type!");
        }
        cancelEditing();
    }

    public void deleteBooking()
    {
        if(deleteConfirmation("Are you sure you want to delete this booking?"))
        {
            if(bookingType.getSelectionModel().getSelectedItem().equals("Vehicle"))
            {
                specRepairSystem.deleteByRepIDV(spcRepID);
            }
            else if(bookingType.getSelectionModel().getSelectedItem().equals("Part"))
            {
                specRepairSystem.deleteByRepIDP(spcRepID);
            }
            else
            {
                System.out.println("Please select the correct type of booking");
            }

        }

    }

    public boolean deleteConfirmation(String message)
    {
        Alert deleteAlert = new Alert(Alert.AlertType.CONFIRMATION);
        deleteAlert.setTitle("Delete Booking");
        deleteAlert.setHeaderText(message);
        deleteAlert.showAndWait();
        if(deleteAlert.getResult() == ButtonType.OK)
        {
            return true;
        }
            return false;

    }

    //todo fix up the date issue
    private void displaySpecRepBookings(List<VehicleRepair> vehicleRepairs)
    {
        specialistBookings.getItems().clear();
        vehicleRepairsObservableList.addAll(vehicleRepairs);
        itemID.setCellValueFactory(new PropertyValueFactory<VehicleRepair, String>("vehicleRegNumber"));
        itemID.setCellFactory(TextFieldTableCell.<VehicleRepair>forTableColumn());
        deliveryDateOfBooking.setCellValueFactory(new PropertyValueFactory<VehicleRepair, Date>("deliveryDate"));
        deliveryDateOfBooking.setCellFactory(TextFieldTableCell.<VehicleRepair, Date>forTableColumn(new StringConverter<Date>() {
            @Override
            public String toString(Date object) {
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                return format.format(object);
            }

            @Override
            public Date fromString(String string) {
                return null;
            }
        }));
        returnDateOfBooking.setCellValueFactory(new PropertyValueFactory<VehicleRepair, Date>("returnDate"));
        returnDateOfBooking.setCellFactory(TextFieldTableCell.<VehicleRepair, Date>forTableColumn(new StringConverter<Date>() {
            @Override
            public String toString(Date object) {
                if (object == null)
                {
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
        costOfBooking.setCellValueFactory(new PropertyValueFactory<VehicleRepair, Double>("cost"));
        costOfBooking.setCellFactory(TextFieldTableCell.<VehicleRepair, Double>forTableColumn(new DoubleStringConverter()));
        spcIDOfBooking.setCellValueFactory(new PropertyValueFactory<VehicleRepair, Integer>("spcID"));
        spcIDOfBooking.setCellFactory(TextFieldTableCell.<VehicleRepair, Integer>forTableColumn(new IntegerStringConverter()));
        bookingIDOfBooking.setCellValueFactory(new PropertyValueFactory<VehicleRepair, Integer>("bookingID"));
        bookingIDOfBooking.setCellFactory(TextFieldTableCell.<VehicleRepair, Integer>forTableColumn(new IntegerStringConverter()));
        specialistBookings.setItems(vehicleRepairsObservableList);
    }

    //required for parts
    @FXML
    private TableView<Installation> Installations;
    @FXML
    private TableColumn<Installation, Date> instDate, warrDate;
    @FXML
    private TableColumn<Installation, Integer> partOccID;
    @FXML
    private TableColumn<Installation, String> VehicleReg;
    @FXML
    private ObservableList<Installation> installationObservableList = FXCollections.observableArrayList();
    @FXML
    private Button hideInstalls, addInsta, editInsta, deleteInsta,cancelInstallationUpdate,allowUpdate;
    @FXML
    private DatePicker instaDate, wEndDate = new DatePicker();
    @FXML
    private TextField instaAbsID, instaOccID, instaVReg;


    public void showInstallations()
    {
        Installations.setVisible(true);
        hideInstalls.setVisible(true);
        List<Installation> installations = specRepairSystem.getVehicleInstallations(idOfBookingItem.getText());
        displayInstallations(installations);
        addInsta.setVisible(true);
        editInsta.setVisible(true);
        deleteInsta.setVisible(true);
        instaDate.setVisible(true);
        wEndDate.setVisible(true);
        instaAbsID.setVisible(true);
        instaOccID.setVisible(true);
        instaVReg.setVisible(true);

    }

    //todo get part occurence ID to show and the dates to show
    private void displayInstallations(List<Installation> installations) {
        Installations.getItems().clear();
        installationObservableList.addAll(installations);
        VehicleReg.setCellValueFactory(new PropertyValueFactory<Installation, String>("vehicleRegNumber"));
        VehicleReg.setCellFactory(TextFieldTableCell.<Installation>forTableColumn());
        instDate.setCellValueFactory(new PropertyValueFactory<Installation, Date>("installationDate"));
        instDate.setCellFactory(TextFieldTableCell.<Installation, Date>forTableColumn(new StringConverter<Date>() {
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
        warrDate.setCellValueFactory(new PropertyValueFactory<Installation, Date>("endWarrantyDate"));
        warrDate.setCellFactory(TextFieldTableCell.<Installation, Date>forTableColumn(new StringConverter<Date>() {
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
        /*
        partOccID.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Installation, Integer>,
                ObservableValue<Integer>>() {
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Installation, Integer> p) {
                return new ReadOnlyObjectWrapper<>(p.getValue().getPartOccurrence().getPartOccurrenceID());
            }
        });

        partOccID.setCellFactory(TextFieldTableCell.<Installation, Integer>forTableColumn(new IntegerStringConverter()));
        */
        Installations.setItems(installationObservableList);
    }

    @FXML
    public void showInstallationDetails()
    {
        Installation installation = Installations.getSelectionModel().getSelectedItem();
        instaVReg.setText(installation.getVehicleRegNumber());
        PartOccurrence partOccurrence = installation.getPartOccurrence();
        instaOccID.setText(Integer.toString(partOccurrence.getPartOccurrenceID()));
        instaAbsID.setText(Integer.toString(installation.getPartAbstractionID()));
        installationID = installation.getInstallationID();
        //instaDate.setValue();
       // wEndDate.setValue();

    }

    public void hideInstallations()
    {
        Installations.setVisible(false);
        hideInstalls.setVisible(false);
        addInsta.setVisible(false);
        editInsta.setVisible(false);
        deleteInsta.setVisible(false);
        instaDate.setVisible(false);
        wEndDate.setVisible(false);
        instaAbsID.setVisible(false);
        instaOccID.setVisible(false);
        instaVReg.setVisible(false);
    }

    //add an SRC Booking
    //todo fix up the date problem
    public void addBooking()
    {
        try {
            List<DiagRepBooking> diagRepBookings = bookingSystem.searchBookings(bookingID.getText());//check if booking exist
            if(diagRepBookings.get(0)!=null) {
                DiagRepBooking diagRepBooking = diagRepBookings.get(0);
                Bill bill = new Bill(Double.parseDouble(bookingCost.getText()), false);
                diagRepBooking.setBill(bill);
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                if (bookingType.getSelectionModel().getSelectedItem().equals("Vehicle")) {
                    VehicleRepair vehicleRepair = new VehicleRepair(Integer.parseInt(bookingSPCID.getText()), new Date(), new Date(), Double.parseDouble(bookingCost.getText()), Integer.parseInt(bookingID.getText()), bookingItemID.getText());
                    specRepairSystem.addSpecialistBooking(vehicleRepair);
                } else {
                    PartRepair partRepair = new PartRepair(Integer.parseInt(bookingSPCID.getText()), new Date(), new Date(), Double.parseDouble(bookingCost.getText()), Integer.parseInt(bookingID.getText()), Integer.parseInt(bookingItemID.getText()));
                    specRepairSystem.addSpecialistBooking(partRepair);
                }
            }
            else
                {
                    System.out.println("Please enter a valid booking ID!");
                }
        }
        catch (NumberFormatException e)
        {
            System.out.println("Entered a non numerical value for a part ID.");
        }
    }

    //todo fix this shit up
    public void addInstallation()
    {
        //PartOccurrence partOccurrence = new PartOccurrence()
        //Installation installation = new Installation()
    }

    public void editInstallation()
    {
        cancelEdit.setVisible(true);
        allowUpdate.setVisible(true);

    }

    public void updateInstallation() {
        Installation installation = specRepairSystem.getByInstallationID(installationID);
        installation.setVehicleRegNumber(instaVReg.getText());
        installation.setPartAbstractionID(Integer.parseInt(instaAbsID.getText()));
        // todo find way to edit part occurence id, warranty date and start date
        //installation.setEndWarrantyDate();
        specRepairSystem.updateInstallation(installation);
    }

    public void cancelUpdate()
    {
        cancelEdit.setVisible(false);
        allowUpdate.setVisible(false);
    }

    public void deleteInstallation()
    {
        try {
            Installation installation = specRepairSystem.getByInstallationID(installationID);
            System.out.println(installation.getInstallationID());
            if (deleteConfirmation("Are you sure you want to delete this installation?")) {
                specRepairSystem.deleteInstallation(installation.getInstallationID());
            }
        }
        catch (NullPointerException e)
        {
            showAlert(e.getMessage());
        }
    }

    @FXML
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Message");
        alert.setHeaderText(message);
        alert.showAndWait();
    }


}
