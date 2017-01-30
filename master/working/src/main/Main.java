package main;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.Scene;

/**
 * @author Marcello De Bernardi, Dillon Vaghela
 * @version 0.1
 * @since 0.1
 */
public class Main extends Application {

    public static void main (String[] args)
    {
        Application.launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {

        Pane mainPane = (Pane) FXMLLoader.load(getClass().getResource("/resources/test.fxml"));
        primaryStage.setScene(new Scene(mainPane));
        primaryStage.show();

    }
}
