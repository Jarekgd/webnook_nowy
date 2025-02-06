package com.jaro.webnookbook.managers;

import com.jaro.webnookbook.models.Book;
import com.jaro.webnookbook.models.Category;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


//  BookManager class for handling book operations
public class BookManager {
    private static final String DB_URL = "jdbc:sqlite:C:\\webnookbook\\sqlite\\nookbook.db";

    public static ArrayList<Book> getAllBooks() {
        ArrayList<Book> books = new ArrayList<>();
        try {
            Class.forName("org.sqlite.JDBC");
            try (Connection connection = DriverManager.getConnection(DB_URL)) {
                String sql = "SELECT b.serialNo, b.title, b.author, b.price, b.quantity, c.categoryId, c.categoryName " +
                             "FROM books b JOIN categories c ON b.categoryId = c.categoryId";
                try (PreparedStatement pstmt = connection.prepareStatement(sql);
                     ResultSet rs = pstmt.executeQuery()) {

                    while (rs.next()) {
                        Category category = new Category(rs.getInt("categoryId"), rs.getString("categoryName"));
                        books.add(new Book(
                            rs.getString("serialNo"),
                            rs.getString("title"),
                            rs.getString("author"),
                            rs.getDouble("price"),
                            rs.getInt("quantity"),
                            category
                        ));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return books;
    }

    public static Book getBookBySerialNo(String serialNo) {
        Book book = null;
        try {
            Class.forName("org.sqlite.JDBC");
            try (Connection connection = DriverManager.getConnection(DB_URL)) {
                String sql = "SELECT b.serialNo, b.title, b.author, b.price, b.quantity, c.categoryId, c.categoryName " +
                             "FROM books b JOIN categories c ON b.categoryId = c.categoryId " +
                             "WHERE b.serialNo = ?";
                try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                    pstmt.setString(1, serialNo);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) {
                            Category category = new Category(rs.getInt("categoryId"), rs.getString("categoryName"));
                            book = new Book(
                                rs.getString("serialNo"),
                                rs.getString("title"),
                                rs.getString("author"),
                                rs.getDouble("price"),
                                rs.getInt("quantity"),
                                category
                            );
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return book;
    }
    public static void updateBookQuantity(String serialNo, int quantityPurchased) {
        try {
            Class.forName("org.sqlite.JDBC");
            try (Connection connection = DriverManager.getConnection(DB_URL)) {
                String checkSql = "SELECT quantity FROM books WHERE serialNo = ?";
                try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
                    checkStmt.setString(1, serialNo);
                    ResultSet rs = checkStmt.executeQuery();
                    
                    if (rs.next()) {
                        int currentQuantity = rs.getInt("quantity");
                        int newQuantity = Math.max(0, currentQuantity - quantityPurchased); // Ensure quantity doesn't go negative

                        String updateSql = "UPDATE books SET quantity = ? WHERE serialNo = ?";
                        try (PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {
                            updateStmt.setInt(1, newQuantity);
                            updateStmt.setString(2, serialNo);
                            updateStmt.executeUpdate();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Search books by title
    public static List<Book> searchBooksByTitle(String title) {
    List<Book> books = new ArrayList<>();
    String sql = "SELECT b.*, c.categoryName FROM books b "
               + "JOIN categories c ON b.categoryId = c.categoryId "
               + "WHERE LOWER(b.title) LIKE LOWER('%'||?||'%')";

    try (Connection conn = DriverManager.getConnection(DB_URL);
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        if (conn != null) {
            System.out.println("Database Connection Successful: " + DB_URL);
        } else {
            System.out.println("Database Connection FAILED!");
        }

        pstmt.setString(1, title); // No extra '%' needed

        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Book book = new Book(
                    rs.getString("serialNo"),
                    rs.getString("title"), // `title` is stored in DB but corresponds to `name` in Product
                    rs.getString("author"),
                    rs.getDouble("price"),
                    rs.getInt("quantity"),
                    new Category(rs.getInt("categoryId"), rs.getString("categoryName"))
                );
                books.add(book);
                System.out.println("Book Found: " + book.getName()); // ✅ Use getName() instead of getTitle()
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return books;
}





    // Search books by author
    public static List<Book> searchBooksByAuthor(String author) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE LOWER(author) LIKE LOWER('%'||?||'%');";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + author + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    books.add(new Book(
                        rs.getString("serialNo"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getDouble("price"),
                        rs.getInt("quantity"),
                        new Category(rs.getInt("categoryId"), rs.getString("categoryName"))
));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }
}
