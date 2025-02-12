<%@page import="com.jaro.webnookbook.models.User"%>
<%@ page session="true" %>
<%
    if (!"Admin".equals(session.getAttribute("userRole"))) {
        response.sendRedirect("login.jsp");
    }

    // Retrieve user object from request
    User user = (User) request.getAttribute("user");
    if (user == null) {
        response.sendRedirect("manageUsers.jsp?error=User not found");
        return;
    }
%>

<html>
    <head>
        <title>Edit User</title>
        <link rel="stylesheet" type="text/css" href="style.css">
    </head>
    <body>
        <div class="container">
            <h2>Edit User</h2>

            <form action="EditUserServlet" method="POST">
                <input type="hidden" name="originalLogin" value="<%= user.getLogin()%>">

                <label>Email:</label>
                <input type="email" name="email" value="<%= user.getEmail()%>"><br>

                <label>Username:</label>
                <input type="text" name="userName" value="<%= user.getUserName()%>"><br>

                <label>Password:</label>
                <input type="password" name="password" placeholder="Leave blank to keep current password"><br>

                <label>Privilege:</label>
                <select name="privilege">
                    <option value="Admin" <%= "Admin".equals(user.getPrivilege()) ? "selected" : ""%>>Admin</option>
                    <option value="Customer" <%= "Customer".equals(user.getPrivilege()) ? "selected" : ""%>>Customer</option>
                </select><br>

                <button type="submit">Save Changes</button>
            </form>


            <a href="manageUsers.jsp">Back to Users</a>
        </div>
    </body>
</html>
