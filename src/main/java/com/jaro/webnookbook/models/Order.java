package com.jaro.webnookbook.models;

import java.util.List;

public class Order {

    private int orderId;
    private String userLogin;
    private double totalAmount;
    private String orderDate;
    private String status;
    private List<OrderItem> items; // List of items in the order

    public Order(int orderId, String userLogin, double totalAmount, String orderDate, String status) {
        this.orderId = orderId;
        this.userLogin = userLogin;
        this.totalAmount = totalAmount;
        this.orderDate = orderDate;
        this.status = status;
    }

    // Getters and Setters
    public int getOrderId() {
        return orderId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public String getStatus() {
        return status;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
}
