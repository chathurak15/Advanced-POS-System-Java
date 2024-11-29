package org.example.controllers.product;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.example.dto.ProductDTO;
import org.example.dto.SupplierDTO;
import org.example.service.custom.impl.ProductServiceIMPL;
import org.example.service.custom.impl.SupplierServiceIMPL;
import org.example.tm.ProductTM;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class EditProductController {
    public AnchorPane addProductPane;
    public TextField txtName;
    public TextField txtCategory;
    public TextField txtPrice;
    public TextField txtCost;
    public TextField txtQty;
    public ComboBox cmbSupplier;
    public DatePicker datePicker;
    public TextField txtDiscount;

    private double price;
    private double cost;
    private int id;

    private final ProductServiceIMPL productService = new ProductServiceIMPL();
    private final SupplierServiceIMPL supplierService = new SupplierServiceIMPL();

    public void initialize() {
        getSelectedSupplierId();
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

        boolean pricevalidation = txtPrice.getText().matches("-?\\d+(\\.\\d+)?") && txtCost.getText().matches("-?\\d+(\\.\\d+)?");

        if (txtName.getText().isEmpty()||txtCategory.getText().isEmpty()||txtPrice.getText().isEmpty()|| txtCost.getText().isEmpty()||
                txtQty.getText().isEmpty()||cmbSupplier.getSelectionModel().isEmpty()||datePicker.getValue() == null) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);alert.setContentText("Please fill all the required fields!");alert.showAndWait();

        }else if(!pricevalidation) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null); alert.setContentText("Please enter a valid price or cost!");alert.showAndWait();

        }else {
            price = Double.parseDouble(txtPrice.getText().trim());
            cost = Double.parseDouble(txtCost.getText().trim());

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

    private Integer getSelectedSupplierId() {

        Map<String, Integer> supplierMap = new HashMap<>();

        List<SupplierDTO> suppliers = supplierService.getAllname();
        if (suppliers != null && !suppliers.isEmpty()) {
            for (SupplierDTO supplier : suppliers) {
                cmbSupplier.getItems().add(supplier.getName());
                supplierMap.put(supplier.getName(), supplier.getId()); // Associate name with ID
            }
        } else {
            throw new RuntimeException("No suppliers found.");
        }

        cmbSupplier.setOnAction(event -> {
            String selectedName = cmbSupplier.getValue().toString();
            if (selectedName != null) {
                Integer supplierId = supplierMap.get(selectedName);
            }
        });

        String selectedName = String.valueOf(cmbSupplier.getValue());
        if (selectedName != null) {
            return supplierMap.get(selectedName);
        } else {
            return null;
        }
    }

    public ProductDTO collectData(){
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName(txtName.getText());
        productDTO.setCategory(txtCategory.getText());
        productDTO.setPrice(price);
        productDTO.setCost(cost);
        productDTO.setQuantity(Integer.parseInt(txtQty.getText()));
        productDTO.setSupplierid(getSelectedSupplierId());
        productDTO.setExpirydate(datePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        productDTO.setDate(date());
        productDTO.setDiscount(txtDiscount.getText());
        productDTO.setId(id);

        return productDTO;
    }

    public String date(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    public void setProductData(ProductTM productTM) {
        id = productTM.getId();
        txtName.setText(productTM.getName());
        txtCategory.setText(productTM.getCategory());
        txtPrice.setText(Double.toString(productTM.getPrice()));
        txtQty.setText(String.valueOf(productTM.getQuantity()));
        txtCost.setText(String.valueOf(productTM.getCost()));

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Adjust pattern to match your date format

        try {
            if (productTM.getExpdate() != null && !productTM.getExpdate().isEmpty()) {
                LocalDate expDate = LocalDate.parse(productTM.getExpdate(), dateFormatter);
                datePicker.setValue(expDate);

                // Set a DayCellFactory to customize the DatePicker
                datePicker.setDayCellFactory(dp -> new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                    }
                });
            }
        } catch (DateTimeParseException e) {
            System.err.println("Invalid date format in Expdate: " + productTM.getExpdate());
        }

    }

    public void updateOnAction(ActionEvent actionEvent) {
        boolean pricevalidation = txtPrice.getText().matches("-?\\d+(\\.\\d+)?") && txtCost.getText().matches("-?\\d+(\\.\\d+)?");

        if (txtName.getText().isEmpty()||txtCategory.getText().isEmpty()||txtPrice.getText().isEmpty()|| txtCost.getText().isEmpty()||
                txtQty.getText().isEmpty()||cmbSupplier.getSelectionModel().isEmpty()||datePicker.getValue() == null) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);alert.setContentText("Please fill all the required fields!");alert.showAndWait();

        }else if(!pricevalidation) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null); alert.setContentText("Please enter a valid price or cost!");alert.showAndWait();

        }else {
            price = Double.parseDouble(txtPrice.getText().trim());
            cost = Double.parseDouble(txtCost.getText().trim());

            String addProduct = productService.update(collectData());

            if (addProduct.equals("Product updated")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("Product Updated successfully!");
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
                alert.setHeaderText(null);alert.setContentText("Product update Failed! Try again!");alert.showAndWait();

            }else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);alert.setContentText("Some thing Wrong Please check and try again!");alert.showAndWait();
            }

        }
    }
}
