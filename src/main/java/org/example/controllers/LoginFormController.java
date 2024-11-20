package org.example.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

public class LoginFormController {

    public TextField txtUserName;
    public PasswordField txtPassword;
    public Button btnLogin;

    public void LoginOnClick(ActionEvent actionEvent) {

        if(txtUserName.getText().isEmpty() || txtPassword.getText().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error"); alert.setHeaderText(null);  alert.setContentText("Please enter all the fields"); alert.showAndWait();

        } else {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Login Success"); alert.setHeaderText(null); alert.setContentText("Login Successful!"); alert.showAndWait();

            Stage window = (Stage) txtUserName.getScene().getWindow();
            window.close();

            Stage stage = new Stage();
            try {
                Parent load = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/Template.fxml")));
                Scene scene = new Scene(load);
                stage.setScene(scene);
                stage.show();

            } catch (IOException e) {
                new Alert(Alert.AlertType.ERROR, e+"Dashboard issue. please contact Developer").show();
            }
        }
    }
}
