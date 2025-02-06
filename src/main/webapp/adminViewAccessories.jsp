<%@page import="com.jaro.webnookbook.models.Accessory"%>
<%@page import="com.jaro.webnookbook.managers.AccessoryManager"%>
<%@ page import="java.util.ArrayList" %>
<%@ page session="true" %>
<%
    if (!"Admin".equals(session.getAttribute("userRole"))) {
        response.sendRedirect("login.jsp");
    }

    ArrayList<Accessory> accessories = AccessoryManager.getAllAccessories();
%>

<html>
<head>
    <title>Admin - View Accessories</title>
    <link rel="stylesheet" type="text/css" href="style.css">

</head>
<body>
    <div class="container">
    <h2>Accessories</h2>

    <table border="1">
        <tr>
            <th>Serial Number</th>
            <th>Name</th>
            <th>Price</th>
            <th>Quantity</th>
            <th>Actions</th>
        </tr>
        <% for (Accessory accessory : accessories) { %>
            <tr>
                <td><%= accessory.getSerialNo() %></td>
                <td><%= accessory.getName() %></td>
                <td><%= accessory.getPrice() %></td>
                <td><%= accessory.getQuantity() %></td>
                <td>
                    <a href="editAccessory.jsp?id=<%= accessory.getSerialNo() %>">Edit</a> |
                    <a href="DeleteAccessoryServlet?id=<%= accessory.getSerialNo() %>" onclick="return confirm('Are you sure?');">Delete</a>
                </td>
            </tr>
        <% } %>
    </table>

    <a href="adminDashboard.jsp">Back to Dashboard</a>
    </div>
</body>
</html>
