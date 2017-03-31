package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.LoadException;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import jfxtras.resources.JFXtrasFontRoboto;
import persistence.DatabaseRepository;

import java.io.IOException;

/**
 * @author Marcello De Bernardi, Dillon Vaghela
 * @version 0.1
 * @since 0.1
 */
public class Main extends Application {
    private static Main application;

    // screen dimensions and other dimensions
    private double screenX = Screen.getPrimary().getVisualBounds().getWidth();
    private double screenY = Screen.getPrimary().getVisualBounds().getHeight();
    private double tabWidth = screenX / 7.05;

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
    public static void main(String[] args) {
        Application.launch(args);
    }

    /**
     * Returns the instance of Main. Allows controllers to interact with top-level GUI logic.
     *
     * @return the JavaFX application
     */
    public static Main getInstance() {
        return application;
    }

    @Override
    public void init() {
        application = this;
        DatabaseRepository.getInstance();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        JFXtrasFontRoboto.loadAll();
        this.primaryStage.getIcons().add(new Image("/resources/common/images/icon.png"));

        try {
            BorderPane loginPane = FXMLLoader.load(getClass().getResource("/common/loginPane.fxml"));
            loginPane.getStylesheets().add("/common/common.css");
            this.primaryStage.setScene(new Scene(loginPane));
            this.primaryStage.show();
        }
        catch (LoadException e) {
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
     * Takes the main scene from the login controller. The main scene cannot be loaded in Main,
     * because it has behavior that assumes that a login attempt has been made.
     */
    public void setRootPane(BorderPane rootPane) {
        applicationPane = rootPane;
        tabPane = (HBox) rootPane.getChildren().get(0);
        // pane properties that are hard to implement in CSS
        applicationPane.setPrefHeight(screenY);
        applicationPane.setPrefWidth(screenX);
        tabPane.setPrefHeight(screenY);
        tabPane.setPrefWidth(screenX);

        for (Node b : tabPane.getChildren()) {
            if (b instanceof Button) {
                ((Button) b).setPrefWidth(tabWidth);
            }
            else if (b instanceof GridPane) {
                ((GridPane) b).setPrefWidth(tabWidth);
            }
        }

        // set scene and stage
        mainScene = new Scene(applicationPane);

        primaryStage.setScene(mainScene);
        primaryStage.setMaximized(true);
        primaryStage.setResizable(true);
    }

    /**
     * Changes the tab displayed.
     *
     * @param pane the tab to show.
     */
    public void replaceTabContent(BorderPane pane) {
        try {
            BorderPane newApplicationPane = new FXMLLoader(getClass().getResource("/common/applicationPane.fxml")).load();
            newApplicationPane.setCenter(pane);

            for (Node b : ((HBox) newApplicationPane.getTop()).getChildren()) {
                if (b instanceof Button) {
                    ((Button) b).setPrefWidth(tabWidth);
                }
                else if (b instanceof GridPane) {
                    ((GridPane) b).setPrefWidth(tabWidth);
                }
            }

            primaryStage.getScene().setRoot(newApplicationPane);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}