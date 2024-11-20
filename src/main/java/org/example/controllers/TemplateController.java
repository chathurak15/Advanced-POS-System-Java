package org.example.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class TemplateController {
    public Label lbName;
    public HBox lbDashboard;
    public HBox lbProducts;
    public Text txtDashboard;
    public Text txtProducts;
    public AnchorPane mainPane;
    private List<HBox> sidebarItems;
    private List<Text> sidebarTexts;

    public void initialize() {
        sidebarItems = List.of( lbDashboard, lbProducts);
        sidebarTexts = List.of( txtDashboard, txtProducts);
    }

    public void DashboardOnClick(MouseEvent mouseEvent) {
        setActiveTab(lbDashboard);
        setActiveText(txtDashboard);

        loadUI("Dashboard");

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
        loadUI("products");

    }

    private void setActiveTab(HBox activeTab) {
        for (HBox item : sidebarItems) {
            item.getStyleClass().remove("sidebar-item-active");
        }
        activeTab.getStyleClass().add("sidebar-item-active");
    }


    private void setActiveText(Text activeText) {
        for (Text item : sidebarTexts) {
            item.getStyleClass().remove("sidebar-item-active-test");
        }
        activeText.getStyleClass().add("sidebar-item-active-test");
    }

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
