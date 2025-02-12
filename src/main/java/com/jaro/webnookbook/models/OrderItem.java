package com.jaro.webnookbook.models;

public class OrderItem {
    private int itemId;
    private int orderId;
    private String productSerial;  // Correct field name
    private String name;
    private double price;
    private int quantity;

    public OrderItem(int itemId, int orderId, String productSerial, String name, double price, int quantity) {
        this.itemId = itemId;
        this.orderId = orderId;
        this.productSerial = productSerial;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    // Correct getter method
    public String getProductSerial() { return productSerial; } 

    public int getItemId() { return itemId; }
    public int getOrderId() { return orderId; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
}
