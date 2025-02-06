<%@page import="com.jaro.webnookbook.managers.OrderManager"%>
<%@ page import="java.util.List, com.jaro.webnookbook.models.Order, com.jaro.webnookbook.models.OrderItem, com.jaro.webnookbook.database.OrderManager" %>
<%
    int orderId = Integer.parseInt(request.getParameter("orderId"));
    OrderManager orderManager = new OrderManager();
    Order order = orderManager.getOrderDetails(orderId);
%>

<h2>Order Details (Order ID: <%= order.getOrderId() %>)</h2>
<p><b>Order Date:</b> <%= order.getOrderDate() %></p>
<p><b>Total Amount:</b> $<%= order.getTotalAmount() %></p>
<p><b>Status:</b> <%= order.getStatus() %></p>

<h3>Items in this Order:</h3>
<table border="1">
    <tr>
        <th>Serial No</th>
        <th>Name</th>
        <th>Quantity</th>
        <th>Price</th>
    </tr>
    <%
        for (OrderItem item : order.getItems()) {
    %>
        <tr>
            <td><%= item.getSerialNo() %></td>
            <td><%= item.getName() %></td>
            <td><%= item.getQuantity() %></td>
            <td>$<%= item.getPrice() %></td>
        </tr>
    <%
        }
    %>
</table>

<a href="orderHistory.jsp">Back to Order History</a>
