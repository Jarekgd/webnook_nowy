package com.jaro.webnookbook.tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.jaro.webnookbook.managers.CartManager;
import com.jaro.webnookbook.models.CartItem;
import java.util.ArrayList;
import java.util.List;

public class CartManagerTest {

    @Test
    void testAddToCart() {
        CartManager.addToCart("testuser", "B001", "Java Book", 15.99, 1);

// Verify that the item exists in the cart
        ArrayList<CartItem> cartItems = CartManager.getCart("testuser");
        boolean itemExists = cartItems.stream()
                .anyMatch(item -> item.getSerialNo().equals("B001") && item.getName().equals("Java Book"));

        assertTrue(itemExists, "Item should be present in the cart after adding.");

    }

    @Test
    void testGetCart() {
        List<CartItem> cart = CartManager.getCart("testuser");
        assertFalse(cart.isEmpty(), "Cart should contain items");
    }

    @Test
    void testClearCart() {
        CartManager.clearCart("testuser");
        List<CartItem> cart = CartManager.getCart("testuser");
        assertTrue(cart.isEmpty(), "Cart should be empty after clearing");
    }
}
