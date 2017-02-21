package main;

import javafx.application.Application;
import javafx.fxml.LoadException;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
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
    private static Main application;

    // stage, scene, and BorderPane containing TabPane
    private Stage primaryStage;
    private Scene mainScene;
    private FlowPane applicationPane;

    public static void main (String[] args) {
        Application.launch(args);
    }

    // standard JavaFX initialization method that runs in separate thread
    // best place to perform startup initializations
    @Override
    public void init() {
        application = this;
        DatabaseRepository.getInstance();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        try {
            BorderPane loginPane = FXMLLoader.load(getClass().getResource("/fxml/loginPane.fxml"));
            this.primaryStage.setScene(new Scene(loginPane));
            primaryStage.getScene().getStylesheets().add("/resources/stylesheets/login.css");
            this.primaryStage.show();
        }
        catch(LoadException e) {
            e.printStackTrace();
        }
    }

    // standard JavaFX method for closing resources etc
    @Override
    public void stop() {
        DatabaseRepository.getInstance().close();
    }

    /**
     * Returns the instance of Main. Allows controllers to interact with top-level GUI logic.
     *
     * @return the JavaFX application
     */
    public static Main getInstance() {
        return application;
    }

    /**
     * Takes the main scene from the login controller. The main scene cannot be loaded in Main,
     * because it has behavior that assumes that a login attempt has been made.
     */
    public void setMainScene(Scene scene) {
        mainScene = scene;
        primaryStage.setScene(mainScene);
        primaryStage.getScene().getStylesheets().add("/resources/stylesheets/stylesheet.css");
        primaryStage.setMaximized(true);
        applicationPane = (FlowPane) mainScene.getRoot();
    }

    public void replaceTabContent(Pane pane) {
        // todo redraw specific tab in ApplicationPane
    }
}