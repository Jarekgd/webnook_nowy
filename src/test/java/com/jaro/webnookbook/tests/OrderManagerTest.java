package com.jaro.webnookbook.tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.jaro.webnookbook.managers.OrderManager;
import com.jaro.webnookbook.managers.UserManager;
import java.util.List;
import com.jaro.webnookbook.models.Order;
import java.util.ArrayList;

public class OrderManagerTest {

    @Test
    void testCreateOrder() {
        int userId = UserManager.getUserId("testuser"); // Retrieve the user ID
        int orderId = OrderManager.createOrder(userId, 29.99, new ArrayList<>());

        assertTrue(orderId > 0, "Order should be created successfully.");

    }

    @Test
    void testGetUserOrders() {
        List<Order> orders = OrderManager.getUserOrders("testuser");
        assertFalse(orders.isEmpty(), "User should have past orders");
    }

    @Test
    void testUpdateOrderStatus() {
        int orderId = 1; // Assume an order exists
        boolean updated = OrderManager.updateOrderStatus(orderId, "Completed");
        assertTrue(updated, "Order status should be updated");
    }
}
