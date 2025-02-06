package com.jaro.webnookbook.servlets;

import com.jaro.webnookbook.managers.*;
import com.jaro.webnookbook.models.CartItem;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/CheckoutServlet")
public class CheckoutServlet extends HttpServlet {
    
    @Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    
    HttpSession session = request.getSession();
    String userLogin = (String) session.getAttribute("userLogin");

    if (userLogin == null) {
        System.out.println("DEBUG: User is not logged in.");
        response.sendRedirect("login.jsp?error=Please log in to proceed with checkout");
        return;
    }

    ArrayList<CartItem> cartItems = CartManager.getCart(userLogin);

    if (cartItems.isEmpty()) {
        System.out.println("DEBUG: Cart is empty for user: " + userLogin);
        response.sendRedirect("customerCart.jsp?error=Your cart is empty");
        return;
    }

    double totalAmount = 0.0;
    for (CartItem item : cartItems) {
        totalAmount += item.getPrice() * item.getQuantity();
    }

    double userBalance = CustomerManager.getBalance(userLogin);
    System.out.println("DEBUG: User balance: " + userBalance + ", Total amount: " + totalAmount);

    if (userBalance < totalAmount) {
        System.out.println("DEBUG: Insufficient balance for user: " + userLogin);
        response.sendRedirect("customerCart.jsp?error=Insufficient balance");
        return;
    }

    int orderId = OrderManager.createOrder(userLogin, totalAmount, cartItems);
    System.out.println("DEBUG: Created order ID: " + orderId);

    if (orderId > 0) {
        for (CartItem item : cartItems) {
            if (BookManager.getBookBySerialNo(item.getSerialNo()) != null) {
                BookManager.updateBookQuantity(item.getSerialNo(), item.getQuantity());
            } else if (AccessoryManager.getAccessoryBySerialNo(item.getSerialNo()) != null) {
                AccessoryManager.updateAccessoryQuantity(item.getSerialNo(), item.getQuantity());
            }
        }
        CustomerManager.updateBalance(userLogin, userBalance - totalAmount);
        OrderManager.updateOrderStatus(orderId, "Completed");
        CartManager.clearCart(userLogin);
        response.sendRedirect("customerOrders.jsp?success=Order placed successfully");
    } else {
        System.out.println("DEBUG: Failed to create order for user: " + userLogin);
        response.sendRedirect("customerCart.jsp?error=Failed to place order");
    }
}

}
