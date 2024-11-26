package org.example.controllers.order;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DashboardController {

    public Text txtDate;
    public Text txtName;
    public ComboBox cmbProduct;
    public TextField txtQuantity;
    public TableView tblProducts;
    public TableColumn colId;
    public TableColumn colName;
    public TableColumn colQty;
    public TableColumn colPrice;
    public TableColumn colSellPrice;
    public TableColumn colTotal;
    public TableColumn colAction;
    public Label txtProducts;
    public Label txtTotalPrice;
    public Label txtDiscount;
    public Label txtSubtotal;
    public ComboBox cmbPaymentMethod;

    public void initialize() {
        cmbPaymentMethod.getItems().addAll("Cash", "Card");
        updateDateTime();
    }


    public void addOnClick(ActionEvent actionEvent) {
    }

    public void proceedOnClick(ActionEvent actionEvent) {
    }

    private void updateDateTime() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd   HH:mm");

        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(1), // Update every second
                        event -> {
                            txtDate.setText(LocalDateTime.now().format(formatter));
                        }
                )
        );
        timeline.setCycleCount(Timeline.INDEFINITE); // Run indefinitely
        timeline.play(); // Start the timeline
    }
}
