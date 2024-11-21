package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import jdk.jshell.spi.ExecutionControl;
import org.example.dto.UserDTO;
import org.example.service.UserService;
import org.example.tm.UserTM;

import java.io.IOException;
import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UsersController {
    public TextField txtSearch;
    public Button btnAddUser;
    public TableView<UserTM> tblUsers;
    public TableColumn<UserTM,Integer> colId;
    public TableColumn<UserTM,String> colName;
    public TableColumn<UserTM,String> colEmail;
    public TableColumn<UserTM,String> colRole;
    public TableColumn<UserTM,String> colUsername;
    public TableColumn colAction;

    private final UserService userService = new UserService();
    public AnchorPane submainPane;

    public void initialize() {
        loadTableData();
        visulizeTable();
    }

    private void visulizeTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colAction.setCellValueFactory(new PropertyValueFactory<>(""));

    }

    public void SearchOnClick(KeyEvent keyEvent) {
    }

    public void SearchIconOnClick(MouseEvent mouseEvent) {
    }

    public void AddUserOnAction(ActionEvent actionEvent) {

        try {
            Parent load = FXMLLoader.load(getClass().getResource("/view/admin/AddUser.fxml"));
            submainPane.getChildren().add(load);

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "UI- Load error || please Contact Developer||").show();
            e.printStackTrace();
        }
    }

    public void loadTableData() {
        try {
            List<UserTM> list = new ArrayList<>();
            List<UserDTO> allUsers = userService.getAllUsers();
            for (UserDTO userDTO : allUsers) {
                UserTM userTM = convertUserDTOToTM(userDTO);
                list.add(userTM);
            }

            ObservableList<UserTM> userTMS = FXCollections.observableArrayList(list);
            tblUsers.setItems(userTMS);

        }catch (Exception e) {
            e.printStackTrace();
        }


    }

//convert userDTO To UserTM and set values
    private UserTM convertUserDTOToTM(UserDTO userDTO) {
        UserTM userTM = new UserTM();
        userTM.setId(userDTO.getId());
        userTM.setName(userDTO.getName());
        userTM.setUsername(userDTO.getUsername());
        userTM.setEmail(userDTO.getEmail());
        userTM.setRole(userDTO.getRole());

        return userTM;
    }
}
