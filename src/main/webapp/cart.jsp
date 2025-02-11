<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.jaro.webnookbook.models.CartItem" %>
<%@ page import="com.jaro.webnookbook.managers.CartManager" %>
<%@ page import="com.jaro.webnookbook.models.User" %>

<html>
<head>
    <title>Shopping Cart</title>
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
<div class="container">
    <h2>Your Shopping Cart</h2>

    <%
        // Retrieve user login from session
        String userLogin = (String) session.getAttribute("userLogin");
        if (userLogin == null) {
            response.sendRedirect("login.jsp?error=Please log in first");
            return;
        }

        // Fetch cart items for the logged-in user
        ArrayList<CartItem> cartItems = CartManager.getCart(userLogin);
        double totalAmount = 0.0;
    %>

    <% if (cartItems == null || cartItems.isEmpty()) { %>
        <p>Your cart is empty.</p>
        <a href="customerDashboard.jsp">Go Back</a>
    <% } else { %>
        <table border="1">
            <tr>
                <th>Item</th>
                <th>Price</th>
                <th>Quantity</th>
                <th>Total</th>
            </tr>
            
            <% for (CartItem item : cartItems) { 
                double itemTotal = item.getPrice() * item.getQuantity();
                totalAmount += itemTotal;
            %>
            <tr>
                <td><%= item.getName() %></td>
                <td><%= item.getPrice() %></td>
                <td><%= item.getQuantity() %></td>
                <td><%= itemTotal %></td>
            </tr>
            <% } %>
        </table>

        <p><strong>Total Amount: </strong> <%= totalAmount %></p>

        <form action="checkoutServlet" method="POST">
            <button type="submit">Proceed to Checkout</button>
        </form>

        <a href="customerDashboard.jsp">Continue Shopping</a>
    <% } %>

</div>
</body>
</html>
