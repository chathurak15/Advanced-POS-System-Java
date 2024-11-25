package org.example.entity;

public class LowStock {
    private int productId;
    private String productName;
    private String SupplierName;
    private String SupplierEmail;
    private int quantity;

    public LowStock() {
    }

    public LowStock(int productId, String productName, String supplierName, String supplierEmail, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.SupplierName = supplierName;
        this.SupplierEmail = supplierEmail;
        this.quantity = quantity;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSupplierName() {
        return SupplierName;
    }

    public void setSupplierName(String supplierName) {
        this.SupplierName = supplierName;
    }

    public String getSupplierEmail() {
        return SupplierEmail;
    }

    public void setSupplierEmail(String supplierEmail) {
        this.SupplierEmail = supplierEmail;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}