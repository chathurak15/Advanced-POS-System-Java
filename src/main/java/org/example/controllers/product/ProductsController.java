package org.example.controllers.product;

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
import org.example.controllers.user.EditUserController;
import org.example.dto.ProductDTO;
import org.example.service.custom.impl.ProductServiceIMPL;
import org.example.tm.ProductTM;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductsController {
    public TableColumn<ProductTM,Integer> colId;
    public TableView<ProductTM> tblProducts;
    public TableColumn<ProductTM,String> colName;
    public TableColumn<ProductTM,String> colCategory;
    public TableColumn<ProductTM,Integer> colQty;
    public TableColumn<ProductTM,Double> colPrice;
    public TableColumn<ProductTM,Double> colcost;
    public TableColumn<ProductTM,String> colExpdate;
    public TableColumn<ProductTM,Button> colAction;
    public Button btnAddProduct;
    public AnchorPane submainPane;


    private ProductServiceIMPL productService = new ProductServiceIMPL();

   

    public void initialize() {
        loadTableData();
        visulizeTable();
    }

    private void visulizeTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colcost.setCellValueFactory(new PropertyValueFactory<>("cost"));
        colExpdate.setCellValueFactory(new PropertyValueFactory<>("expdate"));

        // Display both Delete and Edit buttons in the Action column
        colAction.setCellFactory(param -> new TableCell<>() {
            @Override
            protected void updateItem(Button button, boolean empty) {
                super.updateItem(button, empty);
                if (empty || getTableRow() == null) {
                    setGraphic(null);
                } else {
                    ProductTM productTM = getTableRow().getItem();
                    if (productTM != null) {
                        HBox actionButtons = new HBox(10, productTM.getButton(), productTM.getButtonEdit());
                        setGraphic(actionButtons);
                    }
                }
            }
        });
    }

    //delete product using product id. display conformation alert and after delete product form database.(userService)
    private void deleteProduct(ProductTM productTM) {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete this product?", ButtonType.YES, ButtonType.NO);
        confirmationAlert.setHeaderText(null);
        Optional<ButtonType> result = confirmationAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                boolean delete = productService.delete(productTM.getId());
                if (delete){
                    Alert infoAlert = new Alert(Alert.AlertType.INFORMATION,"product deleted successfully!");
                    infoAlert.setHeaderText(null);infoAlert.showAndWait();
                    loadTableData();
                }else {
                    Alert warnAlert = new Alert(Alert.AlertType.WARNING, "Error deleting product! Try again");
                    warnAlert.setHeaderText(null);warnAlert.showAndWait();
                }
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Error deleting product. Please contact Developer.").show();
            }
        }
    }

//edit product method- load editProduct.fxml and call editproductcontroller setUserData method using userTM param....
    private void editproduct(ProductTM productTM) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/admin/product/EditProduct.fxml"));
            Parent root = loader.load();

            EditProductController editProductController = loader.getController();
            editProductController.setProductData(productTM);

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

    public void AddProductOnAction(ActionEvent actionEvent) {
        try {
            Parent load = FXMLLoader.load(getClass().getResource("/view/admin/product/AddProduct.fxml"));
            submainPane.getChildren().add(load);

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "UI- Load error || please Contact Developer||").show();
            e.printStackTrace();
        }
    }

    public void loadTableData() {
        try {
            List<ProductTM> list = new ArrayList<>();
            List<ProductDTO> allProducts = productService.getAll();
            for (ProductDTO productDTO : allProducts) {
                ProductTM productTM = convertDTOToTM (productDTO);
                list.add(productTM);
            }

            ObservableList<ProductTM> productTMS = FXCollections.observableArrayList(list);
            tblProducts.setItems(productTMS);

        }catch (Exception e) {
            e.printStackTrace();
        }


    }

    //convert userDTO To UserTM and set values
    private ProductTM convertDTOToTM(ProductDTO productDTO) {
        ProductTM productTM = new ProductTM();
        productTM.setId(productDTO.getId());
        productTM.setName(productDTO.getName());
        productTM.setCategory(productDTO.getCategory());
        productTM.setQuantity(productDTO.getQuantity());
        productTM.setPrice(productDTO.getPrice());
        productTM.setCost(productDTO.getCost());
        productTM.setExpdate(productDTO.getExpirydate());


        // Set up Delete button
        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(event -> deleteProduct(productTM));
        productTM.setButton(deleteButton);

        // Set up Edit button
        Button editButton = new Button("Edit");
        editButton.setOnAction(event -> editproduct(productTM));
        productTM.setButtonEdit(editButton);

        return productTM;
    }
}
