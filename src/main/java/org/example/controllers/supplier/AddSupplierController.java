package org.example.controllers.supplier;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.example.dto.SupplierDTO;
import org.example.service.custom.impl.SupplierServiceIMPL;

import java.util.Optional;

public class AddSupplierController {
    public AnchorPane addSupplierPane;
    public TextField txtName;
    public TextField txtEmail;
    public TextField txtNumber;

    private final SupplierServiceIMPL supplierService = new SupplierServiceIMPL();
    public void closeOnClick(ActionEvent actionEvent) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want Exit?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Confirmation");alert.setHeaderText(null);
        Optional<ButtonType> result = alert.showAndWait();

        if(result.get() == ButtonType.YES){
            try {
                addSupplierPane.getChildren().clear();
                Parent supplier = FXMLLoader.load(getClass().getResource("/view/stockmanager/suppliers/Suppliers.fxml"));
                addSupplierPane.getChildren().add(supplier);

            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "UI- Load error || please Contact Developer||").show();
                e.printStackTrace();
            }

        }


    }

    public void AddOnClick(ActionEvent actionEvent) {
        if(txtName.getText().isEmpty()||txtEmail.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");alert.setHeaderText(null);alert.setContentText("Please fill all the fields");
            alert.showAndWait();
        }else{
            String save = supplierService.save(CollectData());
            if (save == "saved") {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");alert.setHeaderText(null);alert.setContentText("Supplier added successfully");
                alert.showAndWait();

                try {
                    addSupplierPane.getChildren().clear();
                    Parent user = FXMLLoader.load(getClass().getResource("/view/stockmanager/suppliers/Suppliers.fxml"));
                    addSupplierPane.getChildren().add(user);

                } catch (Exception e) {
                    new Alert(Alert.AlertType.ERROR, "UI- Load error || please Contact Developer||").show();
                    e.printStackTrace();
                }

            }else if(save == "Duplicate Email") {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");alert.setHeaderText(null);alert.setContentText("Supplier Email already exists! PLease try Again");
                alert.showAndWait();
            }else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");alert.setHeaderText(null);alert.setContentText("Something went wrong! Try Again");
            }
        }
    }

    public SupplierDTO CollectData(){
        SupplierDTO supplierDTO = new SupplierDTO();
        supplierDTO.setName(txtName.getText());
        supplierDTO.setEmail(txtEmail.getText());
        supplierDTO.setNumber(txtNumber.getText());
        return supplierDTO;
    }
}
