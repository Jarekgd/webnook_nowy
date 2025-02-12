<%@page import="com.jaro.webnookbook.models.OrderItem"%>
<%@page import="com.jaro.webnookbook.models.Order"%>
<%@page import="com.jaro.webnookbook.managers.OrderManager"%>
<%@ page import="java.util.List" %>
<%@ page session="true" %>

<%
    if (!"Admin".equals(session.getAttribute("userRole"))) {
        response.sendRedirect("login.jsp");
    }

    String orderIdParam = request.getParameter("orderId");
    int orderId = (orderIdParam != null) ? Integer.parseInt(orderIdParam) : -1;
    Order order = OrderManager.getOrderDetails(orderId);
%>

<html>
    <head>
        <title>Order Details</title>
    </head>
    <body>
        <h2>Order Details (ID: <%= order.getOrderId()%>)</h2>
        <p><strong>User:</strong> <%= order.getUserLogin()%></p>
        <p><strong>Total Amount:</strong> $<%= order.getTotalAmount()%></p>
        <p><strong>Order Date:</strong> <%= order.getOrderDate()%></p>
        <p><strong>Status:</strong> <%= order.getStatus()%></p>

        <h3>Items in this Order:</h3>
        <table border="1">
            <tr>
                <th>Item Name</th>
                <th>Quantity</th>
                <th>Price</th>
            </tr>
            <% for (OrderItem item : order.getItems()) {%>
            <tr>
                <td><%= item.getName()%></td>
                <td><%= item.getQuantity()%></td>
                <td>$<%= item.getPrice()%></td>
            </tr>
            <% }%>
        </table>

        <a href="adminOrders.jsp">Back to Orders</a>
    </body>
</html>
