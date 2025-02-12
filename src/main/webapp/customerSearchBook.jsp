<%@ page session="true" %>
<%@ page import="java.util.List, com.jaro.webnookbook.models.Book" %>

<%
    if (session.getAttribute("userRole") == null || !"Customer".equals(session.getAttribute("userRole"))) {
        response.sendRedirect("login.jsp");
        return;
    }
%>

<html>
    <head>
        <title>Search Books</title>
        <link rel="stylesheet" type="text/css" href="style.css">
    </head>
    <body>
        <div class="container">
            <h2>Search for Books</h2>

            <!-- Search Form -->
            <form action="SearchBookServlet" method="get">
                <input type="text" name="query" placeholder="Enter title or author" required>
                <select name="type">
                    <option value="title">Title</option>
                    <option value="author">Author</option>
                </select>
                <button type="submit">Search</button>
            </form>

            <h3>Search Results:</h3>
            <%
                List<Book> books = (List<Book>) request.getAttribute("books");
                if (books != null) {
                    out.println("<p>Debug: Found " + books.size() + " books.</p>");
                }
                if (books != null && !books.isEmpty()) {
            %>
            <table border="1">
                <tr>
                    <th>Title</th>
                    <th>Author</th>
                    <th>Price</th>
                    <th>Category</th>
                </tr>
                <%
                    for (Book book : books) {
                %>
                <tr>
                    <td><%= book.getName()%></td>   
                    <td><%= book.getAuthor()%></td>
                    <td><%= book.getPrice()%></td>
                    <td><%= book.getCategory().getName()%></td>  
                </tr>
                <%
                    }
                %>
            </table>
            <%
            } else {
            %>
            <p>No books found.</p>
            <%
                }
            %>

            <a href="customerDashboard.jsp">Back to Dashboard</a>
        </div>
    </body>
</html>
