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
import persistence.DatabaseRepository;

import java.util.List;

public class SpecialistRepairController {

    DatabaseRepository db = DatabaseRepository.getInstance();


    @FXML
    private TextField field_custy, field_SRCID, field_itemID, itemName, itemInfo, srID, srcBookingID, srcID, deliveryDate, returnDate, itemID, SRBookingID, cost;

    @FXML
    private RadioButton r_vehicleDel, r_partDel, r_vehicle, r_part, r_vehicleEdit, r_partEdit;

    @FXML
    private Label veh_lbl, part_lbl, partN, vmake, vmod, partDes;

    /**
     * Checks what type of booking specialist repair booking is (Vehicle or part)
     * Generates an object of the respective booking type, representing a specialist repair booking
     * Adds that booking to the database
     */
    @FXML
    void addSRBooking() {

        if (r_part != null && r_vehicle != null) {

            if (r_vehicle.isSelected()) {
                List<Vehicle> vehicles = db.getByCriteria(new Criterion<>(Vehicle.class, "regNumber", CriterionOperator.EqualTo, field_itemID.getText()));
                Vehicle vehicle = vehicles.get(0);
                VehicleRepair vehicleRepair = new VehicleRepair(Integer.parseInt(srID.getText()), null, null, 20, -1, vehicle.getRegNumber());
                db.commitItem(vehicleRepair);
            } else {
                List<PartOccurrence> partOccurrences = db.getByCriteria(new Criterion<>(PartOccurrence.class, "partOccurrenceID", CriterionOperator.EqualTo, Integer.parseInt(field_itemID.getText())));
                PartOccurrence partOccurrence = partOccurrences.get(0);
                PartRepair partRepair = new PartRepair(Integer.parseInt(srID.getText()), null, null, 20, -1, partOccurrence.getPartOccurrenceID());
                db.commitItem(partRepair);
            }
        } else {
            showAlert();
        }


    }

    @FXML
    void verifyBookingDate(ActionEvent event) {

    }

    @FXML
    void searchCusty() {
        List<Customer> custy = db.getByCriteria(new Criterion<>(Customer.class, "customerID", CriterionOperator.EqualTo, Integer.parseInt(field_custy.getText())));
        Customer customer = custy.get(0);
    }

    @FXML
    void searchItem() {

        if (r_vehicle.isSelected()) {
            List<Vehicle> vehicles = db.getByCriteria(new Criterion<>(Vehicle.class, "regNumber", CriterionOperator.EqualTo, field_itemID.getText()));
            Vehicle vehicle = vehicles.get(0);
            partDes.setVisible(false);
            partN.setVisible(false);
            vmod.setVisible(true);
            vmake.setVisible(true);
            itemName.setText(vehicle.getManufacturer());
            itemInfo.setText(vehicle.getModel());
        } else if (r_part.isSelected()) {
            List<PartAbstraction> partAbstractions = db.getByCriteria(new Criterion<>(PartAbstraction.class, "partAbstractionID", CriterionOperator.EqualTo, Integer.parseInt(field_itemID.getText())));
            PartAbstraction partAbstraction = partAbstractions.get(0);
            vmake.setVisible(false);
            vmod.setVisible(false);
            partN.setVisible(true);
            partDes.setVisible(true);
            itemName.setText(partAbstraction.getPartName());
            itemInfo.setText(partAbstraction.getPartDescription());
        } else {
            showAlert();
        }

    }

    @FXML
    void confirmDelete() {
        if (r_vehicleDel.isSelected()) {
            db.deleteItem(new Criterion<>(VehicleRepair.class, "spcRepID", CriterionOperator.EqualTo, Integer.parseInt(field_SRCID.getText())));
        }
        if (r_partDel.isSelected()) {
            db.deleteItem(new Criterion<>(PartRepair.class, "spcRepID", CriterionOperator.EqualTo, Integer.parseInt(field_SRCID.getText())));
        }
    }


    public void showAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Nothing selected!");
        alert.showAndWait();
    }

    public void searchSRC() {
        List<SpecialistRepairCenter> specialistRepairCenters = db.getByCriteria(new Criterion<>(SpecialistRepairCenter.class, "spcID", CriterionOperator.EqualTo, srID.getText()));
        SpecialistRepairCenter specialistRepairCenter = specialistRepairCenters.get(0);
        field_SRCID.setText(Integer.toString(specialistRepairCenter.getSpcID()));

    }

    //todo Implement way of extracting date to String
    public void searchSRCBookings() {
        if (r_vehicleEdit.isSelected()) {
            part_lbl.setVisible(false);
            veh_lbl.setVisible(true);
            List<VehicleRepair> vehicleRepairs = db.getByCriteria(new Criterion<>(VehicleRepair.class, "spcRepID", CriterionOperator.EqualTo, Integer.parseInt(srcBookingID.getText())));
            VehicleRepair vehicleRepair = vehicleRepairs.get(0);
            srcID.setText(Integer.toString(vehicleRepair.getSpcID()));
            deliveryDate.setText("");
            returnDate.setText("");
            cost.setText(Double.toString(vehicleRepair.getCost()));
            SRBookingID.setText(Integer.toString(vehicleRepair.getBookingID()));
            itemID.setText(vehicleRepair.getVehicleRegNumber());
        } else if (r_partEdit.isSelected()) {
            veh_lbl.setVisible(false);
            part_lbl.setVisible(true);
            List<PartRepair> partRepairs = db.getByCriteria(new Criterion<>(PartRepair.class, "spcRepID", CriterionOperator.EqualTo, Integer.parseInt(srcBookingID.getText())));
            PartRepair partRepair = partRepairs.get(0);
            srcID.setText(Integer.toString(partRepair.getSpcID()));
            deliveryDate.setText("");
            returnDate.setText("");
            cost.setText(Double.toString(partRepair.getCost()));
            SRBookingID.setText(Integer.toString(partRepair.getBookingID()));
            itemID.setText(Integer.toString(partRepair.getPartOccurrenceID()));
        }

    }

    public void submitChanges() {
        if (r_vehicleEdit.isSelected()) {
            VehicleRepair vehicleRepair = new VehicleRepair(Integer.parseInt(srcID.getText()), null, null, Double.parseDouble(cost.getText()), Integer.parseInt(SRBookingID.getText()), itemID.getText());
            db.commitItem(vehicleRepair);
        } else if (r_partEdit.isSelected()) {
            PartRepair partRepair = new PartRepair(Integer.parseInt(srcID.getText()), null, null, Double.parseDouble(cost.getText()), Integer.parseInt(SRBookingID.getText()), Integer.parseInt(itemID.getText()));
            db.commitItem(partRepair);
        }
    }

}





