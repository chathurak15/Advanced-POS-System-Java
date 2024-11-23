package org.example.entity;

public class Product {
    private int id;
    private String name;
    private String category;
    private double price;
    private double cost;
    private int quantity;
    private int supplierid;
    private String expirydate;
    private String date;

    public Product() {
    }

    public Product(int id, String name, String category, double price, double cost, int quantity, int supplierid, String expirydate, String date) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.cost = cost;
        this.quantity = quantity;
        this.supplierid = supplierid;
        this.expirydate = expirydate;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getSupplierid() {
        return supplierid;
    }

    public void setSupplierid(int supplierid) {
        this.supplierid = supplierid;
    }

    public String getExpirydate() {
        return expirydate;
    }

    public void setExpirydate(String expirydate) {
        this.expirydate = expirydate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
