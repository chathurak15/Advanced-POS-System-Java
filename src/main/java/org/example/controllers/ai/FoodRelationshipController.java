package org.example.controllers.ai;

import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import org.example.controllers.shortexpiry.AddProductDiscount;
import org.example.entity.AI.Relations;
import org.example.entity.AI.RelationsNormal;
import org.example.entity.Product;
import org.example.service.AIService;
import org.example.tm.ProductTM;
import org.example.tm.RelationProductTM;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FoodRelationshipController {
    public TextField txtSearch;
    public AnchorPane submainPane;
    public TableView<RelationProductTM> tblRelationProduct;
    public TableColumn<RelationProductTM,String> colFirstProduct;
    public TableColumn<RelationProductTM,String> colSecondProduct;
    public TableColumn<RelationProductTM, Button> colAction;

    private final AIService aiService = new AIService();
    private final Relations relations = new Relations();


    public void initialize() {
        visulizeTable();
        loadTable();
    }

    public void visulizeTable() {
        colFirstProduct.setCellValueFactory(new PropertyValueFactory<>("firstProduct"));
        colSecondProduct.setCellValueFactory(new PropertyValueFactory<>("secondProduct"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("addOffer"));
    }

    public void loadTable() {
        try {

            List<RelationsNormal> relationsNormalList = (List<RelationsNormal>) aiService.createDataset();

            List<RelationProductTM> tableData = new ArrayList<>();
            for (RelationsNormal relation : relationsNormalList) {
                List<Product> products = relation.getRelations();
                if (products.size() >= 2) { // Ensure at least two products for the relation
                    String firstProduct = formatProductDetails(products.get(0));
                    String secondProduct = formatProductDetails(products.get(1));

                    RelationProductTM relationProductTM = new RelationProductTM(); // Instantiate object

                    relationProductTM.setFirstProduct(firstProduct);
                    relationProductTM.setSecondProduct(secondProduct);

                    Button addOffer = new Button();
                    Image addOfferIcon = new Image("image/sale.png"); // Ensure the path is correct
                    ImageView deleteIconView = new ImageView(addOfferIcon);
                    deleteIconView.setFitWidth(20);  // Set icon size
                    deleteIconView.setFitHeight(20);
                    addOffer.setGraphic(deleteIconView);
                    addOffer.setOnAction(event -> addOffer(Collections.singletonList(relationProductTM)));
                    relationProductTM.setAddOffer(addOffer);

                    tableData.add(relationProductTM);
                }
            }

            tblRelationProduct.getItems().clear();
            tblRelationProduct.getItems().addAll(tableData);
        } catch (Exception e) {
            e.printStackTrace(); // Handle exceptions gracefully
        }
    }

    private void addOffer(List<RelationProductTM> relationProductTMList) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/admin/foodRelationships/AddOffer.fxml"));
            Parent root = loader.load();

            AddOffer addOffer = loader.getController();
            addOffer.cmbFree(relationProductTMList);

            submainPane.getChildren().add(root);

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "UI- Load error || please Contact Developer||").show();
            e.printStackTrace();
        }

    }

    private String formatProductDetails(Product product) {
        return product.getId() + " - " + product.getName() + " - " + product.getExpirydate();
    }

    public void SearchOnClick(KeyEvent keyEvent) {
        setupSearch();
    }

    private void setupSearch() {
        FilteredList<RelationProductTM> filteredData = new FilteredList<>(tblRelationProduct.getItems(), p -> true);

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(relation -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();
                return relation.getFirstProduct().toLowerCase().contains(lowerCaseFilter)
                        || relation.getSecondProduct().toLowerCase().contains(lowerCaseFilter);
            });
        });

        tblRelationProduct.setItems(filteredData);
    }

}
