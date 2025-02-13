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
            stmt.execute("CREATE TABLE Users (userId INTEGER PRIMARY KEY, login TEXT UNIQUE, balance REAL);");
            stmt.execute("CREATE TABLE Cart (userLogin TEXT, serialNo TEXT, name TEXT, price REAL, quantity INTEGER, PRIMARY KEY (userLogin, serialNo));");
            stmt.execute("CREATE TABLE Orders (orderId INTEGER PRIMARY KEY, userId INTEGER, totalPrice REAL, orderDate TIMESTAMP, status TEXT DEFAULT 'Pending');");
            stmt.execute("CREATE TABLE OrderItems (itemId INTEGER PRIMARY KEY, orderId INTEGER, productSerial TEXT, quantity INTEGER, price REAL);");

            // Insert test data
            stmt.execute("INSERT INTO Users (userId, login, balance) VALUES (1, 'testUser', 100.00);");
            stmt.execute("INSERT INTO Cart (userLogin, serialNo, name, price, quantity) VALUES ('testUser', 'B123', 'Test Book', 15.99, 2);");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
