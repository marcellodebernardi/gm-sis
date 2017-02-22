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
    private AnchorPane applicationPane;
    private TabPane tabPane;


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
    public void setRootPane(AnchorPane rootPane) {
        applicationPane = rootPane;
        tabPane = (TabPane)rootPane.getChildren().get(0);

        // pane properties that are hard to implement in CSS
        applicationPane.setPrefHeight(screenY);
        applicationPane.setPrefWidth(screenX);
        tabPane.setPrefHeight(screenY);
        tabPane.setPrefWidth(screenX);

        // todo make responsive
        // http://stackoverflow.com/questions/38216268/how-to-listen-resize-event-of-stage-in-javafx
        tabPane.setTabMinWidth(screenX/8);
        tabPane.setTabMinHeight(screenY/20);

        // set scene and stage
        mainScene = new Scene(applicationPane);
        primaryStage.setScene(mainScene);
        primaryStage.setMaximized(true);
        primaryStage.getScene().getStylesheets().add("/resources/stylesheets/stylesheet.css");
    }

    public void replaceTabContent(Pane pane) {
        // todo redraw specific tab in ApplicationPane
    }
}