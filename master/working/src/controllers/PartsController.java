package controllers;

import entities.PartAbstraction;
import javafx.event.EventHandler;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import logic.Criterion;
import persistence.DatabaseRepository;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.collections.ObservableList;
import java.util.List;
//import org.controlsfx.control.textfield.TextFields;

import java.lang.*;



public class PartsController {

    @FXML private Button addPartBtn;
    @FXML private TableView<PartAbstraction> PartsTable;
    @FXML private TableColumn<PartAbstraction, Integer> partAbstractionID;
    @FXML private TableColumn<PartAbstraction, String> partName;
    @FXML private TableColumn<PartAbstraction, String> partDescription;
    @FXML private TableColumn<PartAbstraction, Double> partPrice;
    @FXML private TableColumn<PartAbstraction, Integer> partStockLevel;
    @FXML private TextField searchBtn;
    @FXML private Button editPartBtn;
    @FXML private Button deletePartBtn;
    @FXML private Button withdrawBtn;
    @FXML private Button updateBtn;


    DatabaseRepository instance = DatabaseRepository.getInstance();

    final ObservableList<PartAbstraction> tableEntries = FXCollections.observableArrayList();
    private List<PartAbstraction>   List;

    public void updateTable(){

        //Criterion c=new Criterion(PartAbstraction.class, "partAbstractionID", CriterionOperator.EqualTo, 1);
        Criterion c=new Criterion(PartAbstraction.class);

        List = instance.getByCriteria(c);

        System.out.println(List.get(0).getPartName());
        System.out.println(List.get(0).getPartDescription());
        System.out.println(List.size());

        tableEntries.removeAll(tableEntries);

        for(int i =0; i<List.size(); i++){

            tableEntries.add(List.get(i));
        }

        partAbstractionID.setCellValueFactory(new PropertyValueFactory<PartAbstraction, Integer>("partAbstractionID"));
        //partAbstractionID.setCellFactory(TextFieldTableCell.<PartAbstraction>forTableColumn());

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

        partStockLevel.setCellValueFactory(new PropertyValueFactory<PartAbstraction, Integer>("partStockLevel"));
        PartsTable.setItems(tableEntries);
    }

    public void addPartButtonClick() throws Exception{

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/AddPart.fxml"));
        Parent menu = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Add Part");
        stage.setScene(new Scene(menu));
        stage.show();

    }

    public void withdrawButtonClick() throws Exception{

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/RemovePart.fxml"));
        Parent menu = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Withdraw Part");
        stage.setScene(new Scene(menu));
        stage.show();
    }



}
