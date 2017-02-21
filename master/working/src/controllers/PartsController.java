package controllers;

import domain.Installation;
import domain.PartAbstraction;
import domain.PartOccurrence;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.converter.DateStringConverter;
import persistence.DatabaseRepository;
import logic.Criterion;
import javafx.event.EventHandler;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.collections.ObservableList;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.lang.*;
import java.util.ResourceBundle;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ComboBox;
//import java.sql.Connection;
//import org.controlsfx.control.textfield.TextFields;
//import javax.swing.*;


public class PartsController implements Initializable {

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
    private TableColumn<Installation, Date> installationDate;
    @FXML
    private TableColumn<Installation, Date> warrantyEnd;
    @FXML
    private TableColumn<Installation, Integer> partAbsID;
    @FXML
    private TableColumn<Installation, Integer> partOccID;
    @FXML
    private TableColumn<Installation, String> regNumber;

    @FXML
    private TextField searchParts;
    @FXML
    private ComboBox<String> CB1, CB2, CB3,CB4;
    @FXML
    private TextField price1, price2, price3, price4;
    @FXML
    private TextField qty1, qty2, qty3, qty4, totalBill;
    @FXML
    private TextField partIDFeild, partNameField, partPriceField, partStockLevelField;
    @FXML
    private TextArea partDescriptionField;
    @FXML
    private Button addPartBtn, searchBtn, calculateBtn, editPartBtn, deletePartBtn, withdrawBtn, updateBtn, viewBookingsBtn;

    private Stage AddPartStage;
    private Stage WithdrawPartStage;
    private ArrayList data=new ArrayList();
    private List<PartAbstraction> List;
    private List<Installation> List2;

    DatabaseRepository instance = DatabaseRepository.getInstance();
    ObservableList<PartAbstraction> tableEntries = FXCollections.observableArrayList();
    ObservableList<Installation> tableEntries2 = FXCollections.observableArrayList();

    ObservableList<String> CB=FXCollections.observableArrayList();


    /*****************************************************************************************************************/

    /**
     * The initialize method ensures that the combo-boxes which i have created refers to an observableArrayList (CB).
     * Need to click "Display All Parts" button so that the updateTable() method puts data into that list object.
     *
     */
    public void initialize(URL location, ResourceBundle resources){

        try {
            CB1.setItems(CB);
            CB2.setItems(CB);
            CB3.setItems(CB);
            CB4.setItems(CB);
        }catch(NullPointerException e){e.printStackTrace();}

    }

    /**
     * This method is the action for my "Display All Parts" button on the interface
     * This method is responsible for fetching data and displaying it all in the TableView by creating a list and storing into the object
     * Also the combo-box gets the PartAbstractionID and is stored into the object
     * Once the list has been created the setPartsTable() method is called and the PartsTable is set with the tableEntries data
     */
    public void updateTable() {

        Criterion c = new Criterion<>(PartAbstraction.class);

        List = instance.getByCriteria(c); //the data from Parts DB stored in this list

        //Test
        // System.out.println(List.get(0).getPartName());
        // System.out.println(List.get(1).getPartName());
        //System.out.println(List.get(0).getPartDescription());
        // System.out.println(List.size());

        tableEntries.removeAll(tableEntries); //table clears for when database gets updated

        for (int i = 0; i < List.size(); i++) {

            tableEntries.add(List.get(i));
            data.add(List.get(i).getPartName());
            CB.add(Integer.toString(List.get(i).getPartAbstractionID()));
        }

        setPartsTable();;

        PartsTable.setItems(tableEntries);

       // TextFields.bindAutoCompletion(searchParts,data);

    }

    /**
     * The setPartsTable() method has been created to set the data in the TableView
     * Each field has been made to be editable when double clicked, the data can be edited using the interface
     */
    public void setPartsTable(){

        partAbstractionID.setCellValueFactory(new PropertyValueFactory<PartAbstraction, Integer>("partAbstractionID"));
        partAbstractionID.setCellFactory(TextFieldTableCell.<PartAbstraction, Integer>forTableColumn(new IntegerStringConverter()));
        partAbstractionID.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<PartAbstraction, Integer>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<PartAbstraction, Integer> event) {
                (event.getTableView().getItems().get(event.getTablePosition().getRow())).setPartAbstractionID(event.getNewValue());
            }
        });

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
        partStockLevel.setCellFactory(TextFieldTableCell.<PartAbstraction, Integer>forTableColumn(new IntegerStringConverter()));
        partStockLevel.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<PartAbstraction, Integer>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<PartAbstraction, Integer> event) {
                (event.getTableView().getItems().get(event.getTablePosition().getRow())).setPartStockLevel(event.getNewValue());
            }
        });

    }

    /**
     * The addPartButtonClick() and withdrawButtonClick method opens up an pop up window which allows users to enter new stock/withdraw stock into/from the system
     * If the user clicks on the button whilst the relevant window is already open then an alert is shown to user
     * The showAlert() method displays a pop up window telling the user the window is already open
     * @throws Exception
     */

    public void addPartButtonClick() throws Exception {
        try {
            if (AddPartStage != null) {
                if (AddPartStage.isShowing()) {
                    showAlert();
                    AddPartStage.setAlwaysOnTop(true);
                    return;

                }
            }
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/AddPart.fxml"));
            Parent menu = fxmlLoader.load();
            AddPartStage = new Stage();
            AddPartStage.setTitle("Add Part");
            AddPartStage.setScene(new Scene(menu));
            AddPartStage.show();

        } catch (Exception e) {
            System.out.println("Cannot Open");

        }
    }

    public void withdrawButtonClick() throws Exception {
        try {
            if (WithdrawPartStage != null) {
                if (WithdrawPartStage.isShowing()) {
                    showAlert();
                    WithdrawPartStage.setAlwaysOnTop(true);
                    return;
                }
            }
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/RemovePart.fxml"));
            Parent menu = fxmlLoader.load();
            WithdrawPartStage = new Stage();
            WithdrawPartStage.setTitle("Withdraw Part");
            WithdrawPartStage.setScene(new Scene(menu));
            WithdrawPartStage.show();
        } catch (Exception e) {
            System.out.println("Cannot Open");
        }
    }

    public void showAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Alert");
        alert.setHeaderText("This window is already open, press ok to continue");
        alert.showAndWait();
    }

    /**
     * In order to search the table, user must click on "display all parts" button first and then enter
     * part name to search, then click on the search button, the data should display in TableView...
     */
    public void searchPart() {

        tableEntries.removeAll(tableEntries);

        for(int i=0; i<List.size(); i++){

            if(List.get(i).getPartName().toLowerCase().contains(searchParts.getText())){
                tableEntries.add(List.get(i));
            }
        }

        PartsTable.setItems(tableEntries);
    }

    /**
     * The CbSelect methods are used to get the price for the ID which is selected in the combo-box and sets the textbox
     * to display the price...
     */

    public void CbSelect(){

        int IdIndex=CB1.getSelectionModel().getSelectedIndex();
        System.out.println(IdIndex);
        String price= Double.toString(List.get(IdIndex).getPartPrice());
        price1.setText(price);

    }
    public void CbSelect2(){

        int IdIndex=CB2.getSelectionModel().getSelectedIndex();
        System.out.println(IdIndex);
        String price= Double.toString(List.get(IdIndex).getPartPrice());
        price2.setText(price);

    }
    public void CbSelect3(){

        int IdIndex=CB3.getSelectionModel().getSelectedIndex();
        System.out.println(IdIndex);
        String price= Double.toString(List.get(IdIndex).getPartPrice());
        price3.setText(price);

    }
    public void CbSelect4(){

        int IdIndex=CB4.getSelectionModel().getSelectedIndex();
        System.out.println(IdIndex);
        String price= Double.toString(List.get(IdIndex).getPartPrice());
        price4.setText(price);

    }

    /**
     * This method allows user to calculate individual parts bill where the user must click "Display All Parts" button
     * Once that button is clicked, the user selects the part id from relevent combo-box and the price is displayed
     * The user needs to input the quantity and click the button which stores the values into an array
     */
    public void calculateBill(){

        TextField[] prices={price1,price2,price3,price4};
        TextField[] quantity={qty1,qty2,qty3,qty4};
        Double total=0.00;

        for(int i=0;i<4;i++){
            if(!prices[i].getText().equals("")&&!quantity[i].getText().equals("")){
                total+=(Double.parseDouble(prices[i].getText())*Integer.parseInt(quantity[i].getText()));
            }
        }
        //String.format("%1$,.2f", total);
        totalBill.setText("£ " + total.toString()+"");
    }


    /**
     * TODO: fix the Installation data and set Occurence as editable
     *
     */
    public void viewAllBookingsClick(){

        Criterion c2 = new Criterion<>(Installation.class);
        List2 = instance.getByCriteria(c2);

        tableEntries2.removeAll(tableEntries2);

        for(int i =0; i< List2.size(); i++){

            tableEntries2.add(List2.get(i));

        }

        //System.out.println(List2.size());

        installationID.setCellValueFactory(new PropertyValueFactory<Installation, Integer>("installationID"));
        installationID.setCellFactory(TextFieldTableCell.<Installation, Integer>forTableColumn(new IntegerStringConverter()));
        installationID.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Installation, Integer>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Installation, Integer> event) {
                (event.getTableView().getItems().get(event.getTablePosition().getRow())).setInstallationID(event.getNewValue());
            }
        });

        installationDate.setCellValueFactory(new PropertyValueFactory<Installation, Date>("installationDate"));
        installationDate.setCellFactory(TextFieldTableCell.<Installation, Date>forTableColumn(new DateStringConverter()));
        installationDate.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Installation, Date>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Installation, Date> event) {
                (event.getTableView().getItems().get(event.getTablePosition().getRow())).setInstallationDate(event.getNewValue());
            }
        });

        warrantyEnd.setCellValueFactory(new PropertyValueFactory<Installation, Date>("endWarrantyDate"));
        warrantyEnd.setCellFactory(TextFieldTableCell.<Installation, Date>forTableColumn(new DateStringConverter()));
        warrantyEnd.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Installation, Date>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Installation, Date> event) {
                (event.getTableView().getItems().get(event.getTablePosition().getRow())).setEndWarrantyDate(event.getNewValue());
            }
        });

        partAbsID.setCellValueFactory(new PropertyValueFactory<Installation, Integer>("partAbstractionID"));
        partAbsID.setCellFactory(TextFieldTableCell.<Installation, Integer>forTableColumn(new IntegerStringConverter()));
        partAbsID.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Installation, Integer>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Installation, Integer> event) {
                (event.getTableView().getItems().get(event.getTablePosition().getRow())).setPartAbstractionID(event.getNewValue());
            }
        });

        partOccID.setCellValueFactory(new PropertyValueFactory<Installation, Integer>("partOccurrence"));
        /**
         * TODO: make editable, not working atm
         */

        regNumber.setCellValueFactory(new PropertyValueFactory<Installation, String>("vehicleRegNumber"));
        regNumber.setCellFactory(TextFieldTableCell.<Installation>forTableColumn());
        regNumber.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Installation, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Installation, String> event) {
                (event.getTableView().getItems().get(event.getTablePosition().getRow())).setVehicleRegNumber(event.getNewValue());
            }
        });

        PartsBookings.setItems(tableEntries2);

    }


    public void addToDB(){

        List<PartOccurrence> m = new ArrayList<>();


        PartAbstraction newPart= new PartAbstraction("TestPart", "testDesc", 1.1, 1,null);
        boolean s= instance.commitItem(newPart);

        System.out.println(s);



    }


}

