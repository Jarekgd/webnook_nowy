package com.jaro.webnookbook.managers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerManager {
    
    public static double getBalance(String userLogin) {
        String query = "SELECT balance FROM users WHERE login = ?";
        double balance = 0.0;
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
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

    public static void updateBalance(String userLogin, double newBalance) {
        String updateSQL = "UPDATE users SET balance = ? WHERE login = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(updateSQL)) {
            stmt.setDouble(1, newBalance);
            stmt.setString(2, userLogin);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getBalanceMessage(String userLogin) {
        double balance = getBalance(userLogin);
        return "Your current balance is: " + balance;
    }
}