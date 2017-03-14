package controllers.SRC;

// import com.sun.tools.corba.se.idl.InterfaceGen;
// import com.sun.tools.internal.xjc.reader.dtd.bindinfo.BIAttribute;
import domain.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
        import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import logic.*;
import persistence.DatabaseRepository;
import domain.PartRepair;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.time.*;

/**
 * @author: Muhammad Murad Ahmed 11/03/2017.
 * project: SE31
 */
public class SpecialistController implements Initializable{

    private DatabaseRepository databaseRepository = DatabaseRepository.getInstance();
    private SpecRepairSystem specRepairSystem =  SpecRepairSystem.getInstance(databaseRepository);
    private BookingSystem bookingSystem = BookingSystem.getInstance();
    private int spcRepID, installationID;

    @FXML
    private DatePicker bookingDeliveryDate = new DatePicker();
    @FXML
    private DatePicker bookingReturnDate = new DatePicker();
    @FXML
    private DatePicker instaDate, wEndDate = new DatePicker();

    public void initialize(URL location, ResourceBundle resources){
        bookingDeliveryDate.setDayCellFactory(dateChecker);
        bookingReturnDate.setDayCellFactory(dateChecker);
        instaDate.setDayCellFactory(dateChecker);
        wEndDate.setDayCellFactory(dateChecker);

        try {
           setValues();

        }catch(NullPointerException e){e.printStackTrace();}
        findSRCBookings();

    }
    @FXML
    private ComboBox<String> bookingType = new ComboBox<>();

    private void setValues()
    {
        bookingType.getItems().addAll("Vehicle", "Part");
        bookingType.setValue("Vehicle");
    }

    private Callback<DatePicker, DateCell> dateChecker = dp1 -> new DateCell()
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



    private int spcID;

    @FXML
    public void showDetails()
    {
        try {
            VehicleRepair vehicleRepair = specialistBookings.getSelectionModel().getSelectedItem();
            bookingID.setText(Integer.toString(vehicleRepair.getBookingID()));
            bookingID.setEditable(false);
            bookingItemID.setText(vehicleRepair.getVehicleRegNumber());
            bookingItemID.setEditable(false);
           // Instant instant = vehicleRepair.getDeliveryDate().toInstant();
            //ZoneId defaultZoneId = ZoneId.systemDefault();
            java.time.LocalDate deliveryDate = toLocalDate(vehicleRepair.getDeliveryDate());
            bookingDeliveryDate.setValue(deliveryDate);
            bookingDeliveryDate.setEditable(false);
            //Instant returnInstant = vehicleRepair.getReturnDate().toInstant();
            java.time.LocalDate returnDate = toLocalDate(vehicleRepair.getReturnDate());
            bookingReturnDate.setValue(returnDate);
            bookingReturnDate.setEditable(false);
            SpecialistRepairCenter specialistRepairCenter = specRepairSystem.getByID(vehicleRepair.getSpcID());
            bookingSPCID.setText(Integer.toString(specialistRepairCenter.getSpcID()));
            bookingSPCID.setEditable(false);
            bookingSPCName.setText(specialistRepairCenter.getName());
            bookingCost.setText(Double.toString(vehicleRepair.getCost()));
            bookingCost.setEditable(false);
            spcRepID = vehicleRepair.getSpcRepID();

        }
        catch (NullPointerException  e)
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
        bookingCost.setEditable(true);
        bookingType.setVisible(false);
        addSRBooking.setVisible(false);
        updateSRBooking.setVisible(true);
        bookingDeliveryDate.setEditable(true);
        bookingReturnDate.setEditable(true);
    }

    public void updateBooking()
    {
        try {
            //java.time.LocalDate delDate = bookingDeliveryDate.getValue();
            //java.time.LocalDate retDate = bookingReturnDate.getValue();
            //ZoneId defaultZoneId = ZoneId.systemDefault();
           // Date deliveryDate = Date.from(delDate.atStartOfDay(defaultZoneId).toInstant());
            Date deliveryDate = fromLocalDate(bookingDeliveryDate.getValue());
            Date returnDate = fromLocalDate(bookingReturnDate.getValue());
           // Date returnDate = Date.from(retDate.atStartOfDay(defaultZoneId).toInstant());
            if (bookingType.getSelectionModel().getSelectedItem().equals("Vehicle")) {
                VehicleRepair vehicleRepair = specRepairSystem.findVehicleRepairBooking(spcRepID);
                if (vehicleRepair != null) {
                    vehicleRepair.setVehicleRegNumber(bookingItemID.getText());
                    vehicleRepair.setCost(Double.parseDouble(bookingCost.getText()));
                    vehicleRepair.setSpcID(Integer.parseInt(bookingSPCID.getText()));
                    if (!(vehicleRepair.setDeliveryDate(deliveryDate) || vehicleRepair.setReturnDate(returnDate))) {
                        throw new InvalidDateException("The delivery date is before the current date : " + new Date().toString() + "or the return date is before the delivery date");
                    }
                    vehicleRepair.setReturnDate(deliveryDate);
                    vehicleRepair.setDeliveryDate(returnDate);
                    vehicleRepair.setBookingID(Integer.parseInt(bookingID.getText()));
                    specRepairSystem.updateBookings(vehicleRepair);
                }

            }
            if (bookingType.getSelectionModel().getSelectedItem().equals("Part")) {
                PartRepair partRepair = specRepairSystem.findPartRepairBooking(spcRepID);
                if (partRepair != null) {
                    partRepair.setPartOccurrenceID(Integer.parseInt(bookingItemID.getText()));
                    partRepair.setCost(Double.parseDouble(bookingCost.getText()));
                    partRepair.setSpcID(Integer.parseInt(bookingSPCID.getText()));
                    if(!(partRepair.setDeliveryDate(deliveryDate) || partRepair.setReturnDate(returnDate)))
                    {
                        throw new InvalidDateException("Check Dates!");
                    }
                    partRepair.setReturnDate(deliveryDate);
                    partRepair.setDeliveryDate(returnDate);
                    partRepair.setBookingID(Integer.parseInt(bookingID.getText()));
                    specRepairSystem.updateBookings(partRepair);
                }
            }
            else if(bookingType.getSelectionModel().getSelectedItem().equals(""))
                {
                showAlert("No booking was found of this type!");
            }
            cancelEditing();
        }
                catch(InvalidDateException e)
                {
                    showAlert(e.getMessage());
                }

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
               showAlert("Please select the correct type of booking");
            }

        }

    }

    @FXML
    private boolean deleteConfirmation(String message)
    {
        Alert deleteAlert = new Alert(Alert.AlertType.CONFIRMATION);
        deleteAlert.setTitle("Delete Booking");
        deleteAlert.setHeaderText(message);
        deleteAlert.showAndWait();
        return deleteAlert.getResult() == ButtonType.OK;

    }

    //todo fix up the date issue
    //think it works now lol
    private void displaySpecRepBookings(List<VehicleRepair> vehicleRepairs)
    {
        specialistBookings.getItems().clear();
        vehicleRepairsObservableList.addAll(vehicleRepairs);
        itemID.setCellValueFactory(new PropertyValueFactory<>("vehicleRegNumber"));
        itemID.setCellFactory(TextFieldTableCell.forTableColumn());
        deliveryDateOfBooking.setCellValueFactory(new PropertyValueFactory<>("deliveryDate"));
        deliveryDateOfBooking.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Date>() {
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
        returnDateOfBooking.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Date>() {
            @Override
            public String toString(Date object) {
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                return format.format(object);
            }

            @Override
            public Date fromString(String string) {
                return new Date(string);
            }
        }));
        costOfBooking.setCellValueFactory(new PropertyValueFactory<>("cost"));
        costOfBooking.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        spcIDOfBooking.setCellValueFactory(new PropertyValueFactory<>("spcID"));
        spcIDOfBooking.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        bookingIDOfBooking.setCellValueFactory(new PropertyValueFactory<>("bookingID"));
        bookingIDOfBooking.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
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
    private Button hideInstalls, addInsta, editInsta, deleteInsta,cancelInstallationUpdate,allowUpdate,clearPartsFields;

    @FXML
    private TextField instaAbsID, instaOccID, instaVReg;
    @FXML
    private Label instaAbsID_lbl, instaOccID_lbl, instaVReg_lbl,instaDate_lbl, wEndDate_lbl = new Label();


    public void showInstallations()
    {
        VehicleRepair vehicleRepair = specialistBookings.getSelectionModel().getSelectedItem();
        Vehicle vehicle = VehicleSys.getInstance().searchAVehicle(vehicleRepair.getVehicleRegNumber());
        Installations.setVisible(true);
        hideInstalls.setVisible(true);
        List<Installation> installations = specRepairSystem.getVehicleInstallations(vehicle.getRegNumber());
        displayInstallations(installations);
        addInsta.setVisible(true);
        editInsta.setVisible(true);
        deleteInsta.setVisible(true);
        instaDate.setVisible(true);
        wEndDate.setVisible(true);
        instaAbsID.setVisible(true);
        instaOccID.setVisible(true);
        instaVReg.setVisible(true);
        instaAbsID_lbl.setVisible(true);
        instaDate_lbl.setVisible(true);
        instaOccID_lbl.setVisible(true);
        instaVReg_lbl.setVisible(true);
        wEndDate_lbl.setVisible(true);
        clearPartsFields.setVisible(true);

    }

    public void clearFields()
    {
        instaDate.setValue(null);
        wEndDate.setValue(null);
        instaAbsID.clear();
        instaOccID.clear();
        instaVReg.clear();
    }

    //todo get part occurrence ID to show and the dates to show
    private void displayInstallations(List<Installation> installations) {
        Installations.getItems().clear();
        installationObservableList.addAll(installations);
        VehicleReg.setCellValueFactory(new PropertyValueFactory<Installation, String>("vehicleRegNumber"));
        VehicleReg.setCellFactory(TextFieldTableCell.forTableColumn());
        instDate.setCellValueFactory(new PropertyValueFactory<Installation, Date>("installationDate"));
        instDate.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Date>() {
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
        warrDate.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Date>() {
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

        partOccID.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Installation, Integer>,
                ObservableValue<Integer>>() {
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Installation, Integer> p) {
                return new ReadOnlyObjectWrapper<>(p.getValue().getPartOccurrence().getPartOccurrenceID());
            }
        });
        partOccID.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        Installations.setItems(installationObservableList);
    }

    @FXML
    public void showInstallationDetails()
    {
        try {
            Installation installation = Installations.getSelectionModel().getSelectedItem();
            instaVReg.setText(installation.getVehicleRegNumber());
            instaAbsID.setText(Integer.toString(installation.getPartAbstractionID()));
            installationID = installation.getInstallationID();
            //showAlert(Integer.toString(installationID));
            instaOccID.setText(Integer.toString(installation.getPartOccurrence().getPartOccurrenceID()));
            //methods required to sort out date
            //Instant instant = installation.getInstallationDate().toInstant();
            //ZoneId defaultZoneId = ZoneId.systemDefault();
           // java.time.LocalDate installationDate = instant.atZone(defaultZoneId).toLocalDate();
            // Instant returnInstant = installation.getEndWarrantyDate().toInstant();
            //PartOccurrence partOccurrence = installation.getPartOccurrence();
            //showAlert(Integer.toString(partOccurrence.getPartOccurrenceID()));
            java.time.LocalDate installationDate = toLocalDate(installation.getInstallationDate());
            java.time.LocalDate returnDate = toLocalDate(installation.getEndWarrantyDate());
            instaDate.setValue(installationDate);
            wEndDate.setValue(returnDate);
        }
        catch (NullPointerException e)
        {
            showAlert("No available installations.");
            //showAlert("An error has occurred...please stand by");
        }
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
        instaDate.setValue(null);
        wEndDate.setValue(null);
        instaAbsID.clear();
        instaVReg.clear();
        instaOccID.clear();
        instaAbsID_lbl.setVisible(false);
        instaDate_lbl.setVisible(false);
        instaOccID_lbl.setVisible(false);
        instaVReg_lbl.setVisible(false);
        wEndDate_lbl.setVisible(false);
        clearPartsFields.setVisible(false);
    }

    //add an SRC Booking
    //todo fix up the date problem
    //think i fixed it lol
    public void addBooking()
    {
        try {
            List<DiagRepBooking> diagRepBookings = bookingSystem.searchBookings(bookingID.getText());//check if booking exist
            if(diagRepBookings.get(0)!=null) {
               // DiagRepBooking diagRepBooking = diagRepBookings.get(0);
                //Bill bill = new Bill(Double.parseDouble(bookingCost.getText()), false);
                //diagRepBooking.setBill(bill);

                if(!isBefore(bookingDeliveryDate.getValue()))
                {
                  throw new  InvalidDateException("Delivery date before today's date.");
                }

                Date deliveryDate = fromLocalDate(bookingDeliveryDate.getValue());
                Date returnDate = fromLocalDate(bookingReturnDate.getValue());

                if(returnDate.before(deliveryDate))
                {
                    throw new InvalidDateException("Return date before delivery date.");
                }
                else
                {
                    if (bookingType.getSelectionModel().getSelectedItem().equals("Vehicle")) {
                        Vehicle vehicle = VehicleSys.getInstance().searchAVehicle(bookingItemID.getText());
                        if(vehicle !=null) {
                            VehicleRepair vehicleRepair = new VehicleRepair(Integer.parseInt(bookingSPCID.getText()), deliveryDate, returnDate, Double.parseDouble(bookingCost.getText()), Integer.parseInt(bookingID.getText()), bookingItemID.getText());
                            specRepairSystem.addSpecialistBooking(vehicleRepair);
                            showAlert("Successfully added vehicle booking");
                        }
                    } else if(bookingType.getSelectionModel().getSelectedItem().equals("Part")){
                        Installation installation = specRepairSystem.checkIfInstalled(Integer.parseInt(bookingItemID.getText()));
                        if(installation!=null) {
                            PartRepair partRepair = new PartRepair(Integer.parseInt(bookingSPCID.getText()), deliveryDate, returnDate, Double.parseDouble(bookingCost.getText()), Integer.parseInt(bookingID.getText()), Integer.parseInt(bookingItemID.getText()));
                            specRepairSystem.addSpecialistBooking(partRepair);
                            showAlert("Successfully added part booking ");
                        }
                    }
               }

            }
            else{
                showAlert("Please enter a valid booking ID!");
            }
        }
        catch (NumberFormatException | InvalidDateException | IndexOutOfBoundsException e) {
            if (e instanceof InvalidDateException) {
                showAlert(e.getMessage());
            }
            if (e instanceof NumberFormatException)
            {
                showAlert("Please check all fields have been entered correctly.");
            }
            if(e instanceof IndexOutOfBoundsException)
            {
                showAlert(e.getMessage());
            }

        }
    }

    private boolean isBefore(LocalDate localDate)
    {
        LocalDate now = LocalDate.now();
        return !localDate.isBefore(now);
    }

    private java.time.LocalDate toLocalDate(Date date)
    {
        ZoneId zoneId = ZoneId.systemDefault();
        Instant instant = date.toInstant();
        return instant.atZone(zoneId).toLocalDate();
    }

    private Date fromLocalDate(java.time.LocalDate localDate)
    {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
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
    //cannot update any date related issue
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
            if (deleteConfirmation("Are you sure you want to delete this installation?")) {
                specRepairSystem.deleteInstallation(installation.getInstallationID());
            }
        }
        catch (NullPointerException e)
        {
            showAlert("Unable to delete...");
        }
    }

    @FXML
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Message");
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    //code for the right side of the fxml





}
