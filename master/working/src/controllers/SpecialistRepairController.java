package controllers;

import domain.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.converter.LocalDateStringConverter;
import logic.AuthenticationSystem;
import logic.InvalidDateException;
import logic.PartsSystem;
import logic.SpecRepairSystem;
import org.joda.time.LocalDate;
import persistence.DatabaseRepository;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import org.joda.time.LocalDate;
import java.util.Date;
import java.util.List;

public class SpecialistRepairController {


   DatabaseRepository databaseRepository = DatabaseRepository.getInstance();
   SpecRepairSystem specRepairSystem = SpecRepairSystem.getInstance(databaseRepository);
   PartsSystem partsSystem = PartsSystem.getInstance(databaseRepository);
   AuthenticationSystem authenticationSystem = AuthenticationSystem.getInstance();

    @FXML
    private TextField bookingSPCID;

    @FXML
    private TextField bookingSPCName;

    @FXML
    private RadioButton vehicle_repair = new RadioButton();

    @FXML
    private ToggleGroup bookingType;

    @FXML
    private RadioButton part_repair = new RadioButton();

    @FXML
    private TextField bookingItemID;

    @FXML
    private DatePicker bookingReturnDate;

    @FXML
    private TextField itemToSearch;

    @FXML
    private TableView<?> specialistTable;

    @FXML
    private ToggleGroup searchCategory;

    @FXML
    private TextField partAbsID;// holds part ID

    @FXML
    private TextField partOccID;//holds part Occurence ID

    @FXML
    private TextField instID;//holds installationID

    @FXML
    private TextField instaDate;//holds installation date


    @FXML
    private TextField reg_number;

    @FXML
    private TextField spcID;

    @FXML
    private RadioButton veh_selected = new RadioButton();

    @FXML
    private RadioButton src_selected = new RadioButton();

    @FXML
    private TextField part_des;

    @FXML
    private TextField stock_level;

    @FXML
    private TextField part_cost;

    @FXML
    private TextField part_name;

    @FXML
    private TextField new_spcName;

    @FXML
    private TextField new_spcAdd;

    @FXML
    private TextField new_spcPho;

    @FXML
    private TextField new_spcEmail;

    @FXML
    private TextField spcIDtoDelete;

    @FXML
    private Button findSRCToEdit = new Button();

    @FXML
    private Button btn_submitSRC = new Button();

    @FXML
    private Button btn_deleteSRC = new Button();

    @FXML
    private Button btn_updateSRC = new Button();

    @FXML
    private TextField spcToEdit;

    @FXML
    private  TextField bookingCost;

    @FXML
    private Button btn_add_src, btn_del_src, btn_edit_src;

    @FXML
    private int spcIDEdit;

    @FXML
    private TextField returnDate;

    @FXML
    private TextField deliveryDate;

    public void searchSRCToEdit()
    {
        spcIDEdit = Integer.parseInt(spcToEdit.getText());
       List<SpecialistRepairCenter> specialistRepairCenters = specRepairSystem.getByID(Integer.parseInt(spcToEdit.getText()));
       SpecialistRepairCenter specialistRepairCenter = specialistRepairCenters.get(0);
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
    void allowBooking()
    {
        try {
            List<SpecialistRepairCenter> specialistRepairCenters = specRepairSystem.getByID(Integer.parseInt(bookingSPCID.getText()));
            SpecialistRepairCenter specialistRepairCenter = specialistRepairCenters.get(0);
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            Date dD = format.parse(deliveryDate.getText());
            Date rD = format.parse(returnDate.getText());
            if(dD.before(rD)) {
                if (vehicle_repair.isSelected()) {
                    {
                        VehicleRepair vehicleRepair = new VehicleRepair(Integer.parseInt(bookingSPCID.getText()), dD, rD, Double.parseDouble(bookingCost.getText()), 2, bookingItemID.getText());
                        specialistRepairCenter.addToBooking(vehicleRepair);
                        specRepairSystem.updateRepairCentre(specialistRepairCenter);
                        specRepairSystem.addSpecialistBooking(vehicleRepair);
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
            e.printStackTrace();
        }

    }

    @FXML
    public void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Message");
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    @FXML
    void findSRCForBooking(){
        List<SpecialistRepairCenter> specialistRepairCenters = specRepairSystem.getByID(Integer.parseInt(bookingSPCID.getText()));
        SpecialistRepairCenter specialistRepairCenter = specialistRepairCenters.get(0);
        bookingSPCName.setText(specialistRepairCenter.getName());
        bookingSPCName.setEditable(false);
    }

    @FXML
    void deleteSRC() {
        specRepairSystem.deleteRepairCenter(Integer.parseInt(spcIDtoDelete.getText()));
    }

    @FXML
    void updateSRC() {
        String spcName = new_spcName.getText();
        String spcAdd = new_spcAdd.getText();
        String spcPho = new_spcPho.getText();
        String spcEmail = new_spcEmail.getText();
        List<SpecialistRepairCenter> specialistRepairCenters = specRepairSystem.getByID(spcIDEdit);
        SpecialistRepairCenter specialistRepairCenter = specialistRepairCenters.get(0);
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

    @FXML
    void addSRC() {
        specRepairSystem.addRepairCenter(new_spcName.getText(), new_spcAdd.getText(), new_spcPho.getText(), new_spcEmail.getText());
        new_spcName.clear();
        new_spcAdd.clear();
        new_spcPho.clear();
        new_spcEmail.clear();
    }

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

    @FXML
    private void updateTableViewSpecialistBooking(List<SpecRepBooking> specRepBookingps)
    {
        specialistTable.getItems().clear();
    }

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

   @FXML
    public void allowEdit()
   {
       partAbsID.setEditable(true);
       partOccID.setEditable(true);
       instaDate.setEditable(true);
       instID.setEditable(true);
   }

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

    @FXML
        public void getOutStandingItems()
    {
        Date todaysDate = new Date();
        List<SpecRepBooking> specRepBookings = new ArrayList<>();
        List<VehicleRepair> vehicleRepairs = specRepairSystem.getOutstandingV(todaysDate);
        List<PartRepair> partRepairs = specRepairSystem.getOutstandingP(todaysDate);
        specRepBookings.addAll(vehicleRepairs);
        specRepBookings.addAll(partRepairs);
        updateTableViewSpecialistBooking(specRepBookings);

    }

}





