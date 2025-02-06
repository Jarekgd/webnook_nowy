package com.jaro.webnookbook.servlets;

import com.jaro.webnookbook.managers.BookManager;
import com.jaro.webnookbook.models.Book;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/SearchBookServlet")
public class SearchBookServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String searchQuery = request.getParameter("query");
        String searchType = request.getParameter("type");

        List<Book> books;
        if ("title".equals(searchType)) {
            books = BookManager.searchBooksByTitle(searchQuery);
        } else if ("author".equals(searchType)) {
            books = BookManager.searchBooksByAuthor(searchQuery);
        } else {
            books = null;
        }

        request.setAttribute("books", books);
        request.getRequestDispatcher("customerSearchBook.jsp").forward(request, response);
    }
}
