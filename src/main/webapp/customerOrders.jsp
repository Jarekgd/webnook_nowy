<%@ page import="com.jaro.webnookbook.managers.OrderManager" %>
<%@ page import="com.jaro.webnookbook.models.Order" %>

<%
    int orderId = Integer.parseInt(request.getParameter("orderId")); 
    OrderManager orderManager = new OrderManager(); 
    Order order = orderManager.getOrderDetails(orderId); 

    if (order == null) {
        out.println("<h3>Order not found!</h3>");
    } else {
%>
    <h2>Order Details</h2>
    <p>Order ID: <%= order.getOrderId() %></p>
    <p>Total Amount: $<%= order.getTotalAmount() %></p>
    <p>Date: <%= order.getOrderDate() %></p>
<% } %>
