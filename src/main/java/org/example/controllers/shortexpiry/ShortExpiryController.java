package org.example.controllers.shortexpiry;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import org.example.controllers.product.EditProductController;
import org.example.dto.ProductDTO;
import org.example.service.custom.impl.ProductServiceIMPL;
import org.example.tm.ProductTM;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ShortExpiryController {
    public AnchorPane submainPane;
    public TableView<ProductTM> tblExpProduct;
    public TableColumn<ProductTM,Integer>colId;
    public TableColumn<ProductTM,String>colName;
    public TableColumn<ProductTM,String>colCategory;
    public TableColumn<ProductTM,String>colQty;
    public TableColumn <ProductTM,String>colPrice;
    public TableColumn<ProductTM,String>colExpDate;
    public TableColumn<ProductTM, Button>colAction;
    public ComboBox cmbExp;

    private final ProductServiceIMPL productService = new ProductServiceIMPL();


    public void initialize() {
        visualizeTable();
        loadTableData(date());
        cmbExp.getItems().addAll("Expired", "Within Week", "Within 2 Weeks", "Within Month");
    }

    public void visualizeTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colExpDate.setCellValueFactory(new PropertyValueFactory<>("expdate"));

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
    public void SearchOnClick(KeyEvent keyEvent) {
    }

    public void expOnClick(ActionEvent actionEvent) {

        switch (cmbExp.getSelectionModel().getSelectedItem().toString()){
            case "Expired":
                loadTableData(date());
                visualizeTable();
                break;
            case "Within Week":
                loadTableData(withinOneWeekDate());
                visualizeTable();
                break;
            case "Within 2 Weeks":
                 loadTableData(withinTwoWeekDate());
                 visualizeTable();
                 break;
            case "Within Month":
                 loadTableData(withinOneMonthDate());
                 visualizeTable();
                 break;
            default:
                  loadTableData(date());
                  visualizeTable();
                  break;
        }
    }

    public void loadTableData(String date){
        try {
            List<ProductTM> list = new ArrayList<>();
            List<ProductDTO> expired = productService.getExpired(date);
            for (ProductDTO productDTO : expired) {
                ProductTM product = convertDTOtoProductTM(productDTO);
                list.add(product);
            }

            ObservableList<ProductTM> productTMS = FXCollections.observableArrayList(list);
            tblExpProduct.setItems(productTMS);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public String date(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    public String withinOneWeekDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime oneWeekLater = LocalDateTime.now().plusDays(7); // Add 7 days to the current date
        return dtf.format(oneWeekLater);
    }
    public String withinTwoWeekDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime oneWeekLater = LocalDateTime.now().plusDays(14); // Add 7 days to the current date
        return dtf.format(oneWeekLater);
    }
    public String withinOneMonthDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime oneWeekLater = LocalDateTime.now().plusDays(30); // Add 7 days to the current date
        return dtf.format(oneWeekLater);
    }

    public ProductTM convertDTOtoProductTM(ProductDTO productDTO) {
        ProductTM productTM = new ProductTM();
        productTM.setId(productDTO.getId());
        productTM.setName(productDTO.getName());
        productTM.setCategory(productDTO.getCategory());
        productTM.setQuantity(productDTO.getQuantity());
        productTM.setPrice(productDTO.getPrice());
        productTM.setExpdate(productDTO.getExpirydate());

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(event -> deleteProduct(productTM));
        productTM.setButton(deleteButton);

        // Set up Edit button
        Button discountButton = new Button("Add Discount");
        discountButton.setOnAction(event -> AddDiscount(productTM));
        productTM.setButtonEdit(discountButton);

        return productTM;
    }

    private void AddDiscount(ProductTM productTM) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/admin/shortexpiry/AddProductDiscount.fxml"));
            Parent root = loader.load();

            AddProductDiscount addProductDiscount = loader.getController();
            addProductDiscount.collectData(productTM.getId());

            submainPane.getChildren().add(root);

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "UI- Load error || please Contact Developer||").show();
            e.printStackTrace();
        }
    }

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
                    loadTableData(date());
                }else {
                    Alert warnAlert = new Alert(Alert.AlertType.WARNING, "Error deleting product! Try again");
                    warnAlert.setHeaderText(null);warnAlert.showAndWait();
                }
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Error deleting product. Please contact Developer.").show();
            }
        }
    }
}
