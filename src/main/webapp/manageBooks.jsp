<%@page import="com.jaro.webnookbook.managers.BookManager"%>
<%@page import="com.jaro.webnookbook.models.Book"%>
<%@ page import="java.util.ArrayList" %>

<%@ page session="true" %>
<%
    if (!"Admin".equals(session.getAttribute("userRole"))) {
        response.sendRedirect("login.jsp");
    }
%>

<html>
<head>
    <title>Manage Books</title>
    <link rel="stylesheet" type="text/css" href="style.css">
</head>
<body>
    <div class="container">
    <h2>Manage Books</h2>
    <a href="addBook.jsp">Add New Book</a>

    <h3>Existing Books</h3>
    <%
        ArrayList<Book> books = BookManager.getAllBooks();
        for (Book book : books) {
    %>
        <p>
            <b>Title:</b> <%= book.getName() %><br>
            <b>Author:</b> <%= book.getAuthor() %><br>
            <b>Price:</b> $<%= book.getPrice() %><br>
            <a href="editBook.jsp?id=<%= book.getSerialNo() %>">Edit</a> | 
            <a href="DeleteBookServlet?id=<%= book.getSerialNo() %>" onclick="return confirm('Are you sure?');">Delete</a>
        </p>
        <hr>
    <% } %>

    <a href="adminDashboard.jsp">Back</a>
    </div>
</body>
</html>
