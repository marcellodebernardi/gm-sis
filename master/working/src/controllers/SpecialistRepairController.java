package controllers;

import domain.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import logic.*;
import persistence.DatabaseRepository;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SpecialistRepairController {


    private DatabaseRepository databaseRepository = DatabaseRepository.getInstance();
    private SpecRepairSystem specRepairSystem = SpecRepairSystem.getInstance(databaseRepository);
    private PartsSystem partsSystem = PartsSystem.getInstance(databaseRepository);
    private AuthenticationSystem authenticationSystem = AuthenticationSystem.getInstance();
    private VehicleSys vehicleSys = VehicleSys.getInstance();


    @FXML
    private RadioButton vehicle_repair,part_repair;

    @FXML
    private TableView<SpecRepBooking> specialistTable;

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
*/
    @FXML
    private RadioButton veh_selected,src_selected;

    @FXML
    private Button btn_add_src, btn_del_src, btn_edit_src,btn_updateSRC,btn_deleteSRC,btn_submitSRC,findSRCToEdit,btn_edit_booking, btn_add_booking, btn_del_booking, delete_booking,searchForSRCBooking,search_src_deletion, cancelDeletion, findBookingToEdit, applyEditingChanges;

    private int spcIDEdit;

    private int editBookingID;

    @FXML
    private TextField returnDate,deliveryDate,bookingCost,spcToEdit,spcIDtoDelete,new_spcEmail,new_spcPho,new_spcAdd,new_spcName,part_name,part_cost,stock_level,part_des,instaDate,bookingSPCID,partAbsID,partOccID,bookingItemID,bookingSPCName,itemToSearch,edit_booking_ID;

    private final ObservableList tableEntries = FXCollections.observableArrayList();

   /* @FXML
    private TableColumn<SpecRepBooking, String> TbookingTableItem, TbookingItemName, TbookingItemType, TbookingSRCName ;*/

    @FXML
    private TableColumn<SpecRepBooking, Integer> TbookingSRC;


    /*@FXML
    private void updateTableViewSpecialistBooking(List<SpecRepBooking> specRepBookings)
    {
        specialistTable.getItems().clear();
        tableEntries.removeAll(tableEntries);
        for (SpecRepBooking specRepBooking: specRepBookings)
        {
            tableEntries.add(specRepBooking);
        }
        TbookingTableItem.setCellValueFactory(new PropertyValueFactory<SpecRepBooking, String>("spcRepID"));
        TbookingTableItem.setCellFactory(TextFieldTableCell.<SpecRepBooking>forTableColumn());


    }*/

    /**
     * Search a specific SRC to edit
     */
    public void searchSRCToEdit()
    {
        spcIDEdit = Integer.parseInt(spcToEdit.getText());
        SpecialistRepairCenter specialistRepairCenter =  specRepairSystem.getByID(Integer.parseInt(spcToEdit.getText()));
        new_spcName.setText(specialistRepairCenter.getName());
        new_spcAdd.setText(specialistRepairCenter.getAddress());
        new_spcPho.setText(specialistRepairCenter.getPhone());
        new_spcEmail.setText(specialistRepairCenter.getEmail());
        new_spcName.setEditable(true);
        new_spcAdd.setEditable(true);
        new_spcPho.setEditable(true);
        new_spcEmail.setEditable(true);

    }

    /**
     * Allows a booking to be made
     * todo get part repairs working
     * completed Vehicle repairs
     */
    @FXML
    void allowBooking()
    {
        try {
            SpecialistRepairCenter specialistRepairCenter = specRepairSystem.getByID(Integer.parseInt(bookingSPCID.getText()));
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            Date dD = format.parse(deliveryDate.getText());
            Date rD = format.parse(returnDate.getText());
            if(dD.before(rD) && ! (dD.compareTo(new Date()) == -1)) {
                if (vehicle_repair.isSelected()) {
                    if(vehicleSys.VehicleExists(bookingItemID.getText()))
                    {
                        VehicleRepair vehicleRepair = new VehicleRepair(Integer.parseInt(bookingSPCID.getText()), dD, rD, Double.parseDouble(bookingCost.getText()), 2, bookingItemID.getText());
                        specialistRepairCenter.addToBooking(vehicleRepair);
                        specRepairSystem.updateRepairCentre(specialistRepairCenter);
                        specRepairSystem.addSpecialistBooking(vehicleRepair);
                    }
                    else
                    {
                        showAlert("This vehicle is not registered in our system, please register the vehicle : " + bookingItemID.getText() + " before continuing.");
                    }

                }
                if (part_repair.isSelected()) {
                    PartRepair partRepair = new PartRepair(Integer.parseInt(bookingSPCID.getText()), dD, rD, Double.parseDouble(bookingCost.getText()), 2, Integer.parseInt(bookingItemID.getText()));
                    specialistRepairCenter.addToBooking(partRepair);
                    specRepairSystem.updateRepairCentre(specialistRepairCenter);
                    specRepairSystem.addSpecialistBooking(partRepair);
                }
            }
            else
            {
                throw new InvalidDateException("Return date is before delivery date!");
            }
        }
        catch (ParseException | InvalidDateException e)
        {
            showAlert(e.getMessage());
        }

    }

    /**
     * Shows a relevant alert message
     *
     *
     * @param message message displayed to user
     */
    @FXML
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Message");
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    /**
     * Method used to search for an SRC for a specialist repair booking
     */
    @FXML
    public void findSRCForBooking()
    {
            SpecialistRepairCenter specialistRepairCenter = specRepairSystem.getByID(Integer.parseInt(bookingSPCID.getText()));
            bookingSPCName.setText(specialistRepairCenter.getName());
            bookingSPCName.setEditable(false);
    }

    /**
     * Deletes an SRC
     * todo implement a confirmation message
     */
    @FXML
    public void deleteSRC()
    {
        specRepairSystem.deleteAllSubsequentBookings(Integer.parseInt(spcIDtoDelete.getText()));
        specRepairSystem.deleteRepairCenter(Integer.parseInt(spcIDtoDelete.getText()));
    }

    public void confirmDelete(int spcToDelete) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setContentText("Are you sure you want to delete this SRC?");
        Button button = new Button("Yes");
        Button no = new Button("Yes");
        button.setVisible(true);
        no.setVisible(true);
    }


    /**
     * Updates the SRC upon making a few checks
     */
    @FXML
    public void updateSRC()
    {
        String spcName = new_spcName.getText();
        String spcAdd = new_spcAdd.getText();
        String spcPho = new_spcPho.getText();
        String spcEmail = new_spcEmail.getText();
        SpecialistRepairCenter specialistRepairCenter = specRepairSystem.getByID(spcIDEdit);
        if(!spcName.equals("") || !spcAdd.equals("") || !spcPho.equals("") && spcPho.trim().length() == 11 || spcEmail.equals("")) {
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
     */
    @FXML
    void displayFields()
    {
        if(!(authenticationSystem.getUserType().equals(UserType.ADMINISTRATOR)))
        {
            btn_add_src.setDisable(true);
        }
        else {
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
     */
    @FXML
    void displayFieldsToEdit()
    {
        if(!(authenticationSystem.getUserType().equals(UserType.ADMINISTRATOR)))
        {
            btn_edit_src.setDisable(true);
        }
        else
        {
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
     */
    @FXML
    void addSRC()
    {
        String spcName = new_spcName.getText();
        String spcAdd = new_spcAdd.getText();
        String spcPho = new_spcPho.getText();
        String spcEmail = new_spcEmail.getText();
        if(!spcName.equals("") || !spcAdd.equals("") || !spcPho.equals("") && spcPho.trim().length() == 11 || spcEmail.equals("")) {
            specRepairSystem.addRepairCenter(spcName, spcAdd, spcPho, spcEmail);
            new_spcName.clear();
            new_spcAdd.clear();
            new_spcPho.clear();
            new_spcEmail.clear();
        }
    }

    /**
     * Search for a vehicle in db of specialist repair bookings
     */
    @FXML
    public void searchVehiclesSRC()
    {

        if(veh_selected.isSelected())
        {

            List<VehicleRepair> vehicleRepairs =  specRepairSystem.getVehicleBookings(itemToSearch.getText());
            //todo implement list into table view
            updateTableViewVehicleRepair(vehicleRepairs);
        }
        else if(src_selected.isSelected())
        {
            List<SpecialistRepairCenter> specialistRepairCenters = specRepairSystem.getAllBookings(itemToSearch.getText());
            //todo implement list into table view
            updateTableViewSpecialistRepair(specialistRepairCenters);
        }
    }

    /**
     * Displays relevant fields for deletion of an SRC, hides all other irrelevant parts
     * todo implement system which will ensure it only shows when user is administrator
     */
    @FXML
    public void showID()
    {
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
    private <E> void updateTableViewVehicleRepair(List<E> repairs)
    {

    }

    @FXML
    private void updateTableViewSpecialistRepair(List<SpecialistRepairCenter> specialistRepairCenters)
    {
        specialistTable.getItems().clear();

    }



    /**
     * Submits part changes on a specific part
     */
    @FXML
    public void submitPartsChanges()
    {
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
     */
    @FXML
    public void allowEdit()
    {
        partAbsID.setEditable(true);
        partOccID.setEditable(true);
        instaDate.setEditable(true);
        instID.setEditable(true);
    }

    /**
     * ADDS A PART TO THE DB
     * todo needs to be fixed - Talk to Shakib
     */
    @FXML
    public void addPart()
    {
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
        partsSystem.addPart(part_name.getText(),part_des.getText(),Double.parseDouble(part_cost.getText())*Integer.parseInt(stock_level.getText()),Integer.parseInt(stock_level.getText()));
        part_cost.clear();
        part_des.clear();
        stock_level.clear();
        part_cost.setVisible(false);
        part_des.setVisible(false);
        stock_level.setVisible(false);

    }

    /**
     * SHOWS ALL THE OUTSTANDING ITEMS AT REPAIR CENTERS
     */
    @FXML
    public void getOutStandingItems()
    {
        Date todaysDate = new Date();
        List<SpecRepBooking> specRepBookings = new ArrayList<>();
        List<VehicleRepair> vehicleRepairs = specRepairSystem.getOutstandingV(todaysDate);
        List<PartRepair> partRepairs = specRepairSystem.getOutstandingP(todaysDate);
        specRepBookings.addAll(vehicleRepairs);
        specRepBookings.addAll(partRepairs);
        //updateTableViewSpecialistBooking(specRepBookings);

    }

    @FXML
    public void showDeletionField()
    {
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
    }

    /**
     * Allows user to delete a booking
     * Specified booking through SPC ID, Item(part or vehicle) ID(reg or occurrence ID) and date
     * @throws InvalidDateException
     */
    @FXML
    public void deleteBooking() throws InvalidDateException
    {
        try {

            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = dateFormat.parse(deliveryDate.getText());
            if(date.compareTo(new Date())==-1)
            {
                throw new InvalidDateException("This booking has already been done.");
            }
            if (vehicle_repair.isSelected()) {
               if( specRepairSystem.deleteVehicleRepair(bookingItemID.getText(), spcIDEdit, date))
                showAlert("Deletion confirmed");
               else
               {
                   showAlert("Deletion failed, check fields");
               }

            }
            else if(part_repair.isSelected())
            {
                if(specRepairSystem.deletePartRepair(Integer.parseInt(bookingItemID.getText()),spcIDEdit,date))
                showAlert("Deletion confirmed");
                else
                {
                    showAlert("Deletion failed, check fields");
                }

            }
            promptFields();

        }
        catch (ParseException | InvalidDateException e)
        {
            promptFields();
            showAlert(e.getMessage());
        }
    }

    /**
     * Search the SRC Responsible for repair for deletion and set text for SRC Name
     */
    @FXML
    public void searchSRCDeletion()
    {
        SpecialistRepairCenter specialistRepairCenter = specRepairSystem.getByID(Integer.parseInt(bookingSPCID.getText()));
        spcIDEdit = specialistRepairCenter.getSpcID();
        bookingSPCName.setText(specialistRepairCenter.getName());
    }

    /**
     * Reset fields
     */
    @FXML
    public void promptFields()
    {
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

    }

    @FXML
    public void showEditingFields()
    {
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

    }

    @FXML
    public  void findBooking()
    {
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String StringDeliveryDate = "";
        String StringReturnDate = "";
        applyEditingChanges.setVisible(true);
        if(vehicle_repair.isSelected())
        {
            VehicleRepair vehicleRepair = specRepairSystem.findVehicleRepairBooking(Integer.parseInt(edit_booking_ID.getText()));
            editBookingID = vehicleRepair.getSpcRepID();
            bookingSPCID.setText(Integer.toString(vehicleRepair.getSpcID()));
            SpecialistRepairCenter specialistRepairCenter = specRepairSystem.getByID(vehicleRepair.getSpcID());
            bookingSPCName.setText(specialistRepairCenter.getName());
            //todo check why it returns 1970-01-01 instead of actual booking date
            System.out.println(vehicleRepair.getDeliveryDate());
            StringDeliveryDate = format.format(vehicleRepair.getDeliveryDate());
            deliveryDate.setText(StringDeliveryDate);
            bookingItemID.setText(vehicleRepair.getVehicleRegNumber());
            StringReturnDate = format.format(vehicleRepair.getReturnDate());
            returnDate.setText(StringReturnDate);
        }
        else if(part_repair.isSelected())
        {
            PartRepair partRepair = specRepairSystem.findPartRepairBooking(Integer.parseInt(edit_booking_ID.getText()));
            bookingSPCID.setText(Integer.toString(partRepair.getSpcID()));
            SpecialistRepairCenter specialistRepairCenter = specRepairSystem.getByID(partRepair.getSpcID());
            bookingSPCName.setText(specialistRepairCenter.getName());
            StringDeliveryDate = format.format(partRepair.getDeliveryDate());
            //todo check why it returns 1970-01-01 instead of actual booking date
            deliveryDate.setText(StringDeliveryDate);
            bookingItemID.setText(Integer.toString(partRepair.getPartOccurrenceID()));
            StringReturnDate = format.format(partRepair.getReturnDate());
            returnDate.setText(StringReturnDate);
        }
    }

    public void submitChanges() throws InvalidDateException
    {
        try {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date delDate;
            Date retDate;
            int spcID = Integer.parseInt(bookingSPCID.getText());
            SpecialistRepairCenter specialistRepairCenter = specRepairSystem.getByID(spcID);
            delDate = dateFormat.parse(deliveryDate.getText());
            retDate = dateFormat.parse(returnDate.getText());
            double cost = Double.parseDouble(bookingCost.getText());

            if (vehicle_repair.isSelected())
            {
                VehicleRepair vehicleRepair = specRepairSystem.findVehicleRepairBooking(editBookingID);

                String reg = bookingItemID.getText();
                if(!(delDate.compareTo(retDate) == 1 )&&  !(delDate.compareTo(new Date())==-1)
                        && specialistRepairCenter!=null && (!(cost <=-1) && !reg.equals("")))
                {
                    vehicleRepair.setVehicleRegNumber(reg);
                    vehicleRepair.setCost(cost);
                    vehicleRepair.setDeliveryDate(delDate);
                    vehicleRepair.setReturnDate(retDate);
                    vehicleRepair.setSpcID(spcID);
                    specRepairSystem.updateBookings(vehicleRepair);
                }
                else
                {
                 throw new InvalidDateException("Invalid delivery date or return date!");
                }

            }

            else if (part_repair.isSelected())
            {
                PartRepair partRepair = specRepairSystem.findPartRepairBooking(editBookingID);
                int partOccurrenceID = Integer.parseInt(bookingItemID.getText());
                if(!(delDate.compareTo(retDate) == 1 )&&  !(delDate.compareTo(new Date())==-1)
                        && specialistRepairCenter!=null && !(cost <=-1) && partOccurrenceID!=-1)
                {
                        partRepair.setPartOccurrenceID(Integer.parseInt(bookingItemID.getText()));
                        partRepair.setCost(cost);
                        partRepair.setDeliveryDate(delDate);
                        partRepair.setReturnDate(retDate);
                        partRepair.setSpcID(spcID);
                        specRepairSystem.updateBookings(partRepair);
                }
                else
                {
                    throw new InvalidDateException("Invalid delivery date or return date!");
                }

            }
            promptFields();
        }
        catch (ParseException | InvalidDateException e)
        {
            promptFields();
            showAlert(e.getMessage());
        }


    }



}