package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import org.example.dto.LowStockDTO;
import org.example.dto.ProductDTO;
import org.example.service.custom.impl.ProductServiceIMPL;
import org.example.tm.LowStockTM;
import org.example.tm.ProductTM;

import java.util.ArrayList;
import java.util.List;

public class LowStockController {
    public AnchorPane submainPane;
    public TableView<LowStockTM> tblLowStock;
    public TableColumn<LowStockTM, Integer> colId;
    public TableColumn<LowStockTM, String> colName;
    public TableColumn<LowStockTM,String> colSupplierName;
    public TableColumn<LowStockTM,String> colSupplierEmail;
    public TableColumn<LowStockTM,Integer> colQuantity;

    private final ProductServiceIMPL productService = new ProductServiceIMPL();


    public void initialize() {
        visulizeTable();
        loadTable();

    }

    public void visulizeTable(){
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colSupplierName.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        colSupplierEmail.setCellValueFactory(new PropertyValueFactory<>("supplierEmail"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    }

    public void loadTable() {
        try{
            List<LowStockTM> list = new ArrayList<>();
            List<LowStockDTO> all = productService.getLowStock();

            for (LowStockDTO lowStockDTO : all) {
                LowStockTM lowStockTM = convertDTOtoTM(lowStockDTO);
                list.add(lowStockTM);
            }

            ObservableList<LowStockTM> lowStockTMS = FXCollections.observableArrayList(list);
            tblLowStock.setItems(lowStockTMS);

        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    public LowStockTM convertDTOtoTM(LowStockDTO lowStockDTO) {
        LowStockTM lowStockTM = new LowStockTM();
        lowStockTM.setId(lowStockDTO.getProductId());
        lowStockTM.setName(lowStockDTO.getProductName());
        lowStockTM.setSupplierName(lowStockDTO.getSupplierName());
        lowStockTM.setSupplierEmail(lowStockDTO.getSupplierEmail());
        lowStockTM.setQuantity(lowStockDTO.getQuantity());
        return lowStockTM;
    }
}
