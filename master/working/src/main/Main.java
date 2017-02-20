package main;

import javafx.application.Application;
import javafx.fxml.LoadException;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.Scene;
import persistence.DatabaseRepository;

/**
 * @author Marcello De Bernardi, Dillon Vaghela
 * @version 0.1
 * @since 0.1
 */
public class Main extends Application {
    private static Scene mainScene;
    private static Scene loginScene;
    private static Pane mainPane;
    private static AnchorPane loginPane;
    private static DatabaseRepository persistence; // todo inject on modules

    public static void main (String[] args) {
        Application.launch(args);
    }

    // standard JavaFX initialization method that runs in separate thread
    // best place to perform startup initializations
    @Override
    public void init() {
        persistence = DatabaseRepository.getInstance();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            loginPane = FXMLLoader.load(getClass().getResource("/loginPane.fxml"));
            loginScene = new Scene(loginPane);
            primaryStage.setScene(loginScene);
            primaryStage.show();
            // primaryStage.setFullScreen(true);
        }
        catch(LoadException e) {
            e.printStackTrace();
        }
    }

    // standard JavaFX method for closing resources etc
    @Override
    public void stop() {
        persistence.close();
    }


}
