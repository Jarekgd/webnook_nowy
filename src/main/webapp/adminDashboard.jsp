<%@ page session="true" %>
<%
    if (!"Admin".equals(session.getAttribute("userRole"))) {
        response.sendRedirect("login.jsp");
    }
%>

<html>
    <head>
        <title>Admin Dashboard</title>
        <link rel="stylesheet" type="text/css" href="style.css">
    </head>
    <body>
        <div class="container">
            <h2>Welcome, Admin</h2>

            <h3>Manage Products</h3>
            <ul>
                <li><a href="adminViewBooks.jsp">Manage Books</a></li>
                <li><a href="adminViewAccessories.jsp">Manage Accessories</a></li>
                <li><a href="manageCategories.jsp">Manage Categories</a></li>
            </ul>

            <h3>Manage Users</h3>
            <ul>
                <li><a href="manageUsers.jsp">Manage Users</a></li>
                <a href="adminOrders.jsp">Manage Orders</a>

            </ul>

            <h3>Account</h3>
            <ul>
                <li><a href="editAccount.jsp">Edit Your Account</a></li>
                <li><a href="logout.jsp">Logout</a></li>
            </ul>
        </div>
    </body>
</html>
