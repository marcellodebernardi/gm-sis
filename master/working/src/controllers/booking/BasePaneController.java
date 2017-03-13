package controllers.booking;

import domain.PartOccurrence;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;

/**
 * @author Marcello De Bernardi
 */
public class BasePaneController {
    private BookingController master;

    // scene graph panes
    @FXML private BorderPane basePane;
    @FXML private BorderPane detailsPane;
    @FXML private BorderPane listPane;
    @FXML private BorderPane calendarPane;
    @FXML private GridPane detailsGridPane;


    public BasePaneController() {
        master = BookingController.getInstance();
    }

    @FXML private void initialize() {}
}
