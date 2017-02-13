package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import entities.SpecRepBooking;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.SubScene;
import logic.CriterionRepository;
import logic.SpecRepairSystem;
import persistence.DatabaseRepository;

public class add_SRC {
   private CriterionRepository cr = DatabaseRepository.getInstance();

   private add_SRC instance =  new add_SRC();

   private add_SRC()
   {

   }

   public add_SRC getInstance()
   {
       if(instance == null) return new add_SRC();
       return instance;
   }

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Pane searchPane;

    @FXML
    private Button btn_add;

    @FXML
    private TextField field_ID;

    @FXML
    private TextField field_name;

    @FXML
    private TextField field_add;

    @FXML
    private TextField field_phone;

    @FXML
    private TextField field_email;

    @FXML
    private Button btn_error;


    @FXML
    void add_SRC(ActionEvent event) throws NumberFormatException{
        try {
            SpecRepairSystem.getInstance(cr).addRepairCenter(Integer.parseInt(field_ID.getText()), field_name.getText(), field_add.getText(), field_phone.getText(), field_email.getText());
            Platform.exit();
        }
        catch(NumberFormatException e)
        {
            btn_error.setVisible(true);
        }
    }

    @FXML
    void error_handle(ActionEvent e) throws Exception
    {
        btn_error.setVisible(false);
    }

    @FXML
    void initialize() {
        assert searchPane != null : "fx:id=\"searchPane\" was not injected: check your FXML file 'addSRC.fxml'.";
        assert btn_add != null : "fx:id=\"btn_add\" was not injected: check your FXML file 'addSRC.fxml'.";
        assert field_ID != null : "fx:id=\"field_ID\" was not injected: check your FXML file 'addSRC.fxml'.";
        assert field_name != null : "fx:id=\"field_name\" was not injected: check your FXML file 'addSRC.fxml'.";
        assert field_add != null : "fx:id=\"field_add\" was not injected: check your FXML file 'addSRC.fxml'.";
        assert field_phone != null : "fx:id=\"field_phone\" was not injected: check your FXML file 'addSRC.fxml'.";
        assert field_email != null : "fx:id=\"field_email\" was not injected: check your FXML file 'addSRC.fxml'.";
        assert btn_add != null : "fx:id=\"btn_add\" was not injected: check your FXML file 'addSRC.fxml'.";


    }
}
