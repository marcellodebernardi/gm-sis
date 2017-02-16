package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;

public class PartsController {

    private Button addPartBtn;
    private TextField searchBtn;
    private TableColumn<?, ?> partAbstractionID;
    private TableColumn<?, ?> partName;
    private TableColumn<?, ?> partDescription;
    private TableColumn<?, ?> partPrice;
    private TableColumn<?, ?> partStockLevel;
    private Button editPartBtn;
    private Button deletePartBtn;
    private Button withdrawBtn;

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
