package main;

import javafx.application.Application;
import javafx.fxml.LoadException;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import persistence.DatabaseRepository;

import java.io.IOException;

/**
 * @author Marcello De Bernardi, Dillon Vaghela
 * @version 0.1
 * @since 0.1
 */
public class Main extends Application {
    private static Main application;

    // screen dimensions
    private double screenX = Screen.getPrimary().getVisualBounds().getWidth();
    private double screenY = Screen.getPrimary().getVisualBounds().getHeight();

    // stage, scene, and BorderPane containing TabPane
    private Stage primaryStage;
    private Scene mainScene;
    private BorderPane applicationPane;
    private HBox tabPane;


    /**
     * Application entry point.
     *
     * @param args CLI-arguments
     */
    public static void main (String[] args) {
        Application.launch(args);
    }

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

    /**
     * Lifecycle method for closing resources etc.
     */
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
    public void setRootPane(BorderPane rootPane) {
        applicationPane = rootPane;
        tabPane = (HBox)rootPane.getChildren().get(0);

        // pane properties that are hard to implement in CSS
        applicationPane.setPrefHeight(screenY);
        applicationPane.setPrefWidth(screenX);
        tabPane.setPrefHeight(screenY);
        tabPane.setPrefWidth(screenX);

        // todo make responsive
        // http://stackoverflow.com/questions/38216268/how-to-listen-resize-event-of-stage-in-javafx

        // set scene and stage
        mainScene = new Scene(applicationPane);
        primaryStage.setScene(mainScene);
        primaryStage.setMaximized(true);
        primaryStage.getScene().getStylesheets().add("/resources/stylesheets/stylesheet.css");
    }

    /**
     * Changes the tab displayed.
     *
     * @param pane the tab to show.
     */
    public void replaceTabContent(BorderPane pane) {
        try {
            BorderPane newApplicationPane = new FXMLLoader(getClass().getResource("/fxml/applicationPane.fxml")).load();
            newApplicationPane.setCenter(pane);
            newApplicationPane.autosize();
            primaryStage.getScene().setRoot(newApplicationPane);

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}