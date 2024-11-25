package org.example.controllers.supplier;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import org.example.controllers.user.EditUserController;
import org.example.dto.SupplierDTO;
import org.example.service.custom.impl.SupplierServiceIMPL;
import org.example.tm.SupplierTM;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SupplierController {
    public AnchorPane submainPane;
    public TextField txtSearch;
    public Button btnAddUser;
    public TableView<SupplierTM> tblSuppliers;
    public TableColumn<SupplierTM,Integer> colId;
    public TableColumn<SupplierTM,String> colName;
    public TableColumn<SupplierTM,String> colEmail;
    public TableColumn<SupplierTM,String> colNumber;
    public TableColumn<SupplierTM,Button> colAction;
    private ObservableList<SupplierTM> masterData;

    private final SupplierServiceIMPL supplierService = new SupplierServiceIMPL();

    public void initialize() {
        visulizeTable();
        loadTableData();
    }

    public void visulizeTable(){
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colNumber.setCellValueFactory(new PropertyValueFactory<>("number"));

        colAction.setCellFactory(param -> new TableCell<>() {
            @Override
            protected void updateItem(Button button, boolean empty) {
                super.updateItem(button, empty);
                if (empty || getTableRow() == null) {
                    setGraphic(null);
                } else {
                    SupplierTM supplierTM = getTableRow().getItem();
                    if (supplierTM != null) {
                        HBox actionButtons = new HBox(10, supplierTM.getButton(), supplierTM.getButtonEdit());
                        actionButtons.setAlignment(Pos.CENTER);
                        actionButtons.setStyle("-fx-padding: 1;");
                        setGraphic(actionButtons);
                    }
                }
            }
        });
    }

    public void SearchOnClick(KeyEvent keyEvent) {
        setupSearch();
    }

    public void SearchIconOnClick(MouseEvent mouseEvent) {
    }

    public void AddUserOnAction(ActionEvent actionEvent) {
        try {
            Parent load = FXMLLoader.load(getClass().getResource("/view/stockmanager/suppliers/AddSupplier.fxml"));
            submainPane.getChildren().add(load);

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "UI- Load error || please Contact Developer||").show();
        }
    }

    private void editSupplier(SupplierTM supplierTM) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/stockmanager/suppliers/EditSupplier.fxml"));
            Parent root = loader.load();

            EditSupplierController editSupplierController = loader.getController();
            editSupplierController.setSupplierData(supplierTM);

            submainPane.getChildren().add(root);

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "UI- Load error || please Contact Developer||").show();
            e.printStackTrace();
        }
    }

    private void deleteSupplier(SupplierTM supplierTM) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want to delete this supplier?",ButtonType.YES,ButtonType.NO);
        alert.setHeaderText(null);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            boolean delete = supplierService.delete(supplierTM.getId());
            if (delete){
                Alert infoAlert = new Alert(Alert.AlertType.INFORMATION,"Supplier deleted successfully!");
                infoAlert.setHeaderText(null);infoAlert.showAndWait();
                loadTableData();
            }else {
                Alert warnAlert = new Alert(Alert.AlertType.WARNING, "Error deleting Supplier! Try again");
                warnAlert.setHeaderText(null);warnAlert.showAndWait();
            }
        }

    }

    public void loadTableData() {
        try {
            List<SupplierTM> list = new ArrayList<>();
            List<SupplierDTO> supplier = supplierService.getAll();
            for (SupplierDTO supplierDTO: supplier) {
                SupplierTM supplierTM = convertDTOtoTM(supplierDTO);
                list.add(supplierTM);
            }

            masterData = FXCollections.observableArrayList(list); // Update masterData
            tblSuppliers.setItems(masterData);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void setupSearch() {
        FilteredList<SupplierTM> filteredData = new FilteredList<>(masterData, p -> true);

        // Add a listener to the search field
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(supplier -> {
                // If search text is empty, display all rows
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare search text with supplier properties (case-insensitive)
                String lowerCaseFilter = newValue.toLowerCase();
                return supplier.getName().toLowerCase().contains(lowerCaseFilter) ||
                        supplier.getEmail().toLowerCase().contains(lowerCaseFilter);
            });
        });

        tblSuppliers.setItems(filteredData);
    }


    public SupplierTM convertDTOtoTM (SupplierDTO supplierDTO) {
        SupplierTM supplierTM = new SupplierTM ();
        supplierTM.setId(supplierDTO.getId());
        supplierTM.setName(supplierDTO.getName());
        supplierTM.setEmail(supplierDTO.getEmail());
        supplierTM.setNumber(supplierDTO.getNumber());

        Button deleteButton = new Button();
        Image deleteIcon = new Image("image/delete.png"); // Provide the correct path to your delete icon
        ImageView deleteIconView = new ImageView(deleteIcon);
        deleteIconView.setFitWidth(20);  // Set icon size
        deleteIconView.setFitHeight(20);
        deleteButton.setGraphic(deleteIconView);
        deleteButton.setOnAction(event -> deleteSupplier(supplierTM));
        supplierTM.setButton(deleteButton);

        Button editButton = new Button("Edit");
        editButton.setOnAction(event -> editSupplier(supplierTM));
        supplierTM.setButtonEdit(editButton);

        return supplierTM;
    }

}
