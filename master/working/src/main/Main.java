package main;

import controllers.MenuController;
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

    public static Scene login;
    public static Pane mainPane;

    public static void main (String[] args)
    {
        Application.launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {

        //testing my parts GUI for now, you can change it (SHAKIB)


            mainPane = FXMLLoader.load(getClass().getResource("/resources/test.fxml"));
        //test.fxml
        login = new Scene(mainPane);
        primaryStage.setScene(login);
        primaryStage.show();
        primaryStage.setFullScreen(true);


    }




}
