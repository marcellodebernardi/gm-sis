package controllers;

import domain.User;
import domain.UserType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.BooleanStringConverter;
import javafx.util.converter.DateStringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import logic.AuthenticationSystem;


import javax.xml.soap.Text;
import java.util.Date;
import java.util.List;

/**
 * Created by DillonVaghela on 2/20/17.
 */
public class UserController {

    public Button USBTN;
    public TextField UID;
    public TextField P;
    public TextField FN;
    public TextField SN;
    public ComboBox UT;
    public TextField SUID;
    private AuthenticationSystem auth = AuthenticationSystem.getInstance();
    public TableView<User> tUsers;
    public TableColumn<User, String> userID;
    public TableColumn<User, String> password;
    public TableColumn<User, String> firstname;
    public TableColumn<User, String> surname;
    public TableColumn<User, UserType> userType;
    final ObservableList<User> tableEntries = FXCollections.observableArrayList();

    public void addEditUser() throws Exception
    {
        String addOrEdit;
        if (USBTN != null)
        {
            addOrEdit = "edit";
            // true is edit
        }
        else
        {
            addOrEdit = "add";
            // false is add
        }
        boolean checkFields = checkFields();
        if (!checkFields)
        {
            showAlert("complete all fields");
        }
        else {
            boolean add = showAlertC("Are you sure you want to " + addOrEdit +" this User, have you checked the User details?");
            if (add) {
                UserType userType;
                if (UT.getSelectionModel().getSelectedItem().toString().equals("Admin"))
                {
                    userType = UserType.ADMINISTRATOR;
                }
                else {
                    userType = UserType.NORMAL;
                }
                boolean checker = auth.addEditUser(UID.getText(), P.getText(), FN.getText(),SN.getText(),userType);
                showAlert("User "+addOrEdit+": " + checker);
                if (checker)
                {
                    Stage addStage = (Stage) UID.getScene().getWindow();
                    addStage.close();
                }
            }
        }
    }

    public void deleteUser()
    {
        try {
                if (UID.getText().equals(auth.getLoggedInUser()))
                {
                    showAlert("cant delete yourself");
                    return;
                }
                else {
                    if (!showAlertC("Sure you want to delete?"))
                    {
                        return;
                    }
                    boolean check = auth.deleteUser(UID.getText());
                    showAlert("User deleted: " + check);
                }
        }
        catch (Exception e)
        {
            showAlert("cant delete User, check User ID entered");
        }
    }

    public void sUser()
    {
        try{
            User user = auth.searchAUser(SUID.getText());
            showAlert("User Found!");
            setUserDets(user);
        }
        catch (Exception e)
        {
            showAlert("Registration Number invalid");
            System.out.println(e);
        }
    }

    public void setUserDets(User user)
    {
        UID.setText(user.getUserID());
        UID.setDisable(false);
        P.setText(user.getPassword());
        P.setDisable(false);
        FN.setText(user.getFirstName());
        FN.setDisable(false);
        SN.setText(user.getSurname());
        SN.setDisable(false);
        String theUser;
         if (user.getUserType().toString().equals("NORMAL"))
         {
             theUser = "Normal";
         }
         else {
             theUser = "Admin";
         }
        UT.setValue(theUser);
        UT.setDisable(false);
    }

    public boolean checkFields()
    {
        if ((!UID.getText().equals("")) && (!P.getText().equals("")) && (!FN.getText().equals("")) && (!SN.getText().equals("")) && (!UT.getSelectionModel().getSelectedItem().toString().equals("")) ) {
            return true;
        }
        return false;
    }

    public void showAlert(String message)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Message");
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    public void AllUsers()
    {
        List<User> arrayList = auth.getUsersList();
        DisplayTable(arrayList);
    }

    public boolean showAlertC(String message)
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(message);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK){
            return true;
        } else {
            return false;
        }
    }

    public void DisplayTable(List<User> arrayList)
    {
        try {
            tUsers.setDisable(false);
            tableEntries.removeAll(tableEntries);
            for(int i =0; i<arrayList.size(); i++){

                tableEntries.add(arrayList.get(i));
            }

            userID.setCellValueFactory(new PropertyValueFactory<User, String>("userID"));
            userID.setCellFactory(TextFieldTableCell.<User>forTableColumn( ));
            //userID.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<User, String>>() {
             //   @Override
              //  public void handle(TableColumn.CellEditEvent<User, String> event) {
               //     ( event.getTableView().getItems().get(event.getTablePosition().getRow())).set(event.getNewValue());
                //}
            //});

            password.setCellValueFactory(new PropertyValueFactory<User, String>("password"));
            password.setCellFactory(TextFieldTableCell.<User>forTableColumn());
            password.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<User, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<User, String> event) {
                    ( event.getTableView().getItems().get(event.getTablePosition().getRow())).setPassword(event.getNewValue());
                }
            });

            firstname.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
            firstname.setCellFactory(TextFieldTableCell.<User>forTableColumn( ));
            firstname.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<User, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<User, String> event) {
                    ( event.getTableView().getItems().get(event.getTablePosition().getRow())).setFirstName(event.getNewValue());
                }
            });

            surname.setCellValueFactory(new PropertyValueFactory<User, String>("surname"));
            surname.setCellFactory(TextFieldTableCell.<User>forTableColumn());
            surname.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<User, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<User, String> event) {
                    ( event.getTableView().getItems().get(event.getTablePosition().getRow())).setSurname(event.getNewValue());
                }
            });


            userType.setCellValueFactory(new PropertyValueFactory<User, UserType>("userType"));
            userType.setCellFactory(TextFieldTableCell.<User, UserType>forTableColumn(new StringConverter<UserType>() {
                @Override
                public String toString(UserType object) {
                    return null;
                }

                @Override
                public UserType fromString(String string) {
                    return null;
                }
            }));
            userType.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<User, UserType>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<User, UserType> event) {

                    ( event.getTableView().getItems().get(event.getTablePosition().getRow())).setUserType(event.getNewValue());
                }
            });



            tUsers.setItems(tableEntries);
        }
        catch (Exception e)
        {
            System.out.println("not working");
            e.printStackTrace(  );
            System.out.println(e);
        }

    }

}
