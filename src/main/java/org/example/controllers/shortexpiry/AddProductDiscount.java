package org.example.controllers.shortexpiry;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.example.service.custom.impl.ProductServiceIMPL;
import org.example.tm.ProductTM;

import java.util.Optional;

public class AddProductDiscount {

    public AnchorPane addDiscountPane;
    public TextField txtDiscount;
    private Integer productid;

    private final ProductServiceIMPL productService = new ProductServiceIMPL();

    public void closeOnClick(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit?", ButtonType.YES, ButtonType.NO);
        alert.setHeaderText(null);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.YES) {

            try {
                addDiscountPane.getChildren().clear();
                Parent product = FXMLLoader.load(getClass().getResource("/view/admin/shortexpiry/ShortExpiry.fxml"));
                addDiscountPane.getChildren().add(product);

            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "UI- Load error || please Contact Developer||").show();
                e.printStackTrace();
            }
        }
    }

    public void AddOnClick(ActionEvent actionEvent) {
        if (txtDiscount.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);alert.setContentText("Please fill Discount field!");alert.showAndWait();
        }else if(!txtDiscount.getText().matches("^[0-9]*$")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);alert.setContentText("Please enter a valid number!");alert.showAndWait();
        }else{
            boolean rs = productService.AddDiscount(txtDiscount.getText(),productid);
            if (rs) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("Discount Added!");
                alert.showAndWait();

                try {
                    addDiscountPane.getChildren().clear();
                    Parent product = FXMLLoader.load(getClass().getResource("/view/admin/shortexpiry/ShortExpiry.fxml"));
                    addDiscountPane.getChildren().add(product);
                }catch (Exception e) {
                    new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("Discount Add Error!");
                }
            }

        }
    }

    public void collectData(Integer id){
        productid = id;
    }
}
