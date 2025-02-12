package com.jaro.webnookbook.models;

public class OrderItem {

    private int itemId;
    private int orderId;
    private String serialNo;
    private String name;
    private double price;
    private int quantity;

    public OrderItem(int itemId, int orderId, String serialNo, String name, double price, int quantity) {
        this.itemId = itemId;
        this.orderId = orderId;
        this.serialNo = serialNo;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    // Getters
    public int getItemId() {
        return itemId;
    }

    public int getOrderId() {
        return orderId;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
}
