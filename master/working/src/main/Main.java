package main;

import javafx.application.Application;
import javafx.fxml.LoadException;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import persistence.DatabaseRepository;

/**
 * @author Marcello De Bernardi, Dillon Vaghela
 * @version 0.1
 * @since 0.1
 */
public class Main extends Application {
    private Stage primaryStage;
    private Scene mainScene;
    private BorderPane mainPane; // todo inject on controllers
    private DatabaseRepository persistence; // todo inject on modules

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
        this.primaryStage = primaryStage;

        try {
            AnchorPane loginPane = FXMLLoader.load(getClass().getResource("/loginPane.fxml"));
            this.primaryStage.setScene(new Scene(loginPane));
            this.primaryStage.show();
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

    /**
     * Every controller should call this method to change scenes.
     * @param newScene
     */
    public void replaceScene(Scene newScene) {
        primaryStage.setScene(newScene);
        primaryStage.show();
    }


    // todo method for redrawing the content of a tab, do this by giving new pane as argument
    public void replaceTabContent() {

    }
}