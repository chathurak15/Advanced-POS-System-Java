package org.example.controllers.product;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.example.dto.ProductDTO;
import org.example.service.custom.impl.ProductServiceIMPL;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class AddProductController {
    public AnchorPane addProductPane;
    public TextField txtName;
    public TextField txtCategory;
    public TextField txtPrice;
    public TextField txtCost;
    public TextField txtQty;
    public ComboBox cmbSupplier;
    public DatePicker datePicker;

    private final ProductServiceIMPL productService = new ProductServiceIMPL();

    public void initialize() {
        cmbSupplier.getItems().addAll("cashier","stock manager", "Manager");
    }

    public void closeOnClick(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit?", ButtonType.YES, ButtonType.NO);
        alert.setHeaderText(null);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.YES) {

            try {
                addProductPane.getChildren().clear();
                Parent product = FXMLLoader.load(getClass().getResource("/view/admin/product/Products.fxml"));
                addProductPane.getChildren().add(product);

            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "UI- Load error || please Contact Developer||").show();
                e.printStackTrace();
            }
        }
    }

    public void AddOnClick(ActionEvent actionEvent) {

        if (txtName.getText().isEmpty()||txtCategory.getText().isEmpty()||txtPrice.getText().isEmpty()|| txtCost.getText().isEmpty()||
                txtQty.getText().isEmpty()||cmbSupplier.getSelectionModel().isEmpty()||datePicker.getValue() == null) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);alert.setContentText("Please fill all the required fields!");alert.showAndWait();

        }else {
            String addProduct = productService.save(collectData());

            if (addProduct.equals("Product saved")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("Product added successfully!");
                alert.showAndWait();

                try {
                    addProductPane.getChildren().clear();
                    Parent user = FXMLLoader.load(getClass().getResource("/view/admin/product/Products.fxml"));
                    addProductPane.getChildren().add(user);

                } catch (Exception e) {
                    new Alert(Alert.AlertType.ERROR, "UI- Load error || please Contact Developer||").show();
                    e.printStackTrace();
                }
            }else if(addProduct.equals("Sql Error")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);alert.setContentText("Product add Failed! Try again!");alert.showAndWait();

            }else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);alert.setContentText("Some thing Wrong Please check and try again!");alert.showAndWait();
            }

        }

    }

    public ProductDTO collectData(){
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName(txtName.getText());
        productDTO.setCategory(txtCategory.getText());
        productDTO.setPrice(Double.valueOf(txtPrice.getText()));
        productDTO.setCost(Double.valueOf(txtCost.getText()));
        productDTO.setSupplierid(cmbSupplier.getSelectionModel().getSelectedIndex());
        productDTO.setExpirydate(datePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        productDTO.setDate(date());

        return productDTO;
    }

    public String date(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }
}
