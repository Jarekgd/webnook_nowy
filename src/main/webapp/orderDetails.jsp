<%@ page import="java.util.List" %>
<%@ page import="com.jaro.webnookbook.models.Order" %>
<%@ page import="com.jaro.webnookbook.models.OrderItem" %>
<%@ page import="com.jaro.webnookbook.managers.OrderManager" %>

<%
    String orderIdParam = request.getParameter("orderId");
    if (orderIdParam == null || orderIdParam.isEmpty()) {
        response.sendRedirect("customerOrders.jsp?error=Invalid order ID");
        return;
    }

    int orderId = Integer.parseInt(orderIdParam);
    Order order = OrderManager.getOrderDetails(orderId);
    List<OrderItem> orderItems = OrderManager.getOrderItems(orderId);
%>

<html>
<head>
    <title>Order Details</title>
    <link rel="stylesheet" type="text/css" href="style.css">
</head>
<body>
    <div class="container">
    <h2>Order Details</h2>

    <p><strong>Order ID:</strong> <%= order.getOrderId() %></p>
    <p><strong>Total Amount:</strong> $<%= order.getTotalAmount() %></p>
    <p><strong>Status:</strong> <%= order.getStatus() %></p>

    <h3>Items in Order:</h3>
    <table border="1">
        <tr>
            <th>Product Serial</th>
            <th>Quantity</th>
            <th>Price</th>
        </tr>
        <% for (OrderItem item : orderItems) { %>
            <tr>
    <td><%= item.getProductSerial() %></td>  
    <td><%= item.getQuantity() %></td>
    <td>$<%= String.format("%.2f", item.getPrice()) %></td>
</tr>

        <% } %>
    </table>

    <a href="customerOrders.jsp">Back to Orders</a>
    </div>
</body>
</html>
