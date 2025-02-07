package com.jaro.webnookbook.servlets;

import com.jaro.webnookbook.models.*;
import com.jaro.webnookbook.managers.DatabaseConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Integer userId = (Integer) session.getAttribute("userId"); // ✅ Get userId from session
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

        if (user == null || userId == null || cart == null || cart.isEmpty()) {
            response.sendRedirect("cart.jsp?error=No items in cart");
            return;
        }

        double totalPrice = 0;
        for (CartItem item : cart) {
            totalPrice += item.getPrice() * item.getQuantity();
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false); // ✅ Enable transaction management

            // Insert new order into Orders table
            String orderSql = "INSERT INTO Orders (userId, orderDate, totalPrice, status) VALUES (?, datetime('now'), ?, 'Completed')";
            int orderId = -1;
            try (PreparedStatement orderStmt = conn.prepareStatement(orderSql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                orderStmt.setInt(1, userId); // ✅ Use userId from session
                orderStmt.setDouble(2, totalPrice);
                orderStmt.executeUpdate();

                ResultSet rs = orderStmt.getGeneratedKeys();
                if (rs.next()) {
                    orderId = rs.getInt(1);
                }
            }

            if (orderId == -1) {
                conn.rollback();
                response.sendRedirect("cart.jsp?error=Order creation failed");
                return;
            }

            //  Insert items into OrderItems table
            String orderItemSql = "INSERT INTO OrderItems (orderId, productSerial, quantity, price) VALUES (?, ?, ?, ?)";
            try (PreparedStatement orderItemStmt = conn.prepareStatement(orderItemSql)) {
                for (CartItem item : cart) {
                    orderItemStmt.setInt(1, orderId);
                    orderItemStmt.setString(2, item.getSerialNo());
                    orderItemStmt.setInt(3, item.getQuantity());
                    orderItemStmt.setDouble(4, item.getPrice());
                    orderItemStmt.executeUpdate();
                }
            }

            //  Reduce stock quantity for Books and Accessories
            for (CartItem item : cart) {
                String productSerial = item.getSerialNo();

                // Check if the item is a Book
                String checkBookSql = "SELECT COUNT(*) FROM books WHERE serialNo = ?";
                try (PreparedStatement checkBookStmt = conn.prepareStatement(checkBookSql)) {
                    checkBookStmt.setString(1, productSerial);
                    ResultSet bookCheckRs = checkBookStmt.executeQuery();

                    if (bookCheckRs.next() && bookCheckRs.getInt(1) > 0) {
                        // Reduce stock in books table
                        String updateBookStockSql = "UPDATE books SET quantity = quantity - ? WHERE serialNo = ?";
                        try (PreparedStatement updateBookStockStmt = conn.prepareStatement(updateBookStockSql)) {
                            updateBookStockStmt.setInt(1, item.getQuantity());
                            updateBookStockStmt.setString(2, item.getSerialNo());
                            updateBookStockStmt.executeUpdate();
                        }
                    } else {
                        // Reduce stock in accessories table
                        String updateAccessoryStockSql = "UPDATE accessories SET quantity = quantity - ? WHERE serialNo = ?";
                        try (PreparedStatement updateAccessoryStockStmt = conn.prepareStatement(updateAccessoryStockSql)) {
                            updateAccessoryStockStmt.setInt(1, item.getQuantity());
                            updateAccessoryStockStmt.setString(2, item.getSerialNo());
                            updateAccessoryStockStmt.executeUpdate();
                        }
                    }
                }
            }

            conn.commit(); 
            session.removeAttribute("cart"); 

            response.sendRedirect("orderConfirmation.jsp?orderId=" + orderId);
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("cart.jsp?error=Checkout failed");
        }
    }
}

