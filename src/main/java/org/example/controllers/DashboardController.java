package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import org.example.dto.ProductDTO;
import org.example.service.custom.OrderService;
import org.example.service.custom.impl.ProductServiceIMPL;
import org.example.tm.ProductTM;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class DashboardController {

    public AnchorPane submainPane;
    public Text txtTodaySale;
    public Text txtlast30;
    public Text txtlast2month;
    public AnchorPane tblPane;
    public TableView<ProductTM> tblExpProduct;
    public TableColumn<ProductTM,Integer>colId;
    public TableColumn<ProductTM,String>colName;
    public TableColumn<ProductTM,String>colCategory;
    public TableColumn<ProductTM,String>colQty;
    public TableColumn <ProductTM,String>colPrice;
    public TableColumn<ProductTM,String>colExpDate;
    public TableColumn<ProductTM, Button>colAction;
    private ObservableList<ProductTM> masterData;

    private final ProductServiceIMPL productService = new ProductServiceIMPL();
    private final OrderService orderService = new OrderService();

    public void initialize() {
        visualizeTable();
        loadTableData(date());
        getSales();
    }

    public void getSales(){
        String todaysales = orderService.getSales(date());
        String last30daysales = orderService.getlast30Sales(last30days());
        String last60daysales = orderService.getlast60Sales(last60days());
        txtTodaySale.setText(todaysales);
        txtlast30.setText(last30daysales);
        txtlast2month.setText(last60daysales);

    }

    public void visualizeTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colExpDate.setCellValueFactory(new PropertyValueFactory<>("expdate"));
    }

    public void loadTableData(String date){
        try {
            List<ProductTM> list = new ArrayList<>();
            List<ProductDTO> expired = productService.getExpired(date);
            for (ProductDTO productDTO : expired) {
                ProductTM product = convertDTOtoProductTM(productDTO);
                list.add(product);
            }

            masterData = FXCollections.observableArrayList(list);
            tblExpProduct.setItems(masterData);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public String date(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }
    public String last30days(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now().minusDays(30);
        return dtf.format(now);
    }
    public String last60days(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now().minusDays(60);
        return dtf.format(now);
    }


    public ProductTM convertDTOtoProductTM(ProductDTO productDTO) {
        ProductTM productTM = new ProductTM();
        productTM.setId(productDTO.getId());
        productTM.setName(productDTO.getName());
        productTM.setCategory(productDTO.getCategory());
        productTM.setQuantity(productDTO.getQuantity());
        productTM.setPrice(productDTO.getPrice());
        productTM.setExpdate("Expired");

        return productTM;
    }
}
