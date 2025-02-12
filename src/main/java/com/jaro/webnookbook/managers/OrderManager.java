package com.jaro.webnookbook.managers;

import com.jaro.webnookbook.models.CartItem;
import com.jaro.webnookbook.models.Order;
import com.jaro.webnookbook.models.OrderItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderManager {

    private static final String DB_URL = "jdbc:sqlite:C:\\webnookbook\\sqlite\\nookbook.db";

    public static int createOrder(int userId, double totalAmount, ArrayList<CartItem> cartItems) {
        int orderId = -1;
        String orderQuery = "INSERT INTO orders (userId, totalPrice) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL); PreparedStatement orderStmt = conn.prepareStatement(orderQuery, Statement.RETURN_GENERATED_KEYS)) {

            System.out.println("DEBUG: Inserting order for user ID: " + userId);
            System.out.println("DEBUG: Total amount: " + totalAmount);

            orderStmt.setInt(1, userId);
            orderStmt.setDouble(2, totalAmount);
            int affectedRows = orderStmt.executeUpdate();

            if (affectedRows == 0) {
                System.out.println("DEBUG: Order insertion failed - No rows affected.");
                return -1;
            }

            try (ResultSet generatedKeys = orderStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    orderId = generatedKeys.getInt(1);
                    System.out.println("DEBUG: Order ID generated: " + orderId);
                } else {
                    System.out.println("DEBUG: Failed to retrieve generated order ID.");
                    return -1;
                }
            }

            // Insert order items
            String orderItemQuery = "INSERT INTO OrderItems (orderId, productSerial, quantity, price) VALUES (?, ?, ?, ?)";
            try (PreparedStatement itemStmt = conn.prepareStatement(orderItemQuery)) {
                for (CartItem item : cartItems) {
                    System.out.println("DEBUG: Adding item: " + item.getSerialNo() + " (Qty: " + item.getQuantity() + ")");
                    itemStmt.setInt(1, orderId);
                    itemStmt.setString(2, item.getSerialNo()); // Correct column
                    itemStmt.setInt(3, item.getQuantity());
                    itemStmt.setDouble(4, item.getPrice());
                    itemStmt.addBatch();
                }
                itemStmt.executeBatch();
                System.out.println("DEBUG: Order items inserted successfully.");
            }

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
        return orderId;
    }

    public static boolean updateOrderStatus(int orderId, String status) {
        String query = "UPDATE orders SET status = ? WHERE orderId = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, status);
            stmt.setInt(2, orderId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<Order> getUserOrders(String userLogin) {
    List<Order> orders = new ArrayList<>();
    String query = "SELECT * FROM Orders WHERE userId = (SELECT userId FROM users WHERE login = ?) ORDER BY orderDate DESC";

    try (Connection conn = DriverManager.getConnection(DB_URL);
         PreparedStatement stmt = conn.prepareStatement(query)) {

        stmt.setString(1, userLogin);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Order order = new Order(
                rs.getInt("orderId"),
                userLogin, // Use the login instead of userId
                rs.getDouble("totalPrice"),
                rs.getString("orderDate"),
                rs.getString("status")
            );

            // Fetch order items
            List<OrderItem> items = getOrderItems(order.getOrderId());
            order.setItems(items);

            orders.add(order);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return orders;
}


    public static List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM orders ORDER BY orderDate DESC";
        try (Connection conn = DriverManager.getConnection(DB_URL); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                orders.add(new Order(
                        rs.getInt("orderId"),
                        rs.getString("userId"),
                        rs.getDouble("totalPrice"),
                        rs.getString("orderDate"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public static Order getOrderDetails(int orderId) {
    Order order = null;
    String query = "SELECT * FROM Orders WHERE orderId = ?";

    try (Connection conn = DriverManager.getConnection(DB_URL);
         PreparedStatement stmt = conn.prepareStatement(query)) {

        stmt.setInt(1, orderId);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            order = new Order(
                rs.getInt("orderId"),
                rs.getString("userId"), 
                rs.getDouble("totalPrice"),
                rs.getString("orderDate"),
                rs.getString("status")
            );
        }

        if (order != null) {
            List<OrderItem> items = getOrderItems(orderId);
            order.setItems(items);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
    return order;
}



    public static List<OrderItem> getOrderItems(int orderId) {
    List<OrderItem> items = new ArrayList<>();
    String sql = "SELECT itemId, orderId, productSerial, name, price, quantity FROM OrderItems WHERE orderId = ?";

    try (Connection conn = DriverManager.getConnection(DB_URL);
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setInt(1, orderId);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            OrderItem item = new OrderItem(
                rs.getInt("itemId"),
                rs.getInt("orderId"),
                rs.getString("productSerial"),
                rs.getString("name"),
                rs.getDouble("price"),
                rs.getInt("quantity")
            );
            items.add(item);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return items;
}



}
