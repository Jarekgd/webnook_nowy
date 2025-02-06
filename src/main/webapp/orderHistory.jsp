<%@ page import="com.jaro.webnookbook.managers.OrderManager" %>
<%@ page import="com.jaro.webnookbook.models.Order" %>
<%@ page import="java.util.List" %>
<%
    String userLogin = (String) session.getAttribute("userLogin");
    if (userLogin == null) {
        response.sendRedirect("login.jsp");
    }

    OrderManager orderManager = new OrderManager();
    List<Order> orders = orderManager.getUserOrders(userLogin);
%>

<h2>Your Order History</h2>
<table border="1">
    <tr>
        <th>Order ID</th>
        <th>Order Date</th>
        <th>Total Amount</th>
        <th>Status</th>
        <th>Details</th>
    </tr>
    <%
        for (Order order : orders) {
    %>
        <tr>
            <td><%= order.getOrderId() %></td>
            <td><%= order.getOrderDate() %></td>
            <td>$<%= order.getTotalAmount() %></td>
            <td><%= order.getStatus() %></td>
            <td><a href="orderDetails.jsp?orderId=<%= order.getOrderId() %>">View</a></td>
        </tr>
    <%
        }
    %>
</table>
<a href="customerDashboard.jsp">Back to Dashboard</a>
