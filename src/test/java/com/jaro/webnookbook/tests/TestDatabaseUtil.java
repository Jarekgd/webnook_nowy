package com.jaro.webnookbook.tests;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class TestDatabaseUtil {
    private static final String DB_URL = "jdbc:sqlite::memory:"; // Virtual DB

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void setupTestDatabase() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            // Create tables for testing
            stmt.execute("CREATE TABLE categories (categoryId   INTEGER PRIMARY KEY AUTOINCREMENT, categoryName TEXT);");          
            stmt.execute("CREATE TABLE accessories (accessoryId INTEGER PRIMARY KEY AUTOINCREMENT, serialNo TEXT UNIQUE, accessoryName TEXT, price REAL, quantity INTEGER);");
            stmt.execute("CREATE TABLE books (bookId INTEGER PRIMARY KEY AUTOINCREMENT, serialNo TEXT, title TEXT, author TEXT, price REAL, quantity INTEGER, categoryId REFERENCES categories (categoryId) );");
            stmt.execute("CREATE TABLE roles (roleId   INTEGER PRIMARY KEY AUTOINCREMENT, roleName TEXT);");
            stmt.execute("CREATE TABLE users (userId INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT UNIQUE, userName TEXT, login TEXT UNIQUE, password TEXT, privilege TEXT REFERENCES roles (roleId), balance DOUBLE DEFAULT 100.0);");
            stmt.execute("CREATE TABLE cart (userLogin TEXT, serialNo TEXT, name TEXT, price REAL, quantity INTEGER, PRIMARY KEY (userLogin, serialNo));");
            stmt.execute("CREATE TABLE orders (orderId INTEGER PRIMARY KEY AUTOINCREMENT, userId INTEGER NOT NULL, totalPrice REAL NOT NULL, orderDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP, status TEXT DEFAULT 'Pending');");
            stmt.execute("CREATE TABLE OrderItems (itemId INTEGER PRIMARY KEY AUTOINCREMENT, orderId INTEGER NOT NULL REFERENCES Orders (orderId), productSerial TEXT NOT NULL, quantity INTEGER NOT NULL, price REAL NOT NULL, FOREIGN KEY (orderId)REFERENCES orders (orderId) ON DELETE CASCADE);");

// Insert test data
            stmt.execute("INSERT INTO users (email, userName, login, password, privilege, balance) VALUES (1, 'testUser@gmail.com', 'testUser', 'testPass', 1, 100);");
            stmt.execute("INSERT INTO cart (userLogin, serialNo, name, price, quantity) VALUES ('testUser', 'B123', 'Test Book', 15.99, 2);");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
