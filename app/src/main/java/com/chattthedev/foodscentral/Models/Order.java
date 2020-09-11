package com.chattthedev.foodscentral.Models;

public class Order {
    private int ID;
    private String ProductId;
    private String ProductName;
    private String Quantity;
    private String Price;
    private String Halfprice;
    private String Fullprice;
    private String Half;
    private String Full;

    public Order() {
    }

    public Order(String productId, String productName, String quantity, String price) {
        ProductId = productId;
        ProductName = productName;
        Quantity = quantity;
        Price = price;
    }

    public Order(int ID, String productId, String productName, String quantity, String price) {
        this.ID = ID;
        ProductId = productId;
        ProductName = productName;
        Quantity = quantity;
        Price = price;
    }

    public Order(int ID, String productId, String productName, String quantity, String price, String halfPrice, String fullPrice, String half, String full) {
        this.ID = ID;
        ProductId = productId;
        ProductName = productName;
        Quantity = quantity;
        Price = price;
        Halfprice = halfPrice;
        Fullprice = fullPrice;
        Half = half;
        Full = full;
    }

    public Order(String productId, String productName, String quantity, String price, String halfprice, String fullprice, String half, String full) {
        ProductId = productId;
        ProductName = productName;
        Quantity = quantity;
        Price = price;
        Halfprice = halfprice;
        Fullprice = fullprice;
        Half = half;
        Full = full;
    }

    public String getHalf() {
        return Half;
    }

    public void setHalf(String half) {
        Half = half;
    }

    public String getFull() {
        return Full;
    }

    public void setFull(String full) {
        Full = full;
    }

    public String getHalfprice() {
        return Halfprice;
    }

    public void setHalfprice(String halfprice) {
        Halfprice = halfprice;
    }

    public String getFullprice() {
        return Fullprice;
    }

    public void setFullprice(String fullprice) {
        Fullprice = fullprice;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }
}
