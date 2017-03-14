package controllers.booking;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import main.Main;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author Marcello De Bernardi
 */
public class BookingController {
    // singleton instance and booking system
    private static BookingController instance;
    private HashMap<Class<?>, Object> controllerMap;

    private BorderPane basePane;


    private BookingController() {
        controllerMap = new HashMap<>();
    }

    public static BookingController getInstance() {
        if (instance == null) instance = new BookingController();
        return instance;
    }


    public BorderPane show() {
        try {
            basePane = FXMLLoader.load(getClass().getResource("/booking/BookingBasePane.fxml"));
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

    void setController(Class<?> controllerClass, Object controller) {
        controllerMap.put(controllerClass, controller);
    }

    Object getController(Class<?> controllerClass) {
        return controllerMap.get(controllerClass);
    }
}
