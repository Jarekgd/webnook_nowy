package com.jaro.webnookbook.servlets;

import java.io.IOException;
import java.util.ArrayList;
import com.jaro.webnookbook.models.CartItem;
import com.jaro.webnookbook.managers.CartManager;
import com.jaro.webnookbook.managers.OrderManager;
import com.jaro.webnookbook.managers.CustomerManager;
import com.jaro.webnookbook.managers.BookManager;
import com.jaro.webnookbook.managers.AccessoryManager;
import com.jaro.webnookbook.managers.UserManager;
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

        // Get userId from UserManager
        int userId = UserManager.getUserId(userLogin);
        if (userId == -1) {
            response.sendRedirect("customerCart.jsp?error=User ID not found");
            return;
        }

        // Fetch user's balance
        double userBalance = CustomerManager.getBalance(userLogin);
        System.out.println("DEBUG: User Balance: $" + userBalance);

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

        System.out.println("DEBUG: Total Order Amount: $" + totalAmount);

        // Check if user has enough funds
        if (userBalance < totalAmount) {
            response.sendRedirect("customerCart.jsp?error=Insufficient funds");
            return;
        }

        try {
            // Create order and insert into `OrderItems`
            int orderId = OrderManager.createOrder(userId, totalAmount, cartItems);

            if (orderId > 0) {
                System.out.println("DEBUG: Order created successfully with ID: " + orderId);

                // Deduct balance
                boolean balanceUpdated = CustomerManager.updateBalance(userLogin, totalAmount);
                if (!balanceUpdated) {
                    response.sendRedirect("customerCart.jsp?error=Failed to update balance");
                    return;
                }

                // Update stock levels
                for (CartItem item : cartItems) {
                    boolean stockUpdated = false;

                    if (BookManager.isBook(item.getSerialNo())) {
                        stockUpdated = BookManager.updateStock(item.getSerialNo(), item.getQuantity());
                    } else if (AccessoryManager.isAccessory(item.getSerialNo())) {
                        stockUpdated = AccessoryManager.updateStock(item.getSerialNo(), item.getQuantity());
                    }

                    if (!stockUpdated) {
                        System.out.println("DEBUG: Failed to update stock for item: " + item.getSerialNo());
                        response.sendRedirect("customerCart.jsp?error=Not enough stock for " + item.getSerialNo());
                        return;
                    }
                }

                // Update order status to 'Completed'
                boolean statusUpdated = OrderManager.updateOrderStatus(orderId, "Completed");
                if (!statusUpdated) {
                    response.sendRedirect("customerOrders.jsp?error=Failed to update order status");
                    return;
                }

                // Clear cart after checkout
                CartManager.clearCart(userLogin);
                response.sendRedirect("customerOrders.jsp?orderId=" + orderId + "&success=Order placed successfully");

            } else {
                response.sendRedirect("customerCart.jsp?error=Failed to place order");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("customerCart.jsp?error=An unexpected error occurred");
        }
    }
}
