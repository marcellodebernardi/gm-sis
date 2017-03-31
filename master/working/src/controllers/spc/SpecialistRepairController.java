package controllers.spc;

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
import logic.authentication.AuthenticationSystem;
import logic.customer.CustomerSystem;
import logic.spc.SpecRepairSystem;
import logic.vehicle.VehicleSys;
import persistence.DatabaseRepository;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SpecialistRepairController implements Initializable {


    private DatabaseRepository databaseRepository = DatabaseRepository.getInstance();
    private SpecRepairSystem specRepairSystem = SpecRepairSystem.getInstance(databaseRepository);

    private int spcID;
    private String reg;
    @FXML
    private Button btn_addSRC, btn_deleteSRC, btn_updateSRC = new Button();
    @FXML
    private Label id_lbl, name_lbl, address_lbl, number_lbl, email_lbl = new Label();
    private boolean isShowing;
    @FXML
    private TableView<SpecialistRepairCenter> SpecialistRepairCenters;
    @FXML
    private TableColumn<SpecialistRepairCenter, Integer> spc_id_column;
    @FXML
    private TableColumn<SpecialistRepairCenter, String> spc_name_column, spc_phone_column, spc_address_column, spc_email_column = new TableColumn<>();
    @FXML
    private TextField src_id, src_name, src_address, src_email, src_phone = new TextField();
    @FXML
    private ObservableList<SpecialistRepairCenter> specialistRepairCenterObservableList = FXCollections.observableArrayList();
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (AuthenticationSystem.getInstance().getUserType().equals(UserType.NORMAL)) {
            btn_addSRC.setDisable(true);
            btn_updateSRC.setDisable(true);
            btn_deleteSRC.setDisable(true);
        }
        findSRC();

    }

    @FXML
    private void findSRC() {

        displayCentersToTable(specRepairSystem.getRepairCenterList());


    }

    private void displayCentersToTable(List<SpecialistRepairCenter> specialistRepairCenters) {
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

    public void showSpcDetails() {
        if (isShowing) {
            showRepairs();
        }
        SpecialistRepairCenter specialistRepairCenter = SpecialistRepairCenters.getSelectionModel().getSelectedItem();
        src_id.setText(Integer.toString(specialistRepairCenter.getSpcID()));
        src_name.setText(specialistRepairCenter.getName());
        src_phone.setText(specialistRepairCenter.getPhone());
        src_email.setText(specialistRepairCenter.getEmail());
        src_address.setText(specialistRepairCenter.getAddress());
        src_name.setText(specialistRepairCenter.getName());
        spcID = specialistRepairCenter.getSpcID();
        clearLabels();
    }

    public void clearSRCFields() {
        src_id.clear();
        src_name.clear();
        src_phone.clear();
        src_address.clear();
        src_email.clear();
        clearLabels();
    }

    public void addNewSRC() {
        try {
            if (src_phone.getText().length() != 11) {
                number_lbl.setVisible(true);
            }
            else {
                number_lbl.setVisible(false);
            }
        }
        catch (NumberFormatException e) {
            number_lbl.setVisible(true);
        }
        if (!src_id.getText().equals("")) {
            id_lbl.setVisible(true);
        }
        else {
            id_lbl.setVisible(false);
        }
        if (src_address.getText().equals("")) {
            address_lbl.setVisible(true);
        }
        else {
            address_lbl.setVisible(false);
        }
        if (src_email.getText().equals("") || !src_email.getText().contains("@")) {
            email_lbl.setVisible(true);
        }
        else {
            email_lbl.setVisible(false);
        }
        if (src_name.getText().equals("")) {
            name_lbl.setVisible(true);
        }
        else {
            name_lbl.setVisible(false);
        }
        if (src_phone.getText().length() == 11 && !src_name.getText().equals("") && !src_address.equals("") && !src_email.equals("") && src_email.getText().contains("@")) {
            if (addConfirmation()) {
                specRepairSystem.addRepairCenter(src_name.getText(), src_address.getText(), src_phone.getText(), src_email.getText());
                clearLabels();
                clearSRCFields();
            }
            findSRC();


        }

        findSRC();
    }

    private void clearLabels() {
        id_lbl.setVisible(false);
        name_lbl.setVisible(false);
        number_lbl.setVisible(false);
        address_lbl.setVisible(false);
        email_lbl.setVisible(false);
    }

    public void deleteSRC() {
        try {
            if (deleteConfirmation("Are you sure you want to delete this Specialist repair center?")) {
                specRepairSystem.deleteAllSubsequentBookings(spcID);
                specRepairSystem.deleteRepairCenter(spcID);
                clearSRCFields();
                clearLabels();
            }
        }
        catch (NullPointerException e) {
            showAlert("No SPC Selected.");
        }
        findSRC();
        clearLabels();
    }

    public void updateSRC() {
        SpecialistRepairCenter specialistRepairCenter = specRepairSystem.getByID(spcID);
        if (updateConfirmation()) {
            if (!specialistRepairCenter.setEmail(src_email.getText())) {
                showAlert("Please enter a correct email address.");
            }
            if (!specialistRepairCenter.setName(src_name.getText())) {
                showAlert("Please enter an appropriate name.");
            }
            if (!specialistRepairCenter.setAddress(src_address.getText())) {
                showAlert("Please enter an appropriate address.");
            }
            if (!specialistRepairCenter.setPhone(src_phone.getText().trim())) {
                showAlert("Please enter a correct 11 digit, numerical phone number.");
            }
            specRepairSystem.updateRepairCentre(specialistRepairCenter);
        }
        clearSRCFields();
        findSRC();
        clearLabels();
    }

    private boolean addConfirmation() {
        Alert addAlert = new Alert(Alert.AlertType.CONFIRMATION);
        addAlert.setTitle("Add Specialist Repair Center");
        addAlert.setHeaderText("Are you sure you want to add this center?");
        addAlert.showAndWait();
        return addAlert.getResult() == ButtonType.OK;

    }

    @FXML
    @SuppressWarnings("Duplicates")
    private boolean deleteConfirmation(String message) {
        Alert deleteAlert = new Alert(Alert.AlertType.CONFIRMATION);
        deleteAlert.setTitle("Delete Booking");
        deleteAlert.setHeaderText(message);
        deleteAlert.showAndWait();
        System.out.println(deleteAlert.getResult() == ButtonType.OK);
        return deleteAlert.getResult() == ButtonType.OK;

    }

    @FXML
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Message");
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    private boolean updateConfirmation() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Update specialist center");
        alert.setHeaderText("Are you sure you want to update this SPC?");
        alert.showAndWait();
        return alert.getResult() == ButtonType.OK;
    }

    public void showRepairs() {
        try {
            vehicleDetails.setVisible(true);
            custy_label.setVisible(true);
            custyInfo.setVisible(true);
            btn_hideDetails.setVisible(true);
            custyInfo.clear();
            showVehicle();
        }
        catch (NullPointerException e) {
            showAlert("No repair center selected.");
        }

    }

    private void showVehicle() {
        isShowing = true;
        List<VehicleRepair> vehicleRepairs = specRepairSystem.getBySpcID(spcID);
        ArrayList<Vehicle> vehicles = new ArrayList<>();
        for (VehicleRepair v : vehicleRepairs) {
            vehicles.add(VehicleSys.getInstance().searchAVehicle(v.getVehicleRegNumber()));
        }
        vehicleDetails.getItems().clear();
        vehicleObservableList.addAll(vehicles);
        veh_reg_column.setCellValueFactory(new PropertyValueFactory<>("vehicleRegNumber"));
        veh_reg_column.setCellFactory(TextFieldTableCell.forTableColumn());
        veh_make_column.setCellValueFactory(new PropertyValueFactory<>("manufacturer"));
        veh_make_column.setCellFactory(TextFieldTableCell.forTableColumn());
        veh_model_column.setCellValueFactory(new PropertyValueFactory<>("model"));
        veh_model_column.setCellFactory(TextFieldTableCell.forTableColumn());
        veh_mileage_column.setCellValueFactory(new PropertyValueFactory<>("mileage"));
        veh_mileage_column.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        veh_eng_column.setCellValueFactory(new PropertyValueFactory<>("engineSize"));
        veh_eng_column.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        vehicleDetails.setItems(vehicleObservableList);
    }

    public void showCustyDetails() {
        try {
            custy_label.setText("Customer Details :");
            Vehicle vehicle = vehicleDetails.getSelectionModel().getSelectedItem();
            Customer customer = CustomerSystem.getInstance().getACustomers(vehicle.getCustomerID());
            custyInfo.clear();
            custyInfo.appendText("ID : " + customer.getCustomerAddress() + "\n");
            custyInfo.appendText("First name : " + customer.getCustomerFirstname() + "\n");
            custyInfo.appendText("Surname : " + customer.getCustomerSurname() + "\n");
            custyInfo.appendText("Phone number : " + customer.getCustomerPhone() + "\n");
            custyInfo.appendText("Address : " + customer.getCustomerAddress() + "\n");
            custyInfo.appendText("Postcode : " + customer.getCustomerPostcode() + "\n");
            custyInfo.appendText("Type : " + customer.getCustomerType().toString() + "\n");
        }
        catch (NullPointerException e) {
            showAlert("Oops it seems like there is no owner of this vehicle! Please contact system administrator");
        }

    }

    public void hideDetails() {
        vehicleDetails.setVisible(false);
        custy_label.setVisible(false);
        custyInfo.setVisible(false);
        btn_hideDetails.setVisible(false);
    }


}