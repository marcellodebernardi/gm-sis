package main;

import javafx.application.Application;
import javafx.fxml.LoadException;
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
    private static Scene login;
    private static Pane mainPane;
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
            mainPane = FXMLLoader.load(getClass().getResource("/resources/test.fxml"));
            //test.fxml
            login = new Scene(mainPane);
            primaryStage.setScene(login);
            primaryStage.show();
            //primaryStage.setFullScreen(true);
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
