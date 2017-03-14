package controllers.SRC;

import domain.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DoubleStringConverter;
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

    private int spcID;
    private String reg;
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

    @FXML
    private TableView<Vehicle> vehicleDetails;

    @FXML
    private TableColumn<Vehicle, String> veh_reg_column;

    @FXML
    private TableColumn<Vehicle, String> veh_make_column;

    @FXML
    private TableColumn<Vehicle, String> veh_model_column;

    @FXML
    private TableColumn<Vehicle, Integer> veh_mileage_column;
    @FXML
    private TableColumn<Vehicle, Double> veh_eng_column;

    @FXML
    private Label custy_label;

    @FXML
    private TextArea custyInfo;

    @FXML
    private ObservableList<Vehicle> vehicleObservableList = FXCollections.observableArrayList();

    @FXML
    private Button btn_hideDetails = new Button();


    public void showRepairs()
    {
        try {
            vehicleDetails.setVisible(true);
            custy_label.setVisible(true);
            custyInfo.setVisible(true);
            btn_hideDetails.setVisible(true);
            custyInfo.clear();
            showVehicle();
        }
        catch (NullPointerException e)
        {
            showAlert("No repair center selected.");
        }

    }

    private void showVehicle()
    {
        List<VehicleRepair> vehicleRepairs = specRepairSystem.getBySpcID(spcID);
        ArrayList<Vehicle> vehicles = new ArrayList<>();
        for(VehicleRepair v: vehicleRepairs)
        {
            vehicles.add(VehicleSys.getInstance().searchAVehicle(v.getVehicleRegNumber()));
        }
        vehicleDetails.getItems().clear();
        vehicleObservableList.addAll(vehicles);
        veh_reg_column.setCellValueFactory(new PropertyValueFactory<Vehicle, String>("regNumber"));
        veh_reg_column.setCellFactory(TextFieldTableCell.forTableColumn());
        veh_make_column.setCellValueFactory(new PropertyValueFactory<Vehicle, String>("manufacturer"));
        veh_make_column.setCellFactory(TextFieldTableCell.forTableColumn());
        veh_model_column.setCellValueFactory(new PropertyValueFactory<Vehicle, String>("model"));
        veh_model_column.setCellFactory(TextFieldTableCell.forTableColumn());
        veh_mileage_column.setCellValueFactory(new PropertyValueFactory<Vehicle, Integer>("mileage"));
        veh_mileage_column.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        veh_eng_column.setCellValueFactory(new PropertyValueFactory<Vehicle, Double>("engineSize"));
        veh_eng_column.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        vehicleDetails.setItems(vehicleObservableList);
    }

    public void showCustyDetails()
    {
        try {
            Vehicle vehicle = vehicleDetails.getSelectionModel().getSelectedItem();
            Customer customer = specRepairSystem.getByCustomerID(vehicle.getCustomerID());
            custyInfo.clear();
            custyInfo.appendText("Customer ID : " + customer.getCustomerAddress() + "\n");
            custyInfo.appendText("Customer first name : " + customer.getCustomerFirstname() + "\n");
            custyInfo.appendText("Customer surname : " + customer.getCustomerSurname() + "\n");
            custyInfo.appendText("Customer phone number : " + customer.getCustomerPhone() + "\n");
            custyInfo.appendText("Customer address : " + customer.getCustomerAddress() + "\n");
            custyInfo.appendText("Customer postcode : " + customer.getCustomerPostcode() + "\n");
            custyInfo.appendText("Customer type : " + customer.getCustomerType().toString() + "\n");
        }
        catch (NullPointerException e)
        {

        }

    }

    public void hideDetails()
    {
        vehicleDetails.setVisible(false);
        custy_label.setVisible(false);
        custyInfo.setVisible(false);
        btn_hideDetails.setVisible(false);
    }

}