package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;

public class PartsController {

    @FXML
    private Button addPartBtn;

    @FXML
    private TextField searchBtn;

    @FXML
    private TableColumn<?, ?> partID;

    @FXML
    private TableColumn<?, ?> partName;

    @FXML
    private TableColumn<?, ?> partDescription;

    @FXML
    private TableColumn<?, ?> partPrice;

    @FXML
    private TableColumn<?, ?> partStockLevel;

    @FXML
    private Button editPartBtn;

    @FXML
    private Button deletePartBtn;

}
