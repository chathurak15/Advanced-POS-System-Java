package org.example.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.util.List;

public class TemplateController {
    public Label lbName;
    public HBox lbDashboard;
    public HBox lbProducts;
    public Text txtDashboard;
    public Text txtProducts;
    public AnchorPane mainPane;
    public HBox lbUsers;
    public Button btnLogout;
    public HBox lbSuppliers;
    public HBox lbFoodRelationships;
    public HBox lbShortExpiry;
    public HBox lbDiscount;
    public Text txtDiscount;
    public Text txtShortExpiry;
    public Text txtFoodRelationships;
    public Text txtSuppliers;
    public Text txtUsers;
    public Text txtRole;
    private List<HBox> sidebarItems;
    private List<Text> sidebarTexts;


    public void initialize() {

        sidebarItems = List.of( lbDashboard, lbProducts, lbDiscount, lbShortExpiry, lbFoodRelationships, lbSuppliers, lbUsers );
        sidebarTexts = List.of( txtDashboard, txtProducts,txtDiscount,txtShortExpiry,txtFoodRelationships, txtSuppliers, txtUsers );
    }


    public void DashboardOnClick(MouseEvent mouseEvent) {
        setActiveTab(lbDashboard);
        setActiveText(txtDashboard);

        loadUI("admin/Dashboard");

//        Stage window = (Stage) mainPane.getScene().getWindow();
//        window.close();
//
//        Stage stage = new Stage();
//        try {
//            Parent load = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/Template.fxml")));
//            Scene scene = new Scene(load);
//            stage.setScene(scene);
//            stage.show();
//
//        } catch (IOException e) {
//            new Alert(Alert.AlertType.ERROR, e+"Faild load Login please contact Developer").show();
//        }

    }

    public void ProductsOnClick(MouseEvent mouseEvent) {
        setActiveTab(lbProducts);
        setActiveText(txtProducts);
        loadUI("admin/products");

    }

    public void DiscountOnclick(MouseEvent mouseEvent) {
        setActiveTab(lbDiscount);
        setActiveText(txtDiscount);
        loadUI("admin/Discount");
    }

    public void ShortExpiryOnClick(MouseEvent mouseEvent) {
        setActiveTab(lbShortExpiry);
        setActiveText(txtShortExpiry);
//        loadUI("admin/products");
    }

    public void FoodRelationshipsOnClick(MouseEvent mouseEvent) {
        setActiveTab(lbFoodRelationships);
        setActiveText(txtFoodRelationships);
//        loadUI("admin/products");
    }

    public void SuppliersOnClick(MouseEvent mouseEvent) {
        setActiveTab(lbSuppliers);
        setActiveText(txtSuppliers);
//        loadUI("admin/Suppliers");
    }

    public void UserOnClick(MouseEvent mouseEvent) {
        setActiveTab(lbUsers);
        setActiveText(txtUsers);
        loadUI("admin/user/Users");
    }

    //set and remove css class for active hbox. this class using for hbox color change...
    private void setActiveTab(HBox activeTab) {
        for (HBox item : sidebarItems) {
            item.getStyleClass().remove("sidebar-item-active");
        }
        activeTab.getStyleClass().add("sidebar-item-active");
    }

    //set and remove css class for active text(side menu tab). this class using for txt font color change....
    private void setActiveText(Text activeText) {
        for (Text item : sidebarTexts) {
            item.getStyleClass().remove("sidebar-item-active-test");
        }
        activeText.getStyleClass().add("sidebar-item-active-test");
    }

    //load ui replace mainPane (ui name pass for this method).......

    public void loadUI(String UIName){
        mainPane.getChildren().clear();

        try {
            Parent load = FXMLLoader.load(getClass().getResource("/view/"+UIName+".fxml"));
            mainPane.getChildren().add(load);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "UI- Load error || please Contact Developer||").show();
            e.printStackTrace();
        }

    }
}
