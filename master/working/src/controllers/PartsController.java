package controllers;

import entities.PartAbstraction;
import javafx.scene.control.*;
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

import java.util.ArrayList;
import java.util.List;
import java.lang.*;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import static logic.CriterionOperator.*;


public class PartsController {

    @FXML private Button addPartBtn;
    @FXML private TableView<PartAbstraction> PartsTable;
    @FXML private TableColumn<PartAbstraction, Integer> partAbstractionID;
    @FXML private TableColumn<PartAbstraction, String> partName;
    @FXML private TableColumn<PartAbstraction, String> partDescription;
    @FXML private TableColumn<PartAbstraction, Double> partPrice;
    @FXML private TableColumn<PartAbstraction, Integer> partStockLevel;
    @FXML private Button editPartBtn;
    @FXML private Button deletePartBtn;
    @FXML private Button withdrawBtn;
    @FXML private Button updateBtn;
    @FXML private TextField searchParts;

    private Stage AddPartStage;
    private Stage WithdrawPartStage;

    DatabaseRepository instance = DatabaseRepository.getInstance();
    final ObservableList<PartAbstraction> tableEntries = FXCollections.observableArrayList();
    final ObservableList tableEntries2 = FXCollections.observableArrayList();
    final ObservableList<PartAbstraction> tableItems = FXCollections.observableArrayList();
    private List<PartAbstraction> List;
    private List<PartAbstraction> List2;
    private String[] split2;
    private ArrayList data = new ArrayList();


    public void setTable1() {

        partAbstractionID.setCellValueFactory(new PropertyValueFactory<PartAbstraction, Integer>("partAbstractionID"));
        partAbstractionID.setCellFactory(TextFieldTableCell.<PartAbstraction, Integer>forTableColumn(new IntegerStringConverter()));
        partAbstractionID.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<PartAbstraction, Integer>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<PartAbstraction, Integer> event) {
                ( event.getTableView().getItems().get(event.getTablePosition().getRow())).setPartAbstractionID(event.getNewValue());
            }
        });

        partName.setCellValueFactory(new PropertyValueFactory<PartAbstraction, String>("partName"));
        partName.setCellFactory(TextFieldTableCell.<PartAbstraction>forTableColumn());
        partName.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<PartAbstraction, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<PartAbstraction, String> event) {
                ( event.getTableView().getItems().get(event.getTablePosition().getRow())).setPartName(event.getNewValue());
            }
        });

        partDescription.setCellValueFactory(new PropertyValueFactory<PartAbstraction, String>("partDescription"));
        partDescription.setCellFactory(TextFieldTableCell.<PartAbstraction>forTableColumn());
        partDescription.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<PartAbstraction, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<PartAbstraction, String> event) {
                ( event.getTableView().getItems().get(event.getTablePosition().getRow())).setPartDescription(event.getNewValue());
            }
        });

        partPrice.setCellValueFactory(new PropertyValueFactory<PartAbstraction, Double>("partPrice"));
        partPrice.setCellFactory(TextFieldTableCell.<PartAbstraction, Double>forTableColumn(new DoubleStringConverter()));
        partPrice.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<PartAbstraction, Double>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<PartAbstraction, Double> event) {
                ( event.getTableView().getItems().get(event.getTablePosition().getRow())).setPartPrice(event.getNewValue());
            }
        });

        partStockLevel.setCellValueFactory(new PropertyValueFactory<PartAbstraction, Integer>("partStockLevel"));
        partStockLevel.setCellFactory(TextFieldTableCell.<PartAbstraction, Integer>forTableColumn(new IntegerStringConverter()));
        partStockLevel.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<PartAbstraction, Integer>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<PartAbstraction, Integer> event) {
                ( event.getTableView().getItems().get(event.getTablePosition().getRow())).setPartStockLevel(event.getNewValue());
            }
        });
    }

    public void updateTable(){

        Criterion c =new Criterion<>(PartAbstraction.class);

        List = instance.getByCriteria(c); //the data from Parts DB stored in this list

        /** test list
         *
         *
         * // System.out.println(List.get(0).getPartName());
         * // System.out.println(List.get(1).getPartName());
         * /System.out.println(List.get(0).getPartDescription());
         * // System.out.println(List.size());
         **/

        tableEntries.removeAll(tableEntries);

        for(int i =0; i<List.size(); i++){

            tableEntries.add(List.get(i));
        }

        setTable1();

        PartsTable.setItems(tableEntries);

    }

    public void addPartButtonClick() throws Exception{
        try{
            if (AddPartStage != null)
            {
                if (AddPartStage.isShowing())
                {
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

        }
        catch (Exception e)
        {
            System.out.println("Cannot Open");

        }
    }

    public void withdrawButtonClick() throws Exception{
        try{
            if (WithdrawPartStage != null)
            {
                if (WithdrawPartStage.isShowing())
                {
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
        }
        catch (Exception e)
        {
            System.out.println("Cannot Open");
        }
    }

    public void showAlert()
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Alert");
        alert.setHeaderText("This window is already open, press ok to continue");
        alert.showAndWait();
    }

    public void searchPart() {

        // TODO: fix the search textfield, not returning list properly

        Criterion c2 =new Criterion<>(PartAbstraction.class);
        List2 = instance.getByCriteria(c2);

        tableEntries2.removeAll(tableEntries2);
        tableItems.removeAll(tableItems);

        for(int i=0; i<List2.size(); i++){

            if(searchParts.getText().equals("") || List2.get(i).getPartName().startsWith(searchParts.getText()));
            {
                tableEntries2.add(List2.get(i).getPartName());
                tableItems.add(List2.get(i));
            }
        }

        setTable1();

        PartsTable.setItems(tableEntries2);

    }
}

