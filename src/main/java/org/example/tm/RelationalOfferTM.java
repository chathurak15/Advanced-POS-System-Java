package org.example.tm;

import javafx.scene.control.Button;
import lombok.Data;

@Data
public class RelationalOfferTM {
    private int offerId;
    private String firstProduct;
    private String freeProduct;
    private String status;
    private Button delete;
}
