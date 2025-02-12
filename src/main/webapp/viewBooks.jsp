<%@page import="com.jaro.webnookbook.managers.BookManager"%>
<%@page import="com.jaro.webnookbook.models.Book"%>
<%@ page import="java.util.ArrayList" %>

<%@ page session="true" %>
<html>
    <head><title>Book List</title></head>
    <body>
        <h2>Available Books</h2>
        <%
            ArrayList<Book> books = BookManager.getAllBooks();
            for (Book book : books) {
        %>
        <p>
            <b>Title:</b> <%= book.getName()%><br>
            <b>Author:</b> <%= book.getAuthor()%><br>
            <b>Price:</b> $<%= book.getPrice()%><br>
        </p>
        <hr>
        <% }%>
        <a href="customerDashboard.jsp">Back</a>
    </body>
</html>
