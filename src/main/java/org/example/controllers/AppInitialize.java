package org.example.controllers;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class AppInitialize extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        try {
            Parent load = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/LoginForm.fxml")));
            Scene scene = new Scene(load);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, e+"Faild load Login please contact Developer").show();
        }

    }
}
