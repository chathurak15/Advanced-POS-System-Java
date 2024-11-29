package org.example.controllers.ai;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import org.example.dto.AI.RelationshipOfferDTO;
import org.example.entity.AI.RelationsNormal;
import org.example.entity.Product;
import org.example.service.AIService;
import org.example.service.custom.OfferService;
import org.example.tm.RelationProductTM;
import org.example.tm.RelationalOfferTM;

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
    private final OfferService offerService = new OfferService();
    private ObservableList<RelationalOfferTM> masterData;

    public TableView<RelationalOfferTM> tbloffers;
    public TableColumn<RelationalOfferTM,Integer> colId;
    public TableColumn<RelationalOfferTM,String> colMainProduct;
    public TableColumn<RelationalOfferTM,String> colFreeProduct;
    public TableColumn<RelationalOfferTM,String> colStatus;
    public TableColumn<RelationalOfferTM,Button> colAction1;


    public void initialize() {
        visulizeTable();
        visulizeOfferTable();
        loadTable();
        loadOfferTable();
    }

    public void visulizeTable() {
        colFirstProduct.setCellValueFactory(new PropertyValueFactory<>("firstProduct"));
        colSecondProduct.setCellValueFactory(new PropertyValueFactory<>("secondProduct"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("addOffer"));
    }

    public void visulizeOfferTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("offerId"));
        colMainProduct.setCellValueFactory(new PropertyValueFactory<>("firstProduct"));
        colFreeProduct.setCellValueFactory(new PropertyValueFactory<>("freeProduct"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colAction1.setCellValueFactory(new PropertyValueFactory<>("delete"));

    }

    //relationship offer table
    public void loadOfferTable() {
        try {
            List<RelationalOfferTM> list = new ArrayList<>();
            offerService.getAll();
            for (RelationshipOfferDTO relationshipOfferDTO : offerService.getAll()) {
                RelationalOfferTM relationalOfferTM = convertDTOToTM(relationshipOfferDTO);
                list.add(relationalOfferTM);
            }
//
            masterData = FXCollections.observableArrayList(list);
            tbloffers.setItems(masterData);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /// relationship table
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
        return product.getId() + " - " + product.getName()+" - "+ product.getPrice() + " - " + product.getExpirydate();
    }

    public void SearchOnClick(KeyEvent keyEvent) {
        setupSearch();
    }

    private void deleteOffer(RelationalOfferTM relationalOfferTM) {
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

    private RelationalOfferTM convertDTOToTM(RelationshipOfferDTO relationshipOfferDTO) {
        RelationalOfferTM relationalOfferTM = new RelationalOfferTM();

        relationalOfferTM.setOfferId(relationshipOfferDTO.getId());
        relationalOfferTM.setFirstProduct(relationshipOfferDTO.getMainProduct());
        relationalOfferTM.setFreeProduct(relationshipOfferDTO.getFreeProduct());
        relationalOfferTM.setStatus(relationshipOfferDTO.getStatus());

        Button deleteButton = new Button();
        Image deleteIcon = new Image("image/delete.png"); // Provide the correct path to your delete icon
        ImageView deleteIconView = new ImageView(deleteIcon);
        deleteIconView.setFitWidth(20);  // Set icon size
        deleteIconView.setFitHeight(20);
        deleteButton.setGraphic(deleteIconView);
        deleteButton.setOnAction(event -> deleteOffer(relationalOfferTM));
        relationalOfferTM.setDelete(deleteButton);

        return relationalOfferTM;

    }


}
