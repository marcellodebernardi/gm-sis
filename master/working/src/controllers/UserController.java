package controllers;

import domain.User;
import domain.UserType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import logic.AuthenticationSystem;

import java.util.List;

/**
 * Created by DillonVaghela on 2/20/17.
 */
public class UserController {

    private AuthenticationSystem auth = AuthenticationSystem.getInstance();

    @FXML
    private Button addButton, newButton, clearButton, deleteButton;

    @FXML
    private Label userLabel;

    @FXML
    private TextField UID, P, FN, SN, sUID, sFN, sS;

    @FXML
    private ComboBox UT, sUT;

    @FXML
    private TableView tUsers;

    @FXML
    private TableColumn userID, password, firstname, surname, userType;

    final ObservableList tableEntries = FXCollections.observableArrayList();

    public void addEditUser() throws Exception {
        String addOrEdit;
        if (userLabel.equals("Edit User")) {
            addOrEdit = "edit";
            // true is edit
        } else {
            addOrEdit = "add";
            // false is add
        }
        boolean checkFields = checkFields();
        if (!checkFields) {
            showAlert("complete all fields");
        } else {
            boolean add = showAlertC("Are you sure you want to " + addOrEdit + " this User, have you checked the User details?");
            if (add) {
                UserType userType;
                if (UT.getSelectionModel().getSelectedItem().toString().equals("Admin")) {
                    userType = UserType.ADMINISTRATOR;
                } else {
                    userType = UserType.NORMAL;
                }
                boolean checker = auth.addEditUser(UID.getText(), P.getText(), FN.getText(), SN.getText(), userType);
                showAlert("User " + addOrEdit + ": " + checker);
                if (checker) {
                    Stage addStage = (Stage) UID.getScene().getWindow();
                    addStage.close();
                }
            }
        }
    }

    public void deleteUser() {
        try {
            if (UID.getText().equals(auth.getLoggedInUser())) {
                showAlert("cant delete yourself");
                return;
            } else {
                if (!showAlertC("Sure you want to delete?")) {
                    return;
                }
                delete(UID.getText());
            }
        } catch (Exception e) {
            showAlert("cant delete User, check User ID entered");
        }
    }

    public void DeleteFromList()
    {
        User user =((User) tUsers.getSelectionModel().getSelectedItem());
        delete(user.getUserID());
    }

    public void delete(String userID)
    {
        try {
            boolean check = auth.deleteUser(userID);
            showAlert("User deleted: " + check);
        }
        catch (Exception e) {
            showAlert("cant delete User, check User ID entered/selected");
        }
    }

    public void getUser() {
        try {
            User user = ((User) tUsers.getSelectionModel().getSelectedItem());
            setUserDets(user);
        } catch (Exception e) {
            showAlert("No selected Item");
        }
    }

    public void setUserDets(User user) {
        UID.setText(user.getUserID());
        P.setText(user.getPassword());
        FN.setText(user.getFirstName());
        SN.setText(user.getSurname());
        String theUser;
        if (user.getUserType().toString().equals("NORMAL")) {
            theUser = "Normal";
        } else {
            theUser = "Admin";
        }
        UT.setValue(theUser);
        userLabel.setText("Edit User");
        addButton.setText("Edit");
        clearButton.setDisable(true);
        newButton.setDisable(false);
        deleteButton.setDisable(false);
    }

    public boolean checkFields() {
        if ((!UID.getText().equals("")) && (!P.getText().equals("")) && (!FN.getText().equals("")) && (!SN.getText().equals("")) && (!UT.getSelectionModel().getSelectedItem().toString().equals(""))) {
            return true;
        }
        return false;
    }

    public void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Message");
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    public void SearchUsers()
    {
        UserType UT;
         if (sUT.getSelectionModel().getSelectedItem().toString().equals("Admin"))
         {
             UT = UserType.ADMINISTRATOR;
         }
         else
         {
             UT = UserType.NORMAL;
         }
        List<User> arrayList = auth.searchUsers(sUID.getText(), sFN.getText(), sS.getText(), UT);
        DisplayTable(arrayList);
    }

    public void AllUsers() {
        List<User> arrayList = auth.getUsersList();
        DisplayTable(arrayList);
    }

    public boolean showAlertC(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(message);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }
    }

    public void DisplayTable(List<User> arrayList) {
        if (arrayList.size() == 0)
        {
            showAlert("Nothing to display");
            return;
        }
        try {
            tUsers.setDisable(false);
            tableEntries.removeAll(tableEntries);
            for (int i = 0; i < arrayList.size(); i++) {

                tableEntries.add(arrayList.get(i));
            }

            userID.setCellValueFactory(new PropertyValueFactory<User, String>("userID"));
            userID.setCellFactory(TextFieldTableCell.<User>forTableColumn());
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
                    (event.getTableView().getItems().get(event.getTablePosition().getRow())).setPassword(event.getNewValue());
                }
            });

            firstname.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
            firstname.setCellFactory(TextFieldTableCell.<User>forTableColumn());
            firstname.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<User, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<User, String> event) {
                    (event.getTableView().getItems().get(event.getTablePosition().getRow())).setFirstName(event.getNewValue());
                }
            });

            surname.setCellValueFactory(new PropertyValueFactory<User, String>("surname"));
            surname.setCellFactory(TextFieldTableCell.<User>forTableColumn());
            surname.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<User, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<User, String> event) {
                    (event.getTableView().getItems().get(event.getTablePosition().getRow())).setSurname(event.getNewValue());
                }
            });


            userType.setCellValueFactory(new PropertyValueFactory<User, UserType>("userType"));
            userType.setCellFactory(TextFieldTableCell.<User, UserType>forTableColumn(new StringConverter<UserType>() {
                @Override
                public String toString(UserType object) {
                    return object.toString();
                }

                @Override
                public UserType fromString(String string) {
                    if (string.equals("ADMINISTRATOR"))
                    {
                        return UserType.ADMINISTRATOR;
                    }
                    return UserType.NORMAL;
                }
            }));
            userType.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<User, UserType>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<User, UserType> event) {

                    (event.getTableView().getItems().get(event.getTablePosition().getRow())).setUserType(event.getNewValue());
                }
            });


            tUsers.setItems(tableEntries);
        } catch (Exception e) {
            showAlert("Error Displaying table");
        }

    }

    public void ClearFields()
    {
        UID.clear();
        P.clear();
        FN.clear();
        SN.clear();
        UT.getSelectionModel().clearSelection();
    }

    public void NewVehicle()
    {
        ClearFields();
        userLabel.setText("Add User");
        addButton.setText("Add");
        clearButton.setDisable(false);
        newButton.setDisable(true);
        deleteButton.setDisable(true);
    }

}
