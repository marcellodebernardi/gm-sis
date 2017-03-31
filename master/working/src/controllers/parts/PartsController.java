package controllers.parts;

import domain.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import logic.booking.BookingSystem;
import logic.criterion.Criterion;
import logic.parts.PartsSystem;
import logic.vehicle.VehicleSys;
import persistence.DatabaseRepository;

import java.net.URL;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class PartsController implements Initializable {

    DatabaseRepository instance = DatabaseRepository.getInstance();
    ObservableList<PartAbstraction> tableEntries = FXCollections.observableArrayList();
    ObservableList<Installation> tableEntries2 = FXCollections.observableArrayList();
    ObservableList<String> CB = FXCollections.observableArrayList();
    private List<PartOccurrence> partOccurrences = new ArrayList<>();

    private PartsSystem pSys = PartsSystem.getInstance(DatabaseRepository.getInstance());
    @FXML
    private TableView<PartAbstraction> PartsTable;
    @FXML
    private TableColumn<PartAbstraction, Integer> partAbstractionID;
    @FXML
    private TableColumn<PartAbstraction, String> partName;
    @FXML
    private TableColumn<PartAbstraction, String> partDescription;
    @FXML
    private TableColumn<PartAbstraction, Double> partPrice;
    @FXML
    private TableColumn<PartAbstraction, Integer> partStockLevel;
    @FXML
    private TableView<Installation> PartsBookings;
    @FXML
    private TableColumn<Installation, Integer> installationID;
    @FXML
    private TableColumn<Installation, ZonedDateTime> installationDate;
    @FXML
    private TableColumn<Installation, ZonedDateTime> warrantyEnd;
    @FXML
    private TableColumn<Installation, Integer> partAbsID;
    @FXML
    private TableColumn<Installation, Integer> partOccID;
    @FXML
    private TableColumn<Installation, String> regNumber;
    @FXML
    private TextField searchParts;
    @FXML
    private ComboBox<String> CB1, CB2, CB3, CB4, addPartToInst, availableOcc;
    @FXML
    private TextField price1, price2, price3, price4;
    @FXML
    private TextField qty1, qty2, qty3, qty4, totalBill;
    @FXML
    private TextField partNameField, partPriceField, partStockLevelField;
    @FXML
    private TextArea partDescriptionField;
    @FXML
    private Button addPartBtn, searchBtn, calculateBtn, editPartBtn, deletePartBtn, withdrawBtn, updateBtn, viewBookingsBtn, saveChangesBtn, clearBtn, deleteInstBtn;
    @FXML
    private Button increaseStock, decreaseStock, clearInstBtn;
    @FXML
    private TextField regNumberInstallation, searchInst;
    @FXML
    private DatePicker instDate;

    @FXML
    private ComboBox<Integer> bookingsForInstallation = new ComboBox<>();
    @FXML
    private ObservableList<Integer> bookingIDs = FXCollections.observableArrayList();

    private ArrayList data = new ArrayList();
    private List<PartAbstraction> List;
    private List<Installation> List2;


    /*****************************************************************************************************************/
    private Callback<DatePicker, DateCell> dateChecker = dp1 -> new DateCell() {
        @Override
        public void updateItem(LocalDate item, boolean empty) {

            // Must call super
            super.updateItem(item, empty);
            if (item.isBefore(LocalDate.now())) {
                this.setStyle(" -fx-background-color: rgba(171,171,171,0); ");
                this.setDisable(true);
            }
            if (item.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                this.setDisable(true);
                this.setStyle("-fx-background-color: rgba(171,171,171,0)");
            }

        }
    };

    /**
     * The initialize method ensures that the combo-boxes which i have created refers to an observableArrayList (CB).
     * Need to click "View All Parts" button so that the updateTable() method puts data into that list object.
     */
    public void initialize(URL location, ResourceBundle resources) {

        instDate.setDayCellFactory(dateChecker);

        try {
            CB1.setItems(CB);
            CB2.setItems(CB);
            CB3.setItems(CB);
            CB4.setItems(CB);
            addPartToInst.setItems(CB);
            updateTable();
            viewAllBookingsClick();

        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    /**
     * This method is the action for my "View All Parts" button on the interface
     * This method is responsible for fetching data and displaying it all in the TableView by creating a list and storing into the object
     * Also the combo-box gets the PartAbstractionID and is stored into the object
     * Once the list has been created the setPartsTable() method is called and the PartsTable is set with the tableEntries data
     */
    public void updateTable() {

        Criterion c = new Criterion<>(PartAbstraction.class);

        List = instance.getByCriteria(c); //the data from Parts DB stored in this list

        tableEntries.removeAll(tableEntries); //table clears for when database gets updated
        CB.removeAll(CB); //the dropdown boxes update, doesn't show duplicate

        for (int i = 0; i < List.size(); i++) {

            tableEntries.add(List.get(i));
            data.add(List.get(i).getPartName());
            CB.add(List.get(i).getPartAbstractionID() + ": " + List.get(i).getPartName());

        }

        setPartsTable();
        PartsTable.setItems(tableEntries);

    }

    /**
     * The setPartsTable() method has been created to set the data in the TableView
     * Each field has been made to be editable when double clicked, the data can be edited using the interface
     */
    public void setPartsTable() {

        partAbstractionID.setCellValueFactory(new PropertyValueFactory<PartAbstraction, Integer>("partAbstractionID"));

        partName.setCellValueFactory(new PropertyValueFactory<PartAbstraction, String>("partName"));
        partName.setCellFactory(TextFieldTableCell.<PartAbstraction>forTableColumn());
        partName.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<PartAbstraction, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<PartAbstraction, String> event) {
                (event.getTableView().getItems().get(event.getTablePosition().getRow())).setPartName(event.getNewValue());
            }
        });

        partDescription.setCellValueFactory(new PropertyValueFactory<PartAbstraction, String>("partDescription"));
        partDescription.setCellFactory(TextFieldTableCell.<PartAbstraction>forTableColumn());
        partDescription.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<PartAbstraction, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<PartAbstraction, String> event) {
                (event.getTableView().getItems().get(event.getTablePosition().getRow())).setPartDescription(event.getNewValue());
            }
        });

        partPrice.setCellValueFactory(new PropertyValueFactory<PartAbstraction, Double>("partPrice"));
        partPrice.setCellFactory(TextFieldTableCell.<PartAbstraction, Double>forTableColumn(new DoubleStringConverter()));
        partPrice.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<PartAbstraction, Double>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<PartAbstraction, Double> event) {
                (event.getTableView().getItems().get(event.getTablePosition().getRow())).setPartPrice(event.getNewValue());
            }
        });

        partStockLevel.setCellValueFactory(new PropertyValueFactory<PartAbstraction, Integer>("partStockLevel"));

    }

    /**
     * In order to search the table, user must click on "view all parts" button first and then enter
     * part name to search, then click on the search button, the data should display in TableView...
     */
    public void searchPart() {

        tableEntries.removeAll(tableEntries);

        for (int i = 0; i < List.size(); i++) {

            if (List.get(i).getPartName().toLowerCase().contains(searchParts.getText())) {
                tableEntries.add(List.get(i));
            }
        }

        PartsTable.setItems(tableEntries);
    }

    /**
     * The CbSelect methods are used to get the price for the ID which is selected in the combo-box and sets the textbox
     * to display the price...
     */

    public void CbSelect() {

        int IdIndex = CB1.getSelectionModel().getSelectedIndex();
        System.out.println(IdIndex);
        String price = Double.toString(List.get(IdIndex).getPartPrice());
        price1.setText(price);

    }

    public void CbSelect2() {

        int IdIndex = CB2.getSelectionModel().getSelectedIndex();
        System.out.println(IdIndex);
        String price = Double.toString(List.get(IdIndex).getPartPrice());
        price2.setText(price);

    }

    public void CbSelect3() {

        int IdIndex = CB3.getSelectionModel().getSelectedIndex();
        System.out.println(IdIndex);
        String price = Double.toString(List.get(IdIndex).getPartPrice());
        price3.setText(price);

    }

    public void CbSelect4() {

        int IdIndex = CB4.getSelectionModel().getSelectedIndex();
        System.out.println(IdIndex);
        String price = Double.toString(List.get(IdIndex).getPartPrice());
        price4.setText(price);

    }

    /**
     * This method allows user to calculate individual parts bill where the user must click "View All Parts" button
     * Once that button is clicked, the user selects the part id from relevent combo-box and the price is displayed
     * The user needs to input the quantity and click the button which stores the values into an array
     */
    public void calculateBill() {

        TextField[] prices = {price1, price2, price3, price4};
        TextField[] quantity = {qty1, qty2, qty3, qty4};
        Double total = 0.00;

        for (int i = 0; i < 4; i++) {

            if (!prices[i].getText().equals("") && !quantity[i].getText().equals("")) {
                total += (Double.parseDouble(prices[i].getText()) * Integer.parseInt(quantity[i].getText()));
            }
        }
        totalBill.setText("£ " + total.toString() + "");
    }


    /**
     * This method shows all the installations in the tableview.
     */
    public void viewAllBookingsClick() {

        try {

            List2 = pSys.getAllInstallations();
            List2.get(0).getPartOccurrence().getPartOccurrenceID();


            tableEntries2.removeAll(tableEntries2);

            for (int i = 0; i < List2.size(); i++) {
                tableEntries2.add(List2.get(i));

            }

            installationID.setCellValueFactory(new PropertyValueFactory<Installation, Integer>("installationID"));
            installationID.setCellFactory(TextFieldTableCell.<Installation, Integer>forTableColumn(new IntegerStringConverter()));

            installationDate.setCellValueFactory(new PropertyValueFactory<Installation, ZonedDateTime>("installationDate"));
            installationDate.setCellFactory(TextFieldTableCell.<Installation, ZonedDateTime>forTableColumn(new ZonedDateStringConverter()));

            warrantyEnd.setCellValueFactory(new PropertyValueFactory<Installation, ZonedDateTime>("endWarrantyDate"));
            warrantyEnd.setCellFactory(TextFieldTableCell.<Installation, ZonedDateTime>forTableColumn(new ZonedDateStringConverter()));

            partAbsID.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Installation, Integer>,
                    ObservableValue<Integer>>() {
                public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Installation, Integer> p) {
                    return new ReadOnlyObjectWrapper<>(p.getValue().getPartOccurrence().getPartAbstractionID());
                }
            });

            partOccID.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Installation, Integer>,
                    ObservableValue<Integer>>() {
                public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Installation, Integer> p) {
                    return new ReadOnlyObjectWrapper<>(p.getValue().getPartOccurrence().getPartOccurrenceID());
                }
            });

            regNumber.setCellValueFactory(new PropertyValueFactory<Installation, String>("vehicleRegNumber"));
            regNumber.setCellFactory(TextFieldTableCell.forTableColumn());


        }
        catch (IndexOutOfBoundsException | NullPointerException e) {
            e.printStackTrace();
        }

        PartsBookings.setItems(tableEntries2);

    }

    /**
     * The addToDb() method is responsible for adding new part items to the database, a new object is created which uses
     * the constructor from PartAbstraction and is then saved to the database using commitItem(), the stock level creates the part occurrences.
     **/
    public void addToDB() throws Exception {


        try {
            PartAbstraction newPart = new PartAbstraction(partNameField.getText(), partDescriptionField.getText(),
                    Double.parseDouble(partPriceField.getText()), Integer.parseInt(partStockLevelField.getText()),
                    null);
            pSys.commitAbstraction(newPart);
            //boolean s = instance.commitItem(newPart);
            List<PartAbstraction> partAbstractionList = pSys.getByName(partNameField.getText());
            int index = partAbstractionList.size() - 1;
            for (int j = 0; j < Integer.parseInt(partStockLevelField.getText()); j++) {
                System.out.println("added : " + j + 1);
                PartAbstraction partAbstraction = partAbstractionList.get(index);
                PartOccurrence partOccurrence = new PartOccurrence(partAbstraction.getPartAbstractionID(), 0, 0);
                pSys.addPartOccurrence(partOccurrence);
            }

            clearAddForm();
            updateTable();
        }
        catch (NullPointerException | NumberFormatException e) {
            showError("Please fill out all the fields to add a part to the system");
        }

    }

    /**
     * This method just resets the text fields on the add part form
     */
    public void clearAddForm() {

        partNameField.clear();
        partDescriptionField.clear();
        partPriceField.clear();
        partStockLevelField.clear();

    }

    /**
     * This method is for the "Save" button, when the table is edited, the user must press enter to commit and then press save
     */
    public void saveChanges() {

        PartAbstraction singlePart;

        for (int i = 0; i < tableEntries.size(); i++) {

            PartsTable.getSelectionModel().select(i);
            singlePart = PartsTable.getSelectionModel().getSelectedItem();
            System.out.println(singlePart.getPartStockLevel());
            boolean c = instance.commitItem(singlePart);

        }

        showInfo("Changes have been saved");
        PartsTable.getSelectionModel().clearSelection();

    }

    /**
     * Deletes part abstraction, when the part is deleted, all the occurences for that part is also deleted
     */

    public void deletePart() {

        try {

            PartAbstraction part = PartsTable.getSelectionModel().getSelectedItem();

            if (part == null) {
                throw new Exception();
            }
            if ((!showWarning("Warning: Deleting this part type will delete all related stock items from the inventory\n" + " \n" +
                    "Press OK to continue"))) {
                return;
            }
            boolean result = pSys.deletePart(part.getPartAbstractionID());
            if (result) {
                updateTable();
            }
            else {
                showError("Part hasn't been deleted");
            }

        }
        catch (Exception e) {

            showError("No part has been selected");

        }

    }

    /**
     * The 4 show... methods display popups to the user, used for validation.
     */
    public void showInfo(String message) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Updated to Database");
        alert.setHeaderText(message);
        alert.showAndWait();

    }

    public void showError(String message) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(message);
        alert.showAndWait();

    }

    public boolean showWarning(String message) {

        Alert warnAlert = new Alert(Alert.AlertType.WARNING);
        warnAlert.setTitle("Warning");
        warnAlert.setHeaderText(message);
        warnAlert.showAndWait();
        if (warnAlert.getResult() == ButtonType.OK) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean showConfirmation(String message) {

        Alert conAlert = new Alert(Alert.AlertType.CONFIRMATION);
        conAlert.setTitle("Confirmation");
        conAlert.setHeaderText(message);
        conAlert.showAndWait();
        if (conAlert.getResult() == ButtonType.OK) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * This method increases stock by 1 when selecting a part from table and pressing +Stock, an occurrence is also created for that part
     */
    public void increaseStockLevel() {

        try {

            PartAbstraction partIncrease = PartsTable.getSelectionModel().getSelectedItem();
            PartOccurrence partOccurrence = new PartOccurrence(partIncrease.getPartAbstractionID(), 0, 0);
            pSys.addPartOccurrence(partOccurrence);
            int c = partIncrease.getPartStockLevel() + 1;
            System.out.println(c);

            partIncrease.setPartStockLevel(c);

            saveChanges();
            updateTable();

        }
        catch (Exception e) {

            showError("Please select a part first to increase stock");

        }

    }

    /**
     * This method decreases stock by 1 when selecting a part from table and pressing -Stock, an occurrence is deleted
     */
    public void decreaseStockLevel() {

        try {

            PartAbstraction partDecrease = PartsTable.getSelectionModel().getSelectedItem();

            int c = partDecrease.getPartStockLevel() - 1;
            System.out.println(c);
            System.out.println(partDecrease.getPartAbstractionID());
            List<PartOccurrence> partOccurrences = pSys.getAllFreeOccurrences(partDecrease);
            System.out.println("size is " + partOccurrences.size());
            PartOccurrence partOccurrence = partOccurrences.get(0);
            pSys.deleteOccurrence(partOccurrence, partDecrease);
            partDecrease.setPartStockLevel(c);

            saveChanges();
            updateTable();

        }
        catch (IndexOutOfBoundsException | NullPointerException e) {

            if (e instanceof IndexOutOfBoundsException) {
                e.printStackTrace();
            }
            else if (e instanceof NullPointerException) {
                showError("Please select a part first to decrease stock");
            }

        }
    }

    /**
     * This method deletes an installation from the database, the user must select from the table and press the delete button.
     */

    public void deleteInstallation() {

        try {

            Installation deleteInst = PartsBookings.getSelectionModel().getSelectedItem();
            if (deleteInst == null) {
                throw new Exception();
            }
            if ((!showConfirmation("Sure you want to delete this installation?"))) {
                return;
            }
            boolean result = pSys.deleteInstallation(deleteInst.getInstallationID());
            if (result) {
                viewAllBookingsClick();
            }
        }
        catch (Exception e) {
            showError("Please select an installation first to delete");
        }

    }

    /**
     * This method adds an installation to the tableview by filing out the fields on the GUI.
     */

    public void addInstallation() {

        try {

            Vehicle vehicle = VehicleSys.getInstance().searchAVehicle(regNumberInstallation.getText());
            if (vehicle == null) {
                throw new Exception();
            }
            viewAllBookingsClick();
            DiagRepBooking diagRepBooking = BookingSystem.getInstance().getBookingByID(bookingsForInstallation.getSelectionModel().getSelectedItem());
            PartOccurrence partOccurrence = pSys.getPartOcc(Integer.parseInt(availableOcc.getSelectionModel().getSelectedItem().trim()));
            String[] s = addPartToInst.getSelectionModel().getSelectedItem().split(":");
            int partAbs = Integer.parseInt(s[0].trim());
            System.out.println(partAbs);
            System.out.println("Stock level : " + partAbs);
            PartAbstraction partAbstraction = pSys.getPartbyID(partAbs);
            partAbstraction.setPartStockLevel(partAbstraction.getPartStockLevel() - 1);
            pSys.commitAbstraction(partAbstraction);
            System.out.println(partAbs);
            Installation installation = new Installation(ZonedDateTime.of(instDate.getValue(), LocalTime.now(), ZoneId.systemDefault()), ZonedDateTime.of(instDate.getValue().plusYears(1), LocalTime.now(), ZoneId.systemDefault()), regNumberInstallation.getText(), partAbs, partOccurrence);
            partAbstraction.setPartStockLevel(partAbstraction.getPartStockLevel() - 1);
            pSys.commitAbstraction(partAbstraction);

            updateTable();
            partOccurrence.setBookingID(diagRepBooking.getBookingID());
            pSys.commitInst(installation);

            showInfo("Installation has been added, stock for selected part has been reduced");

            clearInstallation();

        }
        catch (Exception e) {
            e.printStackTrace();
            if (e instanceof IndexOutOfBoundsException || e instanceof NullPointerException) {
                showError("No booking found!");
            }
            else {
                showError("Please enter a valid Vehicle registration.");
            }
        }

    }

    /**
     * This method clears the installation fields
     */

    public void clearInstallation() {

        instDate.setValue(null);
        addPartToInst.setValue(null);
        availableOcc.setValue(null);
        regNumberInstallation.clear();

    }

    /**
     * This method gets all the occurrences for the selected part in the the combo box
     */
    public void setOccs() {

        try {
            partOccurrences.removeAll(partOccurrences);
            String[] s = addPartToInst.getSelectionModel().getSelectedItem().trim().split(":");
            partOccurrences.addAll(pSys.getAllUninstalled(Integer.parseInt(s[0].trim())));
            availableOcc.getItems().removeAll(availableOcc.getItems());
            for (PartOccurrence partOccurrence : partOccurrences) {
                availableOcc.getItems().add(Integer.toString(partOccurrence.getPartOccurrenceID()));
            }
        }
        catch (NumberFormatException | NullPointerException e) {
            System.out.println("Showing part description.");
        }

    }

    /**
     * This method allows user to search installations by reg number or customer first name or surname
     */

    public void searchInstallations() {

        tableEntries2.removeAll(tableEntries2);

        try {

            for (int i = 0; i < List.size(); i++) {

                if (List2.get(i).getVehicleRegNumber().toLowerCase().contains(searchInst.getText())
                        || List2.get(i).getCustomer().getCustomerFirstname().toLowerCase().contains(searchInst.getText())
                        || List2.get(i).getCustomer().getCustomerSurname().toLowerCase().contains(searchInst.getText())) {

                    tableEntries2.add(List2.get(i));

                }

            }

            PartsBookings.setItems(tableEntries2);

        }
        catch (NullPointerException e) {

            e.printStackTrace();
        }
    }


    /**
     * This method finds the booking that is selected in the installation form, to check whether an installation can be added to an existing booking
     */
    public void findRelevantBookings() {
        try {
            bookingsForInstallation.getItems().clear();
            Vehicle vehicle = VehicleSys.getInstance().searchAVehicle(regNumberInstallation.getText().toUpperCase().trim());
            List<DiagRepBooking> diagRepBookings = vehicle.getBookingList();
            for (DiagRepBooking diagRepBooking : diagRepBookings) {
                if (!diagRepBooking.isComplete()) {
                    bookingIDs.add(diagRepBooking.getBookingID());
                }
            }
            bookingsForInstallation.setItems(bookingIDs);
        }
        catch (NullPointerException | IndexOutOfBoundsException e) {
            showError("Selected Vehicle has no booking! Please create a booking before trying to add an installation!");
        }
    }

}

