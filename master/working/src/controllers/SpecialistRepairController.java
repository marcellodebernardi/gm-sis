package controllers;

import java.net.URL;
import java.util.ResourceBundle;
import entities.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import logic.Criterion;
import logic.CriterionOperator;
import java.util.List;
import persistence.DatabaseRepository;

public class SpecialistRepairController implements Initializable {

DatabaseRepository db = DatabaseRepository.getInstance();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField field_custy;

    @FXML
    private ComboBox<String> booking_type;

    @FXML
    private ComboBox<String> select_SRC;

    @FXML
    private TextField field_itemID;

    @FXML
    private TextField itemName;

    @FXML
    private TextField itemInfo;

    @FXML
    private Button btn_addSRbooking;

    @FXML
    private DatePicker bookingDate;

    @FXML
    private Label vreg;

    @FXML
    private Label vmod;

    @FXML
    private Label vmake;

    @FXML
    private Label partID;

    @FXML
    private Label partN;

    @FXML
    private Label partDes;

    @Override
    public void initialize(URL location, ResourceBundle  resources)
    {
     booking_type.getItems().addAll("Vehicle", "Part");
     vreg.setVisible(false);
     vmod.setVisible(false);
     vmake.setVisible(false);
     partID.setVisible(false);
     partN.setVisible(false);
     partDes.setVisible(false);
     btn_addSRbooking.setVisible(true);
     field_itemID.setVisible(true);
     field_custy.setVisible(true);

     select_SRC.getItems().addAll("someName","someOtherName");

        if(booking_type.getSelectionModel().getSelectedItem().equals("Vehicle"))
        {
            vreg.setVisible(true);
            vreg.setText("Please Enter a Vehicle Registration");
            vmake.setVisible(true);
            vmod.setVisible(true);
        }
        else
        {
            partID.setVisible(true);
            partID.setText("Please enter a Part ID");
            partN.setVisible(true);
            partDes.setVisible(true);

        }
    }

    /**
     * Checks what type of booking specialist repair booking is (Vehicle or part)
     * Generates an object of the respective booking type, representing a specialist repair booking
     * Adds that booking to the database
     */
    @FXML
    void addSRBooking() {

        if(booking_type.getSelectionModel()!=null)
        {
            List<SpecialistRepairCenter> specialistRepairCenters = db.getByCriteria(new Criterion<>(SpecialistRepairCenter.class,"name",CriterionOperator.EqualTo,select_SRC.getSelectionModel().getSelectedItem()));

            if(booking_type.getSelectionModel().getSelectedItem().equals("Vehicle"))
            {
                List<Vehicle> vehicles = db.getByCriteria(new Criterion<>(Vehicle.class,"regNumber",CriterionOperator.EqualTo,field_itemID.getText()));
                Vehicle vehicle = vehicles.get(0);
                SpecialistRepairCenter specialistRepairCenter = specialistRepairCenters.get(0);
                VehicleRepair vehicleRepair = new VehicleRepair(specialistRepairCenter.getSpcID(),null,null,20,-1,vehicle.getRegNumber());
                db.commitItem(vehicleRepair);
            }
            else
            {
                List<PartOccurrence> partOccurrences = db.getByCriteria(new Criterion<>(PartOccurrence.class, "partOccurrenceID", CriterionOperator.EqualTo,Integer.parseInt(field_itemID.getText())));
                PartOccurrence partOccurrence = partOccurrences.get(0);
                SpecialistRepairCenter specialistRepairCenter = specialistRepairCenters.get(0);
                PartRepair partRepair = new PartRepair(specialistRepairCenter.getSpcID(),null,null,20,-1,partOccurrence.getPartOccurrenceID());
                db.commitItem(partRepair);
            }
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

        if(booking_type.getSelectionModel().getSelectedItem().equals("Vehicle"))
        {
            List<Vehicle> vehicles = db.getByCriteria(new Criterion<>(Vehicle.class, "regNumber",CriterionOperator.EqualTo,field_itemID.getText()));
            Vehicle vehicle = vehicles.get(0);
            itemName.setText(vehicle.getManufacturer());
            itemInfo.setText(vehicle.getModel());
        }
        else
        {
            List<PartAbstraction> partAbstractions = db.getByCriteria(new Criterion<>(PartAbstraction.class,"partAbstractionID",CriterionOperator.EqualTo,Integer.parseInt(field_itemID.getText())));
            PartAbstraction partAbstraction = partAbstractions.get(0);
            itemName.setText(partAbstraction.getPartName());
            itemInfo.setText(partAbstraction.getPartDescription());
        }

    }

}





