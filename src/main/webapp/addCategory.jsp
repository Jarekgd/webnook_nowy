<%@ page session="true" %>
<%
    if (!"Admin".equals(session.getAttribute("userRole"))) {
        response.sendRedirect("login.jsp");
    }
%>

<html>
<head>
    <title>Add Category</title>
    <link rel="stylesheet" type="text/css" href="style.css">
</head>
<body>
    <div class="container">
    <h2>Add New Category</h2>
    
    <!-- Form to Add a New Category -->
    <form action="AddCategoryServlet" method="post">
        <label>Category Name:</label>
        <input type="text" name="categoryName" required><br>

        <button type="submit">Add Category</button>
    </form>

    <a href="manageCategories.jsp">Back to Categories</a>
    <div class="container">
</body>
</html>
