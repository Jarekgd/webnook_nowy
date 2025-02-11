package com.jaro.webnookbook.servlets;

import java.io.IOException;
import java.util.ArrayList;
import com.jaro.webnookbook.models.CartItem;
import com.jaro.webnookbook.managers.CartManager;
import com.jaro.webnookbook.managers.OrderManager;
import com.jaro.webnookbook.managers.UserManager;
import com.jaro.webnookbook.managers.BookManager;
import com.jaro.webnookbook.managers.AccessoryManager;
import com.jaro.webnookbook.managers.CustomerManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/CheckoutServlet")
public class CheckoutServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String userLogin = (String) session.getAttribute("userLogin");

        if (userLogin == null) {
            response.sendRedirect("login.jsp?error=Please log in first");
            return;
        }

        // Fetch cart items
        ArrayList<CartItem> cartItems = CartManager.getCart(userLogin);
        if (cartItems == null || cartItems.isEmpty()) {
            response.sendRedirect("customerCart.jsp?error=Your cart is empty");
            return;
        }

        // Calculate total amount
        double totalAmount = 0.0;
        for (CartItem item : cartItems) {
            totalAmount += item.getPrice() * item.getQuantity();
        }

        System.out.println("DEBUG: Attempting to create order for user: " + userLogin);
        System.out.println("DEBUG: Total Order Amount: " + totalAmount);

        try {
            // Attempt to create an order
            int orderId = OrderManager.createOrder(userLogin, totalAmount, cartItems);

            if (orderId > 0) {
                System.out.println("DEBUG: Order created successfully with ID: " + orderId);

                // Clear cart after checkout
                CartManager.clearCart(userLogin);
                response.sendRedirect("customerOrders.jsp?orderId=" + orderId + "&success=Order placed successfully");

            } else {
                System.out.println("DEBUG: Order creation failed!");
                response.sendRedirect("customerCart.jsp?error=Failed to place order");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("DEBUG: Exception occurred: " + e.getMessage());
            response.sendRedirect("customerCart.jsp?error=An unexpected error occurred");
        }
    }
}
