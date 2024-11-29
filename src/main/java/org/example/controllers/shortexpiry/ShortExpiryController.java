package org.example.controllers.shortexpiry;

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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
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
    public TextField txtSearch;
    private ObservableList<ProductTM> masterData;

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

    public void expOnClick(ActionEvent actionEvent) {

        switch (cmbExp.getSelectionModel().getSelectedItem().toString()){
            case "Expired":
                loadTableData(date());
                visualizeTable();
                break;
            case "Within Week":
                loadExpirySoonTableData(withinOneWeekDate(),date());
                visualizeTable();
                break;
            case "Within 2 Weeks":
                loadExpirySoonTableData(withinTwoWeekDate(),date());
                 visualizeTable();
                 break;
            case "Within Month":
                loadExpirySoonTableData(withinOneMonthDate(),date());
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

            masterData = FXCollections.observableArrayList(list);
            tblExpProduct.setItems(masterData);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void loadExpirySoonTableData(String date,String today){
        try {
            List<ProductTM> list = new ArrayList<>();
            List<ProductDTO> expired = productService.getExpirySoon(date,today);
            for (ProductDTO productDTO : expired) {
                ProductTM product = convertDTOtoProductTM(productDTO);
                list.add(product);
            }

            masterData = FXCollections.observableArrayList(list);
            tblExpProduct.setItems(masterData);

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

        Button deleteButton = new Button();
        Image deleteIcon = new Image("image/delete.png"); // Provide the correct path to your delete icon
        ImageView deleteIconView = new ImageView(deleteIcon);
        deleteIconView.setFitWidth(20);  // Set icon size
        deleteIconView.setFitHeight(20);
        deleteButton.setGraphic(deleteIconView);
        deleteButton.setOnAction(event -> deleteProduct(productTM));
        productTM.setButton(deleteButton);

        // Set up Add Discount button with icon
        Button discountButton = new Button();
        Image discountIcon = new Image("image/discounts.png"); // Provide the correct path to your discount icon
        ImageView discountIconView = new ImageView(discountIcon);
        discountIconView.setFitWidth(20);  // Set icon size
        discountIconView.setFitHeight(20);
        discountButton.setGraphic(discountIconView);
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

    private void setupSearch() {
        FilteredList<ProductTM> filteredData = new FilteredList<>(masterData, p -> true);

        // Add a listener to the search field
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(product -> {
                // If search text is empty, display all rows
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();
                return product.getName().toLowerCase().contains(lowerCaseFilter);
            });
        });

        tblExpProduct.setItems(filteredData);
    }
}
