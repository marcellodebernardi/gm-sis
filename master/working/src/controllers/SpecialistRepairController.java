package controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.Stage;


public class SpecialistRepairController {


    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btn_addBooking;

    @FXML
    private TableView<?> table_bookings;

    @FXML
    private Button btn_deleteBooking;

    @FXML
    private Button btn_addSRC;

    @FXML
    private Button btn_deleteSRC;

    @FXML
    private Button btn_editBooking;

    @FXML
    private Button btn_editSRC;

    @FXML
    private Button btn_searchBookings;

    @FXML
    private Button btn_searchAllSRC;

    @FXML
    void addBookings(ActionEvent event) {

    }

    @FXML
    void add_SRC(ActionEvent event) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resources/addSRC.fxml"));
        Parent menu = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Add SRC");
        stage.setScene(new Scene(menu));
        stage.show();
    }

    @FXML
    void delete_Bookings(ActionEvent event) {

    }

    @FXML
    void delete_SRC(ActionEvent event) {

    }

    @FXML
    void displayAllSRC(ActionEvent event) {
    /*
    FIX ME FFS
    ArrayList arr = (ArrayList)SpecRepairSystem.getInstance().getRepairCenterList()
    */
    }

    @FXML
    void edit_Booking(ActionEvent event) {

    }

    @FXML
    void edit_SRC(ActionEvent event) {

    }

    @FXML
    void searchBookings(ActionEvent event) {


    }

    @FXML
    void initialize() {
        assert btn_addBooking != null : "fx:id=\"btn_addBooking\" was not injected: check your FXML file 'SRCBooking.fxml'.";
        assert table_bookings != null : "fx:id=\"table_bookings\" was not injected: check your FXML file 'SRCBooking.fxml'.";
        assert btn_deleteBooking != null : "fx:id=\"btn_deleteBooking\" was not injected: check your FXML file 'SRCBooking.fxml'.";
        assert btn_addSRC != null : "fx:id=\"btn_addSRC\" was not injected: check your FXML file 'SRCBooking.fxml'.";
        assert btn_deleteSRC != null : "fx:id=\"btn_deleteSRC\" was not injected: check your FXML file 'SRCBooking.fxml'.";
        assert btn_editBooking != null : "fx:id=\"btn_editBooking\" was not injected: check your FXML file 'SRCBooking.fxml'.";
        assert btn_editSRC != null : "fx:id=\"btn_editSRC\" was not injected: check your FXML file 'SRCBooking.fxml'.";
        assert btn_searchBookings != null : "fx:id=\"btn_searchBookings\" was not injected: check your FXML file 'SRCBooking.fxml'.";
        assert btn_searchAllSRC != null : "fx:id=\"btn_searchAllSRC\" was not injected: check your FXML file 'SRCBooking.fxml'.";

    }
}
