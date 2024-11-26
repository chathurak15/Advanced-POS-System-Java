package org.example.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.dto.UserDTO;
import org.example.service.custom.impl.UserServiceIMPL;
import org.example.repo.custom.impl.UserRepoIMPL;

import java.io.IOException;
import java.util.Objects;

public class LoginFormController {
    public TextField txtUserName;
    public PasswordField txtPassword;

    private final UserServiceIMPL userService = new UserServiceIMPL(new UserRepoIMPL());

    public void LoginOnClick(ActionEvent actionEvent) {

        //FORM VALIDATION (Empty FILED VALIDATION)
        if (txtUserName.getText().isEmpty() || txtPassword.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Please enter all the fields");
            return;
        }

        String username = txtUserName.getText();
        String password = txtPassword.getText();

        UserDTO user = userService.loginUser(username, password);

        //CHECK USER IS AVAILABLE FOR ABOVE DETAILS
        if (user != null) {
            showAlert(Alert.AlertType.INFORMATION, "Login Successful!");
            navigateUserDashboard(user.getRole());
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Failed!");
        }
    }

    //CHECK ROLE AND NAVIGATE DASHBOARD ABOVE LOGIN USER
    private void navigateUserDashboard(String role) {
        switch (role.toUpperCase()) {
            case "MANAGER" -> {
                try {
                    Stage window = (Stage) txtUserName.getScene().getWindow();
                    window.close();
                    Stage stage = new Stage();
                    Parent load = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/admin/Index.fxml")));
                    Scene scene = new Scene(load);
                    stage.setScene(scene);
                    stage.show();

                } catch (IOException e) {
                    new Alert(Alert.AlertType.ERROR,  "Dashboard issue. please contact Developer").show();

                }
            }
            case "CASHIER" -> {
                try {
                    Stage window = (Stage) txtUserName.getScene().getWindow();
                    window.close();
                    Stage stage = new Stage();
                    Parent load = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/cashier/Dashboard.fxml")));
                    Scene scene = new Scene(load);
                    stage.setScene(scene);
                    stage.show();

                } catch (IOException e) {
                    new Alert(Alert.AlertType.ERROR,  "Dashboard issue. please contact Developer").show();
                }
            }
            case "STOCK MANAGER" -> {
                try {
                    Stage window = (Stage) txtUserName.getScene().getWindow();
                    window.close();
                    Stage stage = new Stage();
                    Parent load = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/stockmanager/Index.fxml")));
                    Scene scene = new Scene(load);
                    stage.setScene(scene);
                    stage.show();

                } catch (IOException e) {
                    new Alert(Alert.AlertType.ERROR,  "Dashboard issue. please contact Developer").show();
                 }
            }
            default -> showAlert(Alert.AlertType.ERROR, "Invalid role!");
        }
    }

    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Notification");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
