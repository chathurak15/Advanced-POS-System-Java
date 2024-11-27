package org.example.tm;

import javafx.scene.control.Button;
import lombok.Data;

@Data
public class RelationProductTM {
    private String firstProduct;
    private String secondProduct;
    private Button addOffer;

    public RelationProductTM() {
    }

    public RelationProductTM(String firstProduct, String secondProduct, Button addOffer) {
        this.firstProduct = firstProduct;
        this.secondProduct = secondProduct;
        this.addOffer = addOffer;
    }

    public String getFirstProduct() {
        return firstProduct;
    }

    public void setFirstProduct(String firstProduct) {
        this.firstProduct = firstProduct;
    }

    public String getSecondProduct() {
        return secondProduct;
    }

    public void setSecondProduct(String secondProduct) {
        this.secondProduct = secondProduct;
    }

    public Button getAddOffer() {
        return addOffer;
    }

    public void setAddOffer(Button addOffer) {
        this.addOffer = addOffer;
    }
}
