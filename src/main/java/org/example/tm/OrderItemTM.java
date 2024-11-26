package org.example.tm;

import javafx.scene.control.Button;
import lombok.Data;

@Data

public class OrderItemTM {
    private int orderItemId;
    private String itemName;
    private int quantity;
    private double price;
    private double sellPrice;
    private double totalPrice;
    private Button deleteButton;
}
