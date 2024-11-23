package org.example.controllers.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.example.dto.UserDTO;
import org.example.service.custom.impl.UserServiceIMPL;

import java.util.Optional;

public class AddUserController {
    public TextField txtName;
    public TextField txtUsername;
    public TextField txtEmail;
    public ComboBox cmbUserRole;
    public PasswordField txtPassword;
    public PasswordField txtConformPassword;
    public AnchorPane addUserPane;

    private final UserServiceIMPL userService = new UserServiceIMPL();

    public void initialize() {
        cmbUserRole.getItems().addAll("cashier","stock manager", "Manager");

    }

    public void closeOnClick(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit?", ButtonType.YES, ButtonType.NO);
        alert.setHeaderText(null);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.YES) {
            try {
                addUserPane.getChildren().clear();
                Parent user = FXMLLoader.load(getClass().getResource("/view/admin/user/Users.fxml"));
                addUserPane.getChildren().add(user);

            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "UI- Load error || please Contact Developer||").show();
                e.printStackTrace();
            }
        }
    }

    public void RegisterOnClick(ActionEvent actionEvent) {

        String name = txtName.getText();
        String username = txtUsername.getText();
        String email = txtEmail.getText();
        String EmployeeRole = cmbUserRole.getSelectionModel().getSelectedItem().toString();
        String password = txtPassword.getText();
        String conformPassword = txtConformPassword.getText();

        //filed Validation
        if (name.isEmpty()||username.isEmpty()||email.isEmpty()||password.isEmpty()||conformPassword.isEmpty()||EmployeeRole.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);alert.setContentText("Please fill all the fields");alert.showAndWait();
        }else {

            //password validation
            if (!password.equals(conformPassword)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);alert.setContentText("Passwords do not match");alert.showAndWait();
            }else {

                String addUser = userService.save(collectUserData());
                if (addUser.equals("User Saved")) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setContentText("User added successfully!");
                    alert.showAndWait();

                    try {
                        addUserPane.getChildren().clear();
                        Parent user = FXMLLoader.load(getClass().getResource("/view/admin/user/Users.fxml"));
                        addUserPane.getChildren().add(user);

                    } catch (Exception e) {
                        new Alert(Alert.AlertType.ERROR, "UI- Load error || please Contact Developer||").show();
                        e.printStackTrace();
                    }
                }else if(addUser.equals("Duplicate User")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);alert.setContentText("User Name or Email already exited! | Try again using uniqe details");alert.showAndWait();

                }else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);alert.setContentText("Some thing Wrong Please check and try again!");alert.showAndWait();
                }
            }
        }
    }

    //
    private UserDTO collectUserData(){
        UserDTO userDTO = new UserDTO();
        userDTO.setName(txtName.getText());
        userDTO.setUsername(txtUsername.getText());
        userDTO.setEmail(txtEmail.getText());
        userDTO.setRole(cmbUserRole.getSelectionModel().getSelectedItem().toString());
        userDTO.setPassword(txtPassword.getText());
        return userDTO;
    }


}
