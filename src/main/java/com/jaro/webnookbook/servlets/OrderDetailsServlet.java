package com.jaro.webnookbook.servlets;

import com.jaro.webnookbook.managers.OrderManager;
import com.jaro.webnookbook.models.Order;
import com.jaro.webnookbook.models.OrderItem;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/OrderDetailsServlet")
public class OrderDetailsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String orderIdStr = request.getParameter("orderId");
        
        if (orderIdStr == null || orderIdStr.isEmpty()) {
            response.sendRedirect("orderHistory.jsp?error=Invalid order selection");
            return;
        }
        
        try {
            int orderId = Integer.parseInt(orderIdStr);
            Order order = OrderManager.getOrderDetails(orderId);

            if (order != null) {
                List<OrderItem> orderItems = order.getItems();

                // Ensure orderItems is never null
                if (orderItems == null) {
                    orderItems = new ArrayList<>();
                }

                request.setAttribute("orderItems", orderItems);
                request.setAttribute("order", order); // Pass the order object too
                request.getRequestDispatcher("orderDetails.jsp").forward(request, response);
            } else {
                response.sendRedirect("orderHistory.jsp?error=Order not found");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("orderHistory.jsp?error=Invalid order ID");
        }
    }
}