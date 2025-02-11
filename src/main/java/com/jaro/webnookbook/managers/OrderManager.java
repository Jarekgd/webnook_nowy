package com.jaro.webnookbook.managers;

import com.jaro.webnookbook.models.CartItem;
import com.jaro.webnookbook.models.Order;
import com.jaro.webnookbook.models.OrderItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderManager {
    private static final String DB_URL = "jdbc:sqlite:C:\\webnookbook\\sqlite\\nookbook.db";
    
    public static int getUserId(String userLogin) {
    int userId = -1;
    String query = "SELECT userId FROM users WHERE login = ?";

    try (Connection conn = DriverManager.getConnection(DB_URL);
         PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setString(1, userLogin);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            userId = rs.getInt("userId");
        }
    } catch (SQLException e) {
        System.out.println("SQL Exception in getUserId: " + e.getMessage());
        e.printStackTrace();
    }
    return userId;
}

    private static void insertOrderItems(int orderId, ArrayList<CartItem> cartItems) {
    String orderItemQuery = "INSERT INTO order_items (orderId, serialNo, name, price, quantity) VALUES (?, ?, ?, ?, ?)";

    try (Connection conn = DriverManager.getConnection(DB_URL);
         PreparedStatement itemStmt = conn.prepareStatement(orderItemQuery)) {
        
        for (CartItem item : cartItems) {
            System.out.println("DEBUG: Adding item: " + item.getName() + " (Qty: " + item.getQuantity() + ")");
            itemStmt.setInt(1, orderId);
            itemStmt.setString(2, item.getSerialNo());
            itemStmt.setString(3, item.getName());
            itemStmt.setDouble(4, item.getPrice());
            itemStmt.setInt(5, item.getQuantity());
            itemStmt.addBatch();
        }
        itemStmt.executeBatch();
        System.out.println("DEBUG: Order items inserted successfully.");
    } catch (SQLException e) {
        System.out.println("SQL Exception in insertOrderItems: " + e.getMessage());
        e.printStackTrace();
    }
}


    public static int createOrder(String userLogin, double totalAmount, ArrayList<CartItem> cartItems) {
    int orderId = -1;
    int userId = getUserId(userLogin);  // Get userId from username

    if (userId == -1) {
        System.out.println("DEBUG: User ID not found for login: " + userLogin);
        return -1;
    }

    String orderQuery = "INSERT INTO orders (userId, totalPrice) VALUES (?, ?)";

    try (Connection conn = DriverManager.getConnection(DB_URL);
         PreparedStatement orderStmt = conn.prepareStatement(orderQuery, Statement.RETURN_GENERATED_KEYS)) {

        System.out.println("DEBUG: Inserting order for userId: " + userId);
        System.out.println("DEBUG: Total amount: " + totalAmount);

        orderStmt.setInt(1, userId);  // Corrected: Use userId instead of userLogin
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
        insertOrderItems(orderId, cartItems);

    } catch (SQLException e) {
        System.out.println("SQL Exception: " + e.getMessage());
        e.printStackTrace();
        return -1;
    }
    return orderId;
}




    public static void updateOrderStatus(int orderId, String completed) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    // Get all orders for a specific user
    public static List<Order> getUserOrders(String userLogin) {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM orders WHERE userLogin = ? ORDER BY orderDate DESC";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, userLogin);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                orders.add(new Order(
                    rs.getInt("orderId"),
                    rs.getString("userLogin"),
                    rs.getDouble("totalAmount"),
                    rs.getString("orderDate"),
                    rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    // Get full details of a selected order
    public static List<OrderItem> getOrderItems(int orderId) {
    List<OrderItem> items = new ArrayList<>();
    String sql = "SELECT itemId, orderId, serialNo, name, price, quantity FROM order_items WHERE orderId = ?";

    try (Connection conn = DriverManager.getConnection(DB_URL);
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setInt(1, orderId);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            OrderItem item = new OrderItem(
                rs.getInt ("itemId"),
                rs.getInt ("orderIdId"),    
                rs.getString("serialNo"), 
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
    
    public List<Order> getAllOrders() {
    List<Order> orders = new ArrayList<>();
    String query = "SELECT * FROM orders ORDER BY orderDate DESC";

    try (Connection conn = DatabaseManager.getConnection(DB_URL);
         PreparedStatement stmt = conn.prepareStatement(query);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            orders.add(new Order(
                rs.getInt("orderId"),
                rs.getString("userLogin"),
                rs.getDouble("totalAmount"),
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
    String query = "SELECT * FROM orders WHERE orderId = ?";

    try (Connection conn = DriverManager.getConnection(DB_URL);
         PreparedStatement stmt = conn.prepareStatement(query)) {

        stmt.setInt(1, orderId);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            order = new Order(
                rs.getInt("orderId"),
                rs.getString("userId"),  // Ensure this matches your schema
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

    
    

}
