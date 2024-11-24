package org.example.controllers.supplier;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.example.dto.SupplierDTO;
import org.example.service.custom.impl.SupplierServiceIMPL;
import org.example.tm.SupplierTM;

import java.util.Optional;

public class EditSupplierController {

    public AnchorPane updateSupplierPane;
    public TextField txtName;
    public TextField txtEmail;
    public TextField txtNumber;

    private int id;
    private final SupplierServiceIMPL supplierService = new SupplierServiceIMPL();

    public void closeOnClick(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit?", ButtonType.YES, ButtonType.NO);
        alert.setHeaderText(null);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.YES) {

            try {
                updateSupplierPane.getChildren().clear();
                Parent product = FXMLLoader.load(getClass().getResource("/view/stockmanager/suppliers/Suppliers.fxml"));
                updateSupplierPane.getChildren().add(product);

            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "UI- Load error || please Contact Developer||").show();
                e.printStackTrace();
            }
        }
    }

    public void UpdateOnClick(ActionEvent actionEvent) {
        if (txtName.getText().isEmpty() || txtEmail.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");alert.setHeaderText(null);alert.setContentText("Please fill all the fields");
            alert.showAndWait();

        }else {
            String supplier = supplierService.update(collectData());
            if (supplier == "Supplier updated"){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");alert.setHeaderText(null);alert.setContentText("Supplier updated successfully!");
                alert.showAndWait();

                try {
                    updateSupplierPane.getChildren().clear();
                    Parent user = FXMLLoader.load(getClass().getResource("/view/stockmanager/suppliers/Suppliers.fxml"));
                    updateSupplierPane.getChildren().add(user);

                } catch (Exception e) {
                    new Alert(Alert.AlertType.ERROR, "UI- Load error || please Contact Developer||").show();
                }

            }else if (supplier == "SQL Error"){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");alert.setHeaderText(null);alert.setContentText("Supplier updated Failed! Try again");
            }else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");alert.setHeaderText(null);alert.setContentText("Something went wrong! Try again");
                alert.showAndWait();
            }

        }
    }

    public void setSupplierData(SupplierTM supplierTM) {
        txtName.setText(supplierTM.getName());
        txtEmail.setText(supplierTM.getEmail());
        txtNumber.setText(supplierTM.getNumber());
        id = supplierTM.getId();

    }

    public SupplierDTO collectData() {
        SupplierDTO supplierDTO = new SupplierDTO();
        supplierDTO.setName(txtName.getText());
        supplierDTO.setEmail(txtEmail.getText());
        supplierDTO.setNumber(txtNumber.getText());
        supplierDTO.setId(id);

        return supplierDTO;
    }
}
