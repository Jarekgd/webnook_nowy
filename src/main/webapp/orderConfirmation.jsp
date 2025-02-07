
<%@ page import="com.jaro.webnookbook.managers.DatabaseConnection, java.sql.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Order Confirmation</title>
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
<div class="container">
    <h2>Thank You for Your Order!</h2>

    <%
        String orderId = request.getParameter("orderId");
        String orderStatus = "Unknown";
        double totalPrice = 0;

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT totalPrice, status FROM Orders WHERE orderId = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, Integer.parseInt(orderId));
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    totalPrice = rs.getDouble("totalPrice");
                    orderStatus = rs.getString("status");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    %>

    <p><strong>Order ID:</strong> <%= orderId %></p>
    <p><strong>Status:</strong> <%= orderStatus %></p>
    <p><strong>Total Price:</strong> $<%= totalPrice %></p>

    <h3>Ordered Items:</h3>
    <table>
        <tr>
            <th>Product Name</th>
            <th>Quantity</th>
            <th>Price</th>
        </tr>
        <%
            try (Connection conn = DatabaseConnection.getConnection()) {
                String orderItemsSql = "SELECT productSerial, quantity, price FROM OrderItems WHERE orderId = ?";
                try (PreparedStatement orderItemStmt = conn.prepareStatement(orderItemsSql)) {
                    orderItemStmt.setInt(1, Integer.parseInt(orderId));
                    ResultSet rs = orderItemStmt.executeQuery();
                    while (rs.next()) {
                        String productSerial = rs.getString("productSerial");
                        int quantity = rs.getInt("quantity");
                        double price = rs.getDouble("price");

                        String productName = "Unknown";
                        String getProductNameSql = "SELECT title AS name FROM books WHERE serialNo = ? "
                                                 + "UNION SELECT accessoryName AS name FROM accessories WHERE serialNo = ?";
                        try (PreparedStatement productStmt = conn.prepareStatement(getProductNameSql)) {
                            productStmt.setString(1, productSerial);
                            productStmt.setString(2, productSerial);
                            ResultSet productRs = productStmt.executeQuery();
                            if (productRs.next()) {
                                productName = productRs.getString("name");
                            }
                        }
        %>
        <tr>
            <td><%= productName %></td>
            <td><%= quantity %></td>
            <td>$<%= price %></td>
        </tr>
        <%
                    }
                }
            }
        %>
    </table>
    <a href="customerDashboard.jsp">Return to Dashboard</a>
</div>
</body>
</html>