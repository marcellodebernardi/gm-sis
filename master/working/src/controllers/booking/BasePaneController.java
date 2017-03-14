package controllers.booking;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

/**
 * @author Marcello De Bernardi
 */
public class BasePaneController {
    private BookingController master;

    // scene graph panes
    @FXML private BorderPane basePane;


    public BasePaneController() {
        master = BookingController.getInstance();
    }

    @FXML private void initialize() {
        master.setController(BasePaneController.class, this);
    }
}
