<%@ page session="true" %>
<%@ page import="com.jaro.webnookbook.managers.CustomerManager" %>
<%
    if (!"Customer".equals(session.getAttribute("userRole"))) {
        response.sendRedirect("login.jsp");
    }
    String userLogin = (String) session.getAttribute("userLogin");
    double balance = CustomerManager.getBalance(userLogin);
%>

<html>
<head>
    <title>Customer Dashboard</title>
    <link rel="stylesheet" type="text/css" href="style.css">
</head>
<body>
    <div class="container">
    <h2>Welcome, <%= userLogin %>!</h2>
    <h3>Your Balance: $<%= balance %></h3>

    <h3>Add Funds</h3>
    <form action="AddFundsServlet" method="post">
        <input type="number" name="amount" step="0.01" min="0.01" required>
        <button type="submit">Add Funds</button>
    </form>

    <h3>Shopping Options</h3>
    <ul>
        <li><a href="customerViewBooks.jsp">View Books</a></li>
        <li><a href="customerViewAccessories.jsp">View Accessories</a></li>
        <li><a href="customerSearchBook.jsp">Search for Books</a></li>
    <li><a href="orderHistory.jsp">View orders history</a></li>
    </ul>
    
    <h3>Your Shopping Cart</h3>
    <ul>
        <li><a href="customerCart.jsp">View & Edit Cart</a></li>
        <li>
            <form action="CheckoutServlet" method="post">
                <button type="submit">Proceed to Checkout</button>
            </form>
        </li>
    </ul>

    <h3>Account Management</h3>
    <ul>
        <li><a href="editAccount.jsp">Edit Account Details</a></li>
        <li><a href="logout.jsp">Logout</a></li>
    </ul>
    </div>
</body>
</html>