<%@ page import="com.jaro.webnookbook.models.Order" %>
<%@ page import="com.jaro.webnookbook.managers.OrderManager" %>
<%@ page import="java.util.List" %>

<%
    String orderIdParam = request.getParameter("orderId");

    if (orderIdParam == null || orderIdParam.isEmpty()) {
        out.println("<p style='color:red;'>Error: Invalid or missing order ID</p>");
        return;
    }

    int orderId = Integer.parseInt(orderIdParam);
    Order order = OrderManager.getOrderDetails(orderId);

    if (order == null) {
        out.println("<p style='color:red;'>Error: Order not found.</p>");
        return;
    }
%>

<html>
    <head>
        <title>Order Details</title>
    </head>
    <body>
        <h2>Order Details</h2>

        <p>Order ID: <%= order.getOrderId()%></p>
        <p>User: <%= order.getUserLogin()%></p>
        <p>Total Amount: $<%= order.getTotalAmount()%></p>
        <p>Status: <%= order.getStatus()%></p>

        <h3>Order Items:</h3>
        <ul>
            <% for (var item : order.getItems()) {%>
            <li><%= item.getName()%> - $<%= item.getPrice()%> (x<%= item.getQuantity()%>)</li>
                <% }%>
        </ul>

        <a href="customerDashboard.jsp">Back to Orders</a>
    </body>
</html>
