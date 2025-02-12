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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String orderIdParam = request.getParameter("orderId");

        if (orderIdParam == null || orderIdParam.isEmpty()) {
            response.sendRedirect("customerOrders.jsp?error=Invalid order ID");
            return;
        }

        int orderId = Integer.parseInt(orderIdParam);
        Order order = OrderManager.getOrderDetails(orderId);

        if (order == null) {
            response.sendRedirect("customerOrders.jsp?error=Order not found");
            return;
        }

        request.setAttribute("order", order);
        request.getRequestDispatcher("orderDetails.jsp").forward(request, response);
    }
}
