package controllers.SRC;

import domain.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;
import logic.*;
import persistence.DatabaseRepository;

import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class SpecialistRepairController implements Initializable{


    private DatabaseRepository databaseRepository = DatabaseRepository.getInstance();
    private SpecRepairSystem specRepairSystem = SpecRepairSystem.getInstance(databaseRepository);
    private PartsSystem partsSystem = PartsSystem.getInstance(databaseRepository);
    private AuthenticationSystem authenticationSystem = AuthenticationSystem.getInstance();
    private VehicleSys vehicleSys = VehicleSys.getInstance();
    private BookingSystem bookingSystem = BookingSystem.getInstance();

    private int spcID;

    @FXML
    private Button btn_addSRC, btn_deleteSRC, btn_updateSRC = new Button();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        if(AuthenticationSystem.getInstance().getUserType().equals(UserType.NORMAL))
        {
            btn_addSRC.setDisable(true);
            btn_updateSRC.setDisable(true);
            btn_deleteSRC.setDisable(true);
        }
        findSRC();

    }
    @FXML
    private TableView<SpecialistRepairCenter> SpecialistRepairCenters;

    @FXML
    private TableColumn<SpecialistController, Integer> spc_id_column;

    @FXML
    private TableColumn<SpecialistController, String> spc_name_column,spc_phone_column,spc_address_column,spc_email_column = new TableColumn<>();

    @FXML
    private TextField searchSRC,src_id,src_name,src_address,src_email, src_phone = new TextField();

    @FXML
    private ObservableList<SpecialistRepairCenter> specialistRepairCenterObservableList = FXCollections.observableArrayList();

    @FXML
    public void findSRC() {

        try{
            List<SpecialistRepairCenter> specialistRepairCenters = specRepairSystem.getAllBookings(Integer.parseInt(searchSRC.getText()));
            displayCentersToTable(specialistRepairCenters);

        }
        catch (NumberFormatException | NullPointerException e)
        {
            if(e instanceof  NumberFormatException)
            {
                List<SpecialistRepairCenter> specialistRepairCenters = specRepairSystem.getBookingsByName(searchSRC.getText());
                displayCentersToTable(specialistRepairCenters);
            }

        }


    }

    private void displayCentersToTable(List<SpecialistRepairCenter> specialistRepairCenters)
    {
        SpecialistRepairCenters.getItems().clear();
        specialistRepairCenterObservableList.addAll(specialistRepairCenters);
        spc_id_column.setCellValueFactory(new PropertyValueFactory<>("spcID"));
        spc_id_column.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        spc_name_column.setCellValueFactory(new PropertyValueFactory<>("name"));
        spc_name_column.setCellFactory(TextFieldTableCell.forTableColumn());
        spc_address_column.setCellValueFactory(new PropertyValueFactory<>("address"));
        spc_address_column.setCellFactory(TextFieldTableCell.forTableColumn());
        spc_email_column.setCellValueFactory(new PropertyValueFactory<>("email"));
        spc_email_column.setCellFactory(TextFieldTableCell.forTableColumn());
        spc_phone_column.setCellValueFactory(new PropertyValueFactory<>("phone"));
        spc_phone_column.setCellFactory(TextFieldTableCell.forTableColumn());
        SpecialistRepairCenters.setItems(specialistRepairCenterObservableList);
    }

    public void showSpcDetails()
    {
        SpecialistRepairCenter specialistRepairCenter = SpecialistRepairCenters.getSelectionModel().getSelectedItem();
        src_id.setText(Integer.toString(specialistRepairCenter.getSpcID()));
        src_name.setText(specialistRepairCenter.getName());
        src_phone.setText(specialistRepairCenter.getPhone());
        src_email.setText(specialistRepairCenter.getEmail());
        src_address.setText(specialistRepairCenter.getAddress());
        src_name.setText(specialistRepairCenter.getName());
        spcID = specialistRepairCenter.getSpcID();
    }

    public void clearSRCFields()
    {
        src_id.clear();
        src_name.clear();
        src_phone.clear();
        src_address.clear();
        src_email.clear();
    }

    public void addNewSRC()
    {
        if(src_phone.getText().length() == 11 && !src_name.getText().equals("") && !src_address.equals("") && !src_email.equals("")) {
            if(addConfirmation()) {
                specRepairSystem.addRepairCenter(src_name.getText(), src_address.getText(), src_phone.getText(), src_email.getText());
            }
        }
    }

    public void deleteSRC()
    {
        try {
            if(deleteConfirmation("Are you sure you want to delete this Specialist repair center?"))
                specRepairSystem.deleteAllSubsequentBookings(spcID);
            specRepairSystem.deleteRepairCenter(spcID);
        }
        catch (NullPointerException e)
        {
            showAlert("No SPC Selected.");
        }
    }

    public void updateSRC()
    {
        SpecialistRepairCenter specialistRepairCenter = specRepairSystem.getByID(spcID);
        if(updateConfirmation("Are you sure you want to update this SPC?"))
        {
            specialistRepairCenter.setEmail(src_email.getText());
            specialistRepairCenter.setName(src_name.getText());
            specialistRepairCenter.setAddress(src_address.getText());
            specialistRepairCenter.setPhone(src_phone.getText());
            specRepairSystem.updateRepairCentre(specialistRepairCenter);
        }
    }

    private boolean addConfirmation()
    {
        Alert addAlert = new Alert(Alert.AlertType.CONFIRMATION);
        addAlert.setTitle("Add Specialist Repair Center");
        addAlert.setHeaderText("Are you sure you want to add this center?");
        addAlert.showAndWait();
        return addAlert.getResult() == ButtonType.OK;

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

    @FXML
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Message");
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    private boolean updateConfirmation(String message)
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Update specialist center");
        alert.setHeaderText(message);
        alert.showAndWait();
        return alert.getResult() == ButtonType.OK;
    }



    /*
    @FXML

    private RadioButton vehicle_repair, part_repair;

    @FXML
    private TableView<VehicleRepair> VehicleRepairs;

    @FXML
    private TextField instID;//holds installationID

    /*
    @FXML
    private ToggleGroup bookingType;

    @FXML
    private ToggleGroup searchCategory;


    @FXML
    private TextField reg_number;

    @FXML
    private TextField spcID;

    @FXML
    private RadioButton veh_selected, src_selected;

    @FXML
    private Button btn_add_src, btn_del_src, btn_edit_src, btn_updateSRC, btn_deleteSRC, btn_submitSRC,
            findSRCToEdit, btn_edit_booking, btn_add_booking, btn_del_booking, delete_booking, searchForSRCBooking,
            search_src_deletion, cancelDeletion, findBookingToEdit, applyEditingChanges,findBookingForSRC;

    private int spcIDEdit, editBookingID;
    private int bookingID = 0;


    @FXML
    private TextField bookingCost, spcToEdit, spcIDtoDelete, new_spcEmail, new_spcPho, new_spcAdd, new_spcName,
            part_name, part_cost, stock_level, part_des, instaDate, bookingSPCID, partAbsID, partOccID, bookingItemID, bookingSPCName,returnDate, deliveryDate,
            itemToSearch, edit_booking_ID,findBooking;

    private final ObservableList<VehicleRepair> tableEntries = FXCollections.observableArrayList();

    @FXML
    private TableColumn<VehicleRepair, String> reg_of_vehicle_for_srbooking;

    @FXML
    private TableColumn<VehicleRepair, Integer> spcRep_ID_of_Booking, spcID_of_vehiclerepair;


    @FXML
    private TableColumn<VehicleRepair, Date> TbookingSRCName,TbookingSRC;


    /*@FXML
    private void updateTableViewSpecialistBooking(List<SpecRepBooking> specRepBookings)
    {
        VehicleRepairs.getItems().clear();
        tableEntries.removeAll(tableEntries);
        for (SpecRepBooking specRepBooking: specRepBookings)
        {
            tableEntries.add(specRepBooking);
        }
        reg_of_vehicle_for_srbooking.setCellValueFactory(new PropertyValueFactory<SpecRepBooking, String>("spcRepID"));
        reg_of_vehicle_for_srbooking.setCellFactory(TextFieldTableCell.<SpecRepBooking>forTableColumn());


    }

    /**
     * Search for a vehicle in db of specialist repair bookings
     */
    /*
    @FXML
    public void searchVehiclesSRC() {

        if (veh_selected.isSelected()) {

            List<VehicleRepair> vehicleRepairs = specRepairSystem.getVehicleBookings(itemToSearch.getText());
            //todo implement list into table view
            for(VehicleRepair vehicleRepairs1: vehicleRepairs)
            {
                System.out.println(vehicleRepairs1.getSpcRepID());
            }
            updateTableViewVehicleRepair(vehicleRepairs);
        } else if (src_selected.isSelected()) {
          //  List<SpecialistRepairCenter> specialistRepairCenters = specRepairSystem.getAllBookings(itemToSearch.getText());
            //todo implement list into table view
            updateTableViewSpecialistRepair(specialistRepairCenters);
        }
    }

    @FXML
    private void updateTableViewVehicleRepair(List<VehicleRepair> vehicleRepairs)
    {
        VehicleRepairs = new TableView<>();
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        tableEntries.addAll(vehicleRepairs);
        //tableEntries.addAll(vehicleRepairs);
        reg_of_vehicle_for_srbooking = new TableColumn<>();
        spcID_of_vehiclerepair = new TableColumn<>();
        spcRep_ID_of_Booking = new TableColumn<>();
        reg_of_vehicle_for_srbooking.setCellValueFactory(new PropertyValueFactory<VehicleRepair, String>("vehicleRegNumber"));
        reg_of_vehicle_for_srbooking.setCellFactory(TextFieldTableCell.<VehicleRepair>forTableColumn());
        spcRep_ID_of_Booking.setCellValueFactory(new PropertyValueFactory<VehicleRepair, Integer>("spcRepID"));
        spcRep_ID_of_Booking.setCellFactory(TextFieldTableCell.<VehicleRepair, Integer>forTableColumn(new IntegerStringConverter()));
        spcID_of_vehiclerepair.setCellValueFactory(new PropertyValueFactory<VehicleRepair, Integer>("spcID"));
        spcID_of_vehiclerepair.setCellFactory(TextFieldTableCell.<VehicleRepair, Integer>forTableColumn(new IntegerStringConverter()));
        //TbookingSRC.setCellValueFactory(new PropertyValueFactory<VehicleRepair, Date>("deliveryDate"));
        //TbookingSRCName.setCellValueFactory(new PropertyValueFactory<VehicleRepair, Date>("returnDate"));
        //  VehicleRepairs.getColumns().setAll(reg_of_vehicle_for_srbooking, spcRep_ID_of_Booking, spcID_of_vehiclerepair);
        //VehicleRepairs.setVisible(true);
        VehicleRepairs.setItems(tableEntries);
    }


      Search a specific SRC to edit

    public void searchSRCToEdit() {
        spcIDEdit = Integer.parseInt(spcToEdit.getText());
        SpecialistRepairCenter specialistRepairCenter = specRepairSystem.getByID(Integer.parseInt(spcToEdit.getText()));
        new_spcName.setText(specialistRepairCenter.getName());
        new_spcAdd.setText(specialistRepairCenter.getAddress());
        new_spcPho.setText(specialistRepairCenter.getPhone());
        new_spcEmail.setText(specialistRepairCenter.getEmail());
        new_spcName.setEditable(true);
        new_spcAdd.setEditable(true);
        new_spcPho.setEditable(true);
        new_spcEmail.setEditable(true);

    }

    @FXML
    public void findBookingForSRBooking()
    {
        try {
            List<DiagRepBooking> diagRepBookings = bookingSystem.searchBookings(findBooking.getText());
            DiagRepBooking diagRepBooking = diagRepBookings.get(0);
            bookingID = diagRepBooking.getBookingID();

        }
        catch (IndexOutOfBoundsException | NullPointerException e)
        {
            showAlert("No relevant booking found. Please enter a valid Booking ID, if you do not know what this is please search in the Bookings system.");
        }


    }



      Allows a booking to be made

    @FXML
    public void allowBooking() {
        try {
            SpecialistRepairCenter specialistRepairCenter = specRepairSystem.getByID(Integer.parseInt(bookingSPCID.getText()));
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            Date dD = format.parse(deliveryDate.getText());
            Date rD = format.parse(returnDate.getText());
            if(dD.compareTo(new Date())>=0 && dD.compareTo(rD) == -1) {
                if (bookingID != 0) {
                    if (vehicle_repair.isSelected()) {
                        if (vehicleSys.VehicleExists(bookingItemID.getText())) {
                            VehicleRepair vehicleRepair = new VehicleRepair(Integer.parseInt(bookingSPCID.getText()), dD, rD,
                                    Double.parseDouble(bookingCost.getText()), bookingID, bookingItemID.getText());
                            specialistRepairCenter.addToBooking(vehicleRepair);
                            specRepairSystem.updateRepairCentre(specialistRepairCenter);
                            specRepairSystem.addSpecialistBooking(vehicleRepair);
                        } else {
                            showAlert("This vehicle is not registered in our system, please register the vehicle : " + bookingItemID.getText() + " before continuing.");
                        }

                    }
                    if (part_repair.isSelected()) {
                        PartRepair partRepair = new PartRepair(Integer.parseInt(bookingSPCID.getText()), dD, rD,
                                Double.parseDouble(bookingCost.getText()), bookingID, Integer.parseInt(bookingItemID.getText()));
                        specialistRepairCenter.addToBooking(partRepair);
                        specRepairSystem.updateRepairCentre(specialistRepairCenter);
                        specRepairSystem.addSpecialistBooking(partRepair);
                    }
                }
            }
            else
            {
                throw new InvalidDateException("Invalid Dates entered");
            }

        } catch (ParseException  | NumberFormatException | InvalidDateException e) {
            if(e instanceof NumberFormatException)
            {
             showAlert("There is an error in one the fields. Please check all your fields before submitting.ad");
            }
            if(e instanceof InvalidDateException)
            {
                showAlert(e.getMessage());
            }
            if(e instanceof  ParseException)
            {
                showAlert("Please enter a valid date!");
            }
        }

    }

    /**
     * Shows a relevant alert message
     *
     * @param message message displayed to user

    @FXML
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Message");
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    /**
     * Method used to search for an SRC for a specialist repair booking

    @FXML
    public void findSRCForBooking() {
        try {
            SpecialistRepairCenter specialistRepairCenter = specRepairSystem.getByID(Integer.parseInt(bookingSPCID.getText()));
            bookingSPCName.setText(specialistRepairCenter.getName());
            bookingSPCName.setEditable(false);
        }
        catch (IndexOutOfBoundsException | NumberFormatException e)
        {
            if(e instanceof IndexOutOfBoundsException)
            {
            showAlert("Please enter a valid Specialist repair center ID.");
            }
            if(e instanceof  NumberFormatException)
            {
                showAlert("Please enter a numerical value.");
            }

        }
    }

    /**
     * Deletes an SRC
     * todo implement a confirmation message

    @FXML
    public void deleteSRC() {
        //confirmDelete(Integer.parseInt(spcIDtoDelete.getText()));
    }

   /* private void confirmDelete(int spcToDelete) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setContentText("Are you sure you want to delete this SRC?");
        alert.showAndWait();

    }


    /**
     * Updates the SRC upon making a few checks

    @FXML
    public void updateSRC() {
        String spcName = new_spcName.getText();
        String spcAdd = new_spcAdd.getText();
        String spcPho = new_spcPho.getText();
        String spcEmail = new_spcEmail.getText();
        SpecialistRepairCenter specialistRepairCenter = specRepairSystem.getByID(spcIDEdit);
        if (!spcName.equals("") || !spcAdd.equals("") || !spcPho.equals("") && spcPho.trim().length() == 11 || spcEmail.equals("")) {
            specialistRepairCenter.setName(new_spcName.getText());
            specialistRepairCenter.setAddress(new_spcAdd.getText());
            specialistRepairCenter.setPhone(new_spcPho.getText());
            specialistRepairCenter.setEmail(new_spcEmail.getText());
            specRepairSystem.updateRepairCentre(specialistRepairCenter);
        }
        new_spcName.clear();
        new_spcAdd.clear();
        new_spcPho.clear();
        new_spcEmail.clear();
    }

    /**
     * Displays relevant fields for adding of an SRC, hides all other irrelevant parts
     * todo implement system which will ensure it only shows when user is administrator

    @FXML
    void displayFields() {
        if (!(authenticationSystem.getUserType().equals(UserType.ADMINISTRATOR))) {
            btn_add_src.setDisable(true);
        } else {
            spcToEdit.setVisible(false);
            findSRCToEdit.setVisible(false);
            spcIDtoDelete.setVisible(false);
            new_spcName.setVisible(true);
            new_spcAdd.setVisible(true);
            new_spcPho.setVisible(true);
            new_spcEmail.setVisible(true);
            btn_deleteSRC.setVisible(false);
            btn_updateSRC.setVisible(false);
            new_spcName.setEditable(true);
            new_spcAdd.setEditable(true);
            new_spcPho.setEditable(true);
            new_spcEmail.setEditable(true);
            btn_submitSRC.setVisible(true);
        }

    }

    /**
     * Displays relevant fields for editing of an SRC, hides all other irrelevant parts
     * todo implement system which will ensure it only shows when user is administrator

    @FXML
    void displayFieldsToEdit() {
        if (!(authenticationSystem.getUserType().equals(UserType.ADMINISTRATOR))) {
            btn_edit_src.setDisable(true);
        } else {
            spcToEdit.setVisible(true);
            findSRCToEdit.setVisible(true);
            spcIDtoDelete.setVisible(false);
            new_spcName.setVisible(true);
            new_spcAdd.setVisible(true);
            new_spcPho.setVisible(true);
            new_spcEmail.setVisible(true);
            new_spcName.setEditable(false);
            new_spcAdd.setEditable(false);
            new_spcPho.setEditable(false);
            new_spcEmail.setEditable(false);
            btn_submitSRC.setVisible(false);
            btn_deleteSRC.setVisible(false);
            btn_updateSRC.setVisible(true);

        }
    }

    /**
     * Adds a new SRC to database

    @FXML
    void addSRC() {
        String spcName = new_spcName.getText();
        String spcAdd = new_spcAdd.getText();
        String spcPho = new_spcPho.getText();
        String spcEmail = new_spcEmail.getText();
        if (!spcName.equals("") || !spcAdd.equals("") || !spcPho.equals("") && spcPho.trim().length() == 11 || spcEmail.equals("")) {
            specRepairSystem.addRepairCenter(spcName, spcAdd, spcPho, spcEmail);
            new_spcName.clear();
            new_spcAdd.clear();
            new_spcPho.clear();
            new_spcEmail.clear();
        }
    }


    /**
     * Displays relevant fields for deletion of an SRC, hides all other irrelevant parts
     * todo implement system which will ensure it only shows when user is administrator

    @FXML
    public void showID() {
        if (!(authenticationSystem.getUserType().equals(UserType.ADMINISTRATOR))) {
            btn_del_src.setDisable(true);
        } else {
            spcToEdit.setVisible(false);
            findSRCToEdit.setVisible(false);
            new_spcName.setVisible(false);
            new_spcAdd.setVisible(false);
            new_spcPho.setVisible(false);
            new_spcEmail.setVisible(false);
            spcIDtoDelete.setVisible(true);
            spcIDtoDelete.setEditable(true);
            btn_submitSRC.setVisible(false);
            btn_submitSRC.setVisible(false);
            btn_deleteSRC.setVisible(true);
            btn_updateSRC.setVisible(false);
        }
    }



    @FXML
    private void updateTableViewSpecialistRepair(List<SpecialistRepairCenter> specialistRepairCenters) {
        VehicleRepairs.getItems().clear();

    }


    /**
     * Submits part changes on a specific part

    @FXML
    public void submitPartsChanges() {
        //todo implement updating part system (talk to Shakib)
        partAbsID.setEditable(true);
        partOccID.setEditable(true);
        instaDate.setEditable(true);
        instID.setEditable(true);
        partAbsID.clear();
        partOccID.clear();
        instaDate.clear();
        instID.clear();

    }

    /**
     * Sets the editable fields for a part

    @FXML
    public void allowEdit() {
        partAbsID.setEditable(true);
        partOccID.setEditable(true);
        instaDate.setEditable(true);
        instID.setEditable(true);
    }

    /**
     * ADDS A PART TO THE DB
     * todo needs to be fixed - Talk to Shakib

    @FXML
    public void addPart() {
        partAbsID.clear();
        partOccID.clear();
        part_cost.setVisible(true);
        part_des.setVisible(true);
        stock_level.setVisible(true);
        partOccID.setText("ID will be preset");
        partOccID.setEditable(false);
        instaDate.setEditable(false);
        instID.setEditable(false);
        part_cost.setVisible(true);
        part_name.setVisible(true);
        partsSystem.addPart(part_name.getText(), part_des.getText(),
                Double.parseDouble(part_cost.getText()) * Integer.parseInt(stock_level.getText()), Integer.parseInt(stock_level.getText()));
        part_cost.clear();
        part_des.clear();
        stock_level.clear();
        part_cost.setVisible(false);
        part_des.setVisible(false);
        stock_level.setVisible(false);

    }

    /**
     * SHOWS ALL THE OUTSTANDING ITEMS AT REPAIR CENTERS

    @FXML
    public void getOutStandingItems() {
        Date todaysDate = new Date();
        List<SpecRepBooking> specRepBookings = new ArrayList<>();
        List<VehicleRepair> vehicleRepairs = specRepairSystem.getOutstandingV(todaysDate);
        List<PartRepair> partRepairs = specRepairSystem.getOutstandingP(todaysDate);
        specRepBookings.addAll(vehicleRepairs);
        specRepBookings.addAll(partRepairs);
        //updateTableViewSpecialistBooking(specRepBookings);

    }

    @FXML
    public void showDeletionField() {
        bookingSPCName.clear();
        bookingSPCID.clear();
        deliveryDate.clear();
        bookingSPCID.setPromptText("Enter SRC ID of repair center responsible for booking.");
        returnDate.setVisible(false);
        bookingCost.setVisible(false);
        searchForSRCBooking.setVisible(false);
        btn_edit_booking.setVisible(false);
        btn_add_booking.setVisible(false);
        btn_del_booking.setVisible(false);
        delete_booking.setVisible(true);
        search_src_deletion.setVisible(true);
        cancelDeletion.setVisible(true);
        findBooking.clear();
        findBooking.setVisible(false);
        findBookingForSRC.setVisible(false);
    }

    /**
     * Allows user to delete a booking
     * Specified booking through SPC ID, Item(part or vehicle) ID(reg or occurrence ID) and date
     *
     * @throws InvalidDateException

    @FXML
    public void deleteBooking() throws InvalidDateException {
        try {

            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = dateFormat.parse(deliveryDate.getText());
            if (date.compareTo(new Date()) == -1) {
                throw new InvalidDateException("This booking may have already been completed.");
            }
            if (vehicle_repair.isSelected()) {
                if (specRepairSystem.deleteVehicleRepair(bookingItemID.getText(), spcIDEdit, date))
                    showAlert("Deletion confirmed");
                else {
                    showAlert("Deletion failed, check fields");
                }

            } else if (part_repair.isSelected()) {
                if (specRepairSystem.deletePartRepair(Integer.parseInt(bookingItemID.getText()), spcIDEdit, date))
                    showAlert("Deletion confirmed");
                else {
                    showAlert("Deletion failed, check fields");
                }

            }
            promptFields();

        } catch (ParseException | InvalidDateException e) {
            promptFields();
            showAlert(e.getMessage());
        }
    }

    /**
     * Search the SRC Responsible for repair for deletion and set text for SRC Name

    @FXML
    public void searchSRCDeletion() {
        SpecialistRepairCenter specialistRepairCenter = specRepairSystem.getByID(Integer.parseInt(bookingSPCID.getText()));
        spcIDEdit = specialistRepairCenter.getSpcID();
        bookingSPCName.setText(specialistRepairCenter.getName());
    }

    /**
     * Reset fields

    @FXML
    public void promptFields() {
        search_src_deletion.setVisible(false);
        bookingSPCName.clear();
        bookingSPCID.clear();
        deliveryDate.clear();
        bookingSPCID.setPromptText("Enter a Specialist Repair Center ID");
        returnDate.setVisible(true);
        returnDate.clear();
        bookingCost.clear();
        searchForSRCBooking.setVisible(true);
        btn_add_booking.setVisible(true);
        btn_edit_booking.setVisible(true);
        btn_del_booking.setVisible(true);
        bookingCost.setVisible(true);
        delete_booking.setVisible(false);
        cancelDeletion.setVisible(false);
        findBookingToEdit.setVisible(false);
        applyEditingChanges.setVisible(false);
        edit_booking_ID.setVisible(false);
        bookingItemID.clear();
        findBooking.clear();
        findBooking.setVisible(true);
        findBookingForSRC.setVisible(true);

    }

    @FXML
    public void showEditingFields() {
        btn_add_booking.setVisible(false);
        btn_del_booking.setVisible(false);
        bookingSPCID.clear();
        bookingSPCName.clear();
        cancelDeletion.setVisible(true);
        searchForSRCBooking.setVisible(false);
        deliveryDate.clear();
        returnDate.clear();
        findBookingToEdit.setVisible(true);
        edit_booking_ID.setVisible(true);
        bookingCost.clear();
        bookingItemID.clear();
        findBookingForSRC.setVisible(false);
        findBooking.setVisible(false);


    }

    /**
     *

    @FXML
    public void findBooking() {
        try {
            DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            applyEditingChanges.setVisible(true);
            if (vehicle_repair.isSelected()) {
                System.out.println(edit_booking_ID.getText());
                VehicleRepair vehicleRepair = specRepairSystem.findVehicleRepairBooking(Integer.parseInt(edit_booking_ID.getText()));
                editBookingID = vehicleRepair.getSpcRepID();
                bookingSPCID.setText(Integer.toString(vehicleRepair.getSpcID()));
                SpecialistRepairCenter specialistRepairCenter = specRepairSystem.getByID(vehicleRepair.getSpcID());
                bookingSPCName.setText(specialistRepairCenter.getName());
                deliveryDate.setText(format.format(vehicleRepair.getDeliveryDate()));
                returnDate.setText(format.format(vehicleRepair.getReturnDate()));
                bookingItemID.setText(vehicleRepair.getVehicleRegNumber());
                bookingCost.setText(Double.toString(vehicleRepair.getCost()));
                findBooking.setText(Integer.toString(vehicleRepair.getBookingID()));
            } else if (part_repair.isSelected()) {
                PartRepair partRepair = specRepairSystem.findPartRepairBooking(Integer.parseInt(edit_booking_ID.getText()));
                bookingSPCID.setText(Integer.toString(partRepair.getSpcID()));
                SpecialistRepairCenter specialistRepairCenter = specRepairSystem.getByID(partRepair.getSpcID());
                bookingSPCName.setText(specialistRepairCenter.getName());
                deliveryDate.setText(format.format(partRepair.getDeliveryDate().toString()));
                returnDate.setText(format.format(partRepair.getReturnDate().toString()));
                bookingItemID.setText(Integer.toString(partRepair.getPartOccurrenceID()));
                bookingCost.setText(Double.toString(partRepair.getCost()));
                findBooking.setText(Integer.toString(partRepair.getBookingID()));
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            showAlert(e.getMessage());
        }
    }

    public void submitChanges() throws InvalidDateException {
        try {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date delDate;
            Date retDate;
            int spcID = Integer.parseInt(bookingSPCID.getText());
            delDate = dateFormat.parse(deliveryDate.getText());
            retDate = dateFormat.parse(returnDate.getText());
            double cost = Double.parseDouble(bookingCost.getText());
            if (delDate.before(retDate)) {
                if (vehicle_repair.isSelected()) {
                    VehicleRepair vehicleRepair = specRepairSystem.findVehicleRepairBooking(editBookingID);
                    String reg = bookingItemID.getText();
                    vehicleRepair.setVehicleRegNumber(reg);
                    vehicleRepair.setCost(cost);
                    vehicleRepair.setDeliveryDate(delDate);
                    vehicleRepair.setReturnDate(retDate);
                    vehicleRepair.setSpcID(spcID);
                    specRepairSystem.updateBookings(vehicleRepair);
                } else if (part_repair.isSelected()) {
                    PartRepair partRepair = specRepairSystem.findPartRepairBooking(editBookingID);
                    int partOccurrenceID = Integer.parseInt(bookingItemID.getText());
                    partRepair.setPartOccurrenceID(partOccurrenceID);
                    partRepair.setCost(cost);
                    partRepair.setDeliveryDate(delDate);
                    partRepair.setReturnDate(retDate);
                    partRepair.setSpcID(spcID);
                    specRepairSystem.updateBookings(partRepair);
                }
                promptFields();
            }
            else
            {
                throw new InvalidDateException("Error in dates, check that delivery date is NOT in the past and the return day is after the delivery date!");
            }
        }

        catch (ParseException | InvalidDateException e) {
            promptFields();
            showAlert(e.getMessage());
        }


    }*/

}