package org.example.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class SMIndexController {
    public AnchorPane mainPane;
    public Label lbName;
    public HBox lbProducts;
    public Text txtProducts;
    public HBox lbShortExpiry;
    public Text txtShortExpiry;
    public HBox lbSuppliers;
    public Text txtSuppliers;
    public HBox lbLowStock;
    public Text txtLowStocks;
    public Label lbDateandTime;
    private List<HBox> sidebarItems;
    private List<Text> sidebarTexts;

    public void initialize() {
        sidebarItems = List.of(lbProducts, lbShortExpiry, lbLowStock,  lbSuppliers );
        sidebarTexts = List.of( txtProducts,txtShortExpiry,txtLowStocks, txtSuppliers);
        updateDateTime();
        loadUI("admin/product/products");
    }

    public void ProductsOnClick(MouseEvent mouseEvent) {
        setActiveTab(lbProducts);
        setActiveText(txtProducts);
        loadUI("admin/product/products");
    }

    public void ShortExpiryOnClick(MouseEvent mouseEvent) {
        setActiveTab(lbShortExpiry);
        setActiveText(txtShortExpiry);
        loadUI("admin/shortexpiry/ShortExpiry");
    }

    public void LowStocksOnAction(MouseEvent mouseEvent) {
        setActiveTab(lbLowStock);
        setActiveText(txtLowStocks);
        loadUI("stockmanager/LowStock");

    }

    public void SuppliersOnClick(MouseEvent mouseEvent) {
        setActiveTab(lbSuppliers);
        setActiveText(txtSuppliers);
        loadUI("stockmanager/suppliers/Suppliers");
    }

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


    public void LogoutOnAction(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to log out?", ButtonType.YES , ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.YES) {

            Stage window = (Stage) mainPane.getScene().getWindow();
            window.close();

            Stage stage = new Stage();

            try {
                Parent load = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/LoginForm.fxml")));
                Scene scene = new Scene(load);;
                stage.setScene(scene);
                stage.show();

            } catch (IOException e) {
                new Alert(Alert.AlertType.ERROR, e + "Faild load Login please contact Developer").show();
            }
        }
    }

    private void updateDateTime() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd   HH:mm");

        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(1), // Update every second
                        event -> {
                            lbDateandTime.setText(LocalDateTime.now().format(formatter));
                        }
                )
        );
        timeline.setCycleCount(Timeline.INDEFINITE); // Run indefinitely
        timeline.play(); // Start the timeline
    }

}
