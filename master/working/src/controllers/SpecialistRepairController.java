package controllers;

import domain.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import logic.Criterion;
import logic.CriterionOperator;
import logic.PartsSystem;
import logic.SpecRepairSystem;
import persistence.DatabaseRepository;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;

import javax.xml.soap.Text;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SpecialistRepairController {


   DatabaseRepository databaseRepository = DatabaseRepository.getInstance();
   SpecRepairSystem specRepairSystem = SpecRepairSystem.getInstance(databaseRepository);
   PartsSystem partsSystem = PartsSystem.getInstance(databaseRepository);

    @FXML
    private TextField vehicle_reg;

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
    private ToggleGroup bookingType;

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
    void allowBooking() {

    }

    @FXML
    void deleteSRC() {

    }

    @FXML
    void updateSRC() {
    }

    @FXML
    void displayFields()
    {
        spcIDtoDelete.setVisible(false);
        new_spcName.setVisible(true);
        new_spcAdd.setVisible(true);
        new_spcPho.setVisible(true);
        new_spcEmail.setVisible(true);
    }

    @FXML
    void addSRC() {

        specRepairSystem.addRepairCenter(new_spcName.getText(), new_spcAdd.getText(), new_spcPho.getText(), new_spcEmail.getText());

    }

    @FXML
    public void searchVehiclesSRC()
   {

       if(veh_selected.isSelected())
       {

          List<VehicleRepair> vehicleRepairs =  specRepairSystem.getVehicleBookings(vehicle_reg.getText());
          //todo implement list into table view
           updateTableViewVehicleRepair(vehicleRepairs);
       }
       else
       {
           List<SpecialistRepairCenter> specialistRepairCenters = specRepairSystem.getAllBookings(vehicle_reg.getText());
           //todo implement list into table view
           updateTableViewSpecialistRepair(specialistRepairCenters);
       }
   }

   @FXML
   public void showID()
   {
       new_spcName.setVisible(false);
       new_spcAdd.setVisible(false);
       new_spcPho.setVisible(false);
       new_spcEmail.setVisible(false);
       spcIDtoDelete.setVisible(true);
       specRepairSystem.deleteRepairCenter(Integer.parseInt(spcIDtoDelete.getText()));
   }

   @FXML
   private void updateTableViewVehicleRepair(List<VehicleRepair> repairs)
   {
       specialistTable.getItems().clear();

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
        List<SpecRepBooking> specRepBookings = new ArrayList<>();
        List<VehicleRepair> vehicleRepairs = specRepairSystem.getOutstandingV();
        List<PartRepair> partRepairs = specRepairSystem.getOutstandingP();
        specRepBookings.addAll(vehicleRepairs);
        specRepBookings.addAll(partRepairs);
        updateTableViewSpecialistBooking(specRepBookings);

    }

}





