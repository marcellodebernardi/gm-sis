package controllers.booking;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import main.Main;

import java.io.IOException;

/**
 * @author Marcello De Bernardi
 */
public class BookingController {
    // singleton instance and booking system
    private static BookingController instance;

    private BorderPane basePane;


    private BookingController() {}

    public static BookingController getInstance() {
        if (instance == null) instance = new BookingController();
        return instance;
    }

    public BorderPane show() {
        try {
            basePane = FXMLLoader.load(getClass().getResource("/resources/booking/BasePane.fxml"));
            basePane.setLeft(FXMLLoader.load(getClass().getResource("/resources/booking/DetailsPane.fxml")));
            basePane.setCenter(FXMLLoader.load(getClass().getResource("/resources/booking/ListPane.fxml")));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        Main.getInstance().replaceTabContent(basePane);
        return basePane;
    }

    void setLeft(Node node) {
        basePane.setLeft(node);
    }

    void setCenter(Node node) {
        basePane.setCenter(node);
    }
}
