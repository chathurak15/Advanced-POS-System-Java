package org.example.controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.Optional;

public class AddUserController {
    public TextField txtName;
    public TextField txtUsername;
    public TextField txtEmail;
    public ComboBox cmbUserRole;
    public PasswordField txtPassword;
    public PasswordField txtConforimPassword;

    public void closeOnClick(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.YES) {
            Stage stage = (Stage) txtName.getScene().getWindow();
            stage.close();
        }
    }

    public void RegisterOnClick(ActionEvent actionEvent) {
    }
}
