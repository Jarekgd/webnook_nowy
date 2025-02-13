<%@ page import="java.util.List" %>
<%@ page import="com.jaro.webnookbook.models.Order" %>
<%@ page import="com.jaro.webnookbook.managers.OrderManager" %>
<%@ page session="true" %>

<%
    String userLogin = (String) session.getAttribute("userLogin");
    if (userLogin == null) {
        response.sendRedirect("login.jsp?error=Please log in first");
        return;
    }

    List<Order> userOrders = OrderManager.getUserOrders(userLogin);
%>

<html>
<head>
    <title>Your Order History</title>
    <link rel="stylesheet" type="text/css" href="style.css">
</head>
<body>
    <div class="container">
    <h2>Your Order History</h2>

    <% if (userOrders.isEmpty()) { %>
        <p>You have no past orders.</p>
    <% } else { %>
        <table border="1">
            <tr>
                <th>Order ID</th>
                <th>Total Price</th>
                <th>Order Date</th>
                <th>Status</th>
                <th>Details</th>
            </tr>
            <% for (Order order : userOrders) { %>
                <tr>
                    <td><%= order.getOrderId() %></td>
                    <td>£<%= String.format("%.2f", order.getTotalAmount()) %></td>
                    <td><%= order.getOrderDate() %></td>
                    <td><%= order.getStatus() %></td>
                    <td><a href="orderDetails.jsp?orderId=<%= order.getOrderId() %>">View</a></td>
                </tr>
            <% } %>
        </table>
    <% } %>

    <a href="customerDashboard.jsp">Back to Dashboard</a>
    </div>
</body>
</html>
