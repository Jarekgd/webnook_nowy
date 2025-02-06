<%@page import="com.jaro.webnookbook.models.Order"%>
<%@page import="com.jaro.webnookbook.managers.OrderManager"%>
<%@page import="java.util.List"%>
<%@ page session="true" %>

<%
    if (!"Admin".equals(session.getAttribute("userRole"))) {
        response.sendRedirect("login.jsp");
    }

    OrderManager orderManager = new OrderManager();
    String searchUser = request.getParameter("searchUser");
    List<Order> orders = (searchUser != null && !searchUser.isEmpty()) 
        ? orderManager.getUserOrders(searchUser) 
        : orderManager.getAllOrders();
%>

<html>
<head>
    <title>Admin - Manage Orders</title>
</head>
<body>
    <h2>All Orders</h2>

    <form method="GET">
        <label>Search Orders by User:</label>
        <input type="text" name="searchUser" placeholder="Enter username">
        <button type="submit">Search</button>
    </form>

    <table border="1">
        <tr>
            <th>Order ID</th>
            <th>User Login</th>
            <th>Total Amount</th>
            <th>Order Date</th>
            <th>Status</th>
            <th>Details</th>
        </tr>
        <% for (Order order : orders) { %>
            <tr>
                <td><%= order.getOrderId() %></td>
                <td><%= order.getUserLogin() %></td>
                <td>$<%= order.getTotalAmount() %></td>
                <td><%= order.getOrderDate() %></td>
                <td><%= order.getStatus() %></td>
                <td><a href="orderDetails.jsp?orderId=<%= order.getOrderId() %>">View</a></td>
            </tr>
        <% } %>
    </table>

    <a href="adminDashboard.jsp">Back to Dashboard</a>
</body>
</html>
