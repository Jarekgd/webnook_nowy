<%@page import="com.jaro.webnookbook.managers.CategoryManager"%>
<%@page import="com.jaro.webnookbook.models.Category"%>
<%@ page import="java.util.ArrayList" %>


<%@ page session="true" %>
<%
    if (!"Admin".equals(session.getAttribute("userRole"))) {
        response.sendRedirect("login.jsp");
    }
%>

<html>
    <head>
        <title>Manage Categories</title>
        <link rel="stylesheet" type="text/css" href="style.css">
    </head>
    <body>
        <div class="container">
            <h2>Manage Categories</h2>


            <a href="addCategory.jsp">Add New Category</a>

            <h3>Existing Categories</h3>
            <%
                ArrayList<Category> categories = CategoryManager.getAllCategories();
                for (Category category : categories) {
            %>
            <p>
                <b>Category Name:</b> <%= category.getName()%><br>
                <a href="editCategory.jsp?id=<%= category.getId()%>">Edit</a> | 
                <a href="DeleteCategoryServlet?id=<%= category.getId()%>" onclick="return confirm('Are you sure?');">Delete</a>
            </p>
            <hr>
            <% }%>

            <a href="adminDashboard.jsp">Back</a>
        </div>
    </body>
</html>
