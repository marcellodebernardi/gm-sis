package controllers;

import entities.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import logic.Criterion;
import logic.CriterionOperator;
import java.util.List;

import logic.Searchable;
import persistence.DatabaseRepository;
public class SpecialistRepairController {

DatabaseRepository db = DatabaseRepository.getInstance();


    @FXML
    private TextField field_custy;

    @FXML
    private TextField field_SRCID;

    @FXML
    private TextField field_itemID;

    @FXML
    private TextField itemName;

    @FXML
    private TextField itemInfo;

    @FXML
    private TextField srID;

    @FXML
    private RadioButton r_vehicleDel;

    @FXML
    private RadioButton r_partDel;

    @FXML
    private Label vmod;

    @FXML
    private Label vmake;

    @FXML
    private RadioButton r_vehicle;

    @FXML
    private RadioButton r_part;

    @FXML
    private Label partN;

    @FXML
    private Label partDes;



    /**
     * Checks what type of booking specialist repair booking is (Vehicle or part)
     * Generates an object of the respective booking type, representing a specialist repair booking
     * Adds that booking to the database
     */
    @FXML
    void addSRBooking() {

        if(r_part != null && r_vehicle !=null)
        {

            if(r_vehicle.isSelected())
            {
                List<Vehicle> vehicles = db.getByCriteria(new Criterion<>(Vehicle.class,"regNumber",CriterionOperator.EqualTo,field_itemID.getText()));
                Vehicle vehicle = vehicles.get(0);
                VehicleRepair vehicleRepair = new VehicleRepair(Integer.parseInt(srID.getText()),null,null,20,-1,vehicle.getRegNumber());
                db.commitItem(vehicleRepair);
            }
            else
            {
                List<PartOccurrence> partOccurrences = db.getByCriteria(new Criterion<>(PartOccurrence.class, "partOccurrenceID", CriterionOperator.EqualTo,Integer.parseInt(field_itemID.getText())));
                PartOccurrence partOccurrence = partOccurrences.get(0);
                PartRepair partRepair = new PartRepair(Integer.parseInt(srID.getText()),null,null,20,-1,partOccurrence.getPartOccurrenceID());
                db.commitItem(partRepair);
            }
        }
        else
        {
            showAlert();
        }


    }

    @FXML
    void verifyBookingDate(ActionEvent event) {

    }

    @FXML
    void searchCusty()
    {
        List<Customer> custy = db.getByCriteria(new Criterion<>(Customer.class, "customerID", CriterionOperator.EqualTo,Integer.parseInt(field_custy.getText())));
        Customer customer = custy.get(0);
    }

    @FXML
    void searchItem()
    {

        if(r_vehicle.isSelected())
            {
            List<Vehicle> vehicles = db.getByCriteria(new Criterion<>(Vehicle.class, "regNumber",CriterionOperator.EqualTo,field_itemID.getText()));
            Vehicle vehicle = vehicles.get(0);
            partDes.setVisible(false);
            partN.setVisible(false);
            vmod.setVisible(true);
            vmake.setVisible(true);
            itemName.setText(vehicle.getManufacturer());
            itemInfo.setText(vehicle.getModel());
        }
        else if(r_part.isSelected())
        {
            List<PartAbstraction> partAbstractions = db.getByCriteria(new Criterion<>(PartAbstraction.class,"partAbstractionID",CriterionOperator.EqualTo,Integer.parseInt(field_itemID.getText())));
            PartAbstraction partAbstraction = partAbstractions.get(0);
            vmake.setVisible(false);
            vmod.setVisible(false);
            partN.setVisible(true);
            partDes.setVisible(true);
            itemName.setText(partAbstraction.getPartName());
            itemInfo.setText(partAbstraction.getPartDescription());
        }
        else
        {
            showAlert();
        }

    }

    @FXML
    void confirmDelete()
    {
       if(r_vehicleDel.isSelected())
       {
        db.deleteItem(new Criterion<>(VehicleRepair.class,"spcRepID", CriterionOperator.EqualTo,Integer.parseInt(field_SRCID.getText())));
       }
       if(r_partDel.isSelected())
       {
           db.deleteItem(new Criterion<>(PartRepair.class, "spcRepID",CriterionOperator.EqualTo,Integer.parseInt(field_SRCID.getText())));
       }
    }


    public void showAlert()
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Nothing selected!");
        alert.showAndWait();
    }

}





