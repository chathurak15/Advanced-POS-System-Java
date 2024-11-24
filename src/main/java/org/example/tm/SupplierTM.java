package org.example.tm;

import javafx.scene.control.Button;
import lombok.Data;

@Data

public class SupplierTM {
    private int id;
    private String name;
    private String email;
    private String number;
    private Button button;
    private Button buttonEdit;
}
