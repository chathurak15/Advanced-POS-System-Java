package org.example.controllers.user;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import org.example.dto.UserDTO;
import org.example.service.custom.impl.UserServiceIMPL;
import org.example.tm.UserTM;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsersController {
    public TextField txtSearch;
    public Button btnAddUser;
    public TableView<UserTM> tblUsers;
    public TableColumn<UserTM,Integer> colId;
    public TableColumn<UserTM,String> colName;
    public TableColumn<UserTM,String> colEmail;
    public TableColumn<UserTM,String> colRole;
    public TableColumn<UserTM,String> colUsername;
    public TableColumn<UserTM,Button> colAction;

    private final UserServiceIMPL userService = new UserServiceIMPL();
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

        // Display both Delete and Edit buttons in the Action column
        colAction.setCellFactory(param -> new TableCell<>() {
            @Override
            protected void updateItem(Button button, boolean empty) {
                super.updateItem(button, empty);
                if (empty || getTableRow() == null) {
                    setGraphic(null);
                } else {
                    UserTM userTM = getTableRow().getItem();
                    if (userTM != null) {
                        HBox actionButtons = new HBox(10, userTM.getButton(), userTM.getButtonEdit());
                        setGraphic(actionButtons);
                    }
                }
            }
        });
    }

    //delete user using user id. display conformation alert and after delete user form database.(userService)
    private void deleteUser(UserTM userTM) {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete this user?", ButtonType.YES, ButtonType.NO);
        confirmationAlert.setHeaderText(null);
        Optional<ButtonType> result = confirmationAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                boolean delete = userService.delete(userTM.getId());
                if (delete){
                    Alert infoAlert = new Alert(Alert.AlertType.INFORMATION,"User deleted successfully!");
                    infoAlert.setHeaderText(null);infoAlert.showAndWait();
                    loadTableData();
                }else {
                    Alert warnAlert = new Alert(Alert.AlertType.WARNING, "Error deleting user! Try again");
                    warnAlert.setHeaderText(null);warnAlert.showAndWait();
                }
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Error deleting user. Please contact Developer.").show();
            }
        }
    }

    //edit user method- load edituser.fxml and call editusercontroller setUserData method using userTM param....
    private void editUser(UserTM userTM) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/admin/user/EditUser.fxml"));
            Parent root = loader.load();

            EditUserController editUserController = loader.getController();
            editUserController.setUserData(userTM);

            submainPane.getChildren().clear();
            submainPane.getChildren().add(root);

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "UI- Load error || please Contact Developer||").show();
            e.printStackTrace();
        }
    }

    public void SearchOnClick(KeyEvent keyEvent) {
    }

    public void SearchIconOnClick(MouseEvent mouseEvent) {
    }

    public void AddUserOnAction(ActionEvent actionEvent) {
        try {
            Parent load = FXMLLoader.load(getClass().getResource("/view/admin/user/AddUser.fxml"));
            submainPane.getChildren().add(load);

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "UI- Load error || please Contact Developer||").show();
            e.printStackTrace();
        }
    }

    public void loadTableData() {
        try {
            List<UserTM> list = new ArrayList<>();
            List<UserDTO> allUsers = userService.getAll();
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

        // Set up Delete button
        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(event -> deleteUser(userTM));
        userTM.setButton(deleteButton);

        // Set up Edit button
        Button editButton = new Button("Edit");
        editButton.setOnAction(event -> editUser(userTM));
        userTM.setButtonEdit(editButton);

        return userTM;
    }
}
