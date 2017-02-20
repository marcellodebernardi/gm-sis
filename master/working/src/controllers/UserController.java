package controllers;

import domain.User;
import domain.UserType;
import javafx.scene.control.*;
import javafx.stage.Stage;
import logic.AuthenticationSystem;


import javax.xml.soap.Text;

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
                showAlert("User added: " + checker);
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

}
