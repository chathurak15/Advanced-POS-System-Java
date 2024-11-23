package org.example.tm;

import javafx.scene.control.Button;
import lombok.Data;

@Data

public class ProductTM {
    private int Id;
    private String name;
    private String category;
    private int quantity;
    private double price;
    private Button button;
    private Button buttonEdit;

}
