package com.jaro.webnookbook.managers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerManager {

    public static double getBalance(String userLogin) {
        String query = "SELECT balance FROM users WHERE login = ?";
        double balance = 0.0;

        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, userLogin);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    balance = rs.getDouble("balance");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return balance;
    }

    public static boolean updateBalance(String userLogin, double amount) {
        String query = "UPDATE users SET balance = balance - ? WHERE login = ? AND balance >= ?";

        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDouble(1, amount);
            stmt.setString(2, userLogin);
            stmt.setDouble(3, amount); // Ensure user has enough balance

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0; // Return true if balance was updated successfully

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean addFunds(String userLogin, double amount) {
        String query = "UPDATE users SET balance = balance + ? WHERE login = ?";

        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDouble(1, amount);
            stmt.setString(2, userLogin);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0; // Return true if balance was updated successfully

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getBalanceMessage(String userLogin) {
        double balance = getBalance(userLogin);
        return "Your current balance is: " + balance;
    }
}
