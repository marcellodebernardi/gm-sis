package main;

import controllers.MenuController;
import javafx.application.Application;
import javafx.fxml.LoadException;
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

        try {
            mainPane = FXMLLoader.load(getClass().getResource("/resources/PartModule.fxml"));
            //test.fxml
            login = new Scene(mainPane);
            primaryStage.setScene(login);
            primaryStage.show();
            //primaryStage.setFullScreen(true);
        }
        catch(LoadException e)
        {
            e.printStackTrace();
        }


    }
    

}
