package main;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Marcello De Bernardi
 * @version 0.1
 * @since 0.1
 */
public class Main extends Application {

    public static void main (String[] args)
    {
        launch(args);
    }
    @Override
    public void start(Stage stage) {

        Parent root = FXMLLoader.load(getClass().getResource("test.fxml"));
        primaryStage.setTitle("Hello");
        primaryStage.setScene(new Scene(root, 300, 300));
        primaryStage.show();

    }
}
