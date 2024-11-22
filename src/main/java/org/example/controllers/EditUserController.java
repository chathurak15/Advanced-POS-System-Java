package org.example.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.example.dto.UserDTO;
import org.example.service.UserService;
import org.example.tm.UserTM;

import java.util.Optional;

public class EditUserController {

    public AnchorPane addUserPane;
    public TextField txtName;
    public TextField txtUsername;
    public TextField txtEmail;
    public ComboBox cmbUserRole;
    public PasswordField txtPassword;
    public PasswordField txtConformPassword;

    private  UserTM selectedUser;
    private final UserService userService = new UserService();;

    public int id;

    public void initialize() {
        cmbUserRole.getItems().addAll("cashier","stock manager", "Manager");

    }

    public void setUserData(UserTM userTM) {
        this.selectedUser = userTM;

        // Populate the fields with the selected user's data
        if (selectedUser != null) {
            id = selectedUser.getId();
            txtName.setText(selectedUser.getName());
            txtUsername.setText(selectedUser.getUsername());
            txtEmail.setText(selectedUser.getEmail());
        }
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

    public void UpdateOnClick(ActionEvent actionEvent) {

        if (txtName.getText().isEmpty()||txtUsername.getText().isEmpty()||
                txtEmail.getText().isEmpty()||txtPassword.getText().isEmpty()||txtConformPassword.getText().isEmpty()||
                cmbUserRole.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);alert.setContentText("Please fill all the fields");alert.showAndWait();
        }else {

            //password validation
            if (!txtPassword.getText().equals(txtConformPassword.getText())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);alert.setContentText("Passwords do not match");alert.showAndWait();
            }else {

                String updateUser = userService.updateUser(collectData());
                System.out.println(updateUser);
                if (updateUser.equals("User Updated")) {
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
                }else if(updateUser.equals("Duplicate User")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);alert.setContentText("User Name or Email already exited! | Try again using uniqe details");alert.showAndWait();

                }else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);alert.setContentText("Something Wrong Please check and try again!");alert.showAndWait();
                }
            }
        }

    }

    private UserDTO collectData(){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(id);
        userDTO.setName(txtName.getText());
        userDTO.setUsername(txtUsername.getText());
        userDTO.setEmail(txtEmail.getText());
        userDTO.setRole(cmbUserRole.getSelectionModel().getSelectedItem().toString());
        userDTO.setPassword(txtPassword.getText());

        return userDTO;
    }
}
