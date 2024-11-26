package org.example.entity;

public class OrderItem {
    private int productId;
    private int orderId;
    private int quantity;
    private double sellPrice;

    public OrderItem() {
    }

    public OrderItem(int productId, int orderId, int quantity, double sellPrice) {
        this.productId = productId;
        this.orderId = orderId;
        this.quantity = quantity;
        this.sellPrice = sellPrice;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(double sellPrice) {
        this.sellPrice = sellPrice;
    }
}
