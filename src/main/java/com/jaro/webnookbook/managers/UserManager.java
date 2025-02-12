package com.jaro.webnookbook.managers;

import com.jaro.webnookbook.models.User;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import static javax.swing.UIManager.getString;

/**
 * UserManager class for handling user-related operations
 */
public class UserManager {
    private static final String DB_URL = "jdbc:sqlite:C:\\webnookbook\\sqlite\\nookbook.db";

   public static ArrayList<User> getAllUsers() {
        ArrayList<User> users = new ArrayList<>();
        try {
            Class.forName("org.sqlite.JDBC");
            try (Connection connection = DriverManager.getConnection(DB_URL)) {
                String sql = "SELECT u.email, u.userName, u.login, r.roleName " +
                             "FROM users u JOIN roles r ON u.privilege = r.roleId";
                try (PreparedStatement pstmt = connection.prepareStatement(sql);
                     ResultSet rs = pstmt.executeQuery()) {

                    while (rs.next()) {
                        users.add(new User(
                            rs.getString("email"),
                            rs.getString("userName"),
                            rs.getString("login"),
                            null,  // Not returning password for security
                            rs.getString("roleName")  // Role: "Admin" or "Customer"
                        ));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }
   
public static User authenticateUser(String login, String password) {
    try {
        Class.forName("org.sqlite.JDBC");
        try (Connection connection = DriverManager.getConnection(DB_URL)) {
            String sql = "SELECT u.email, u.userName, u.login, u.password, r.roleName " +
                         "FROM users u JOIN roles r ON u.privilege = r.roleId " +
                         "WHERE u.login = ?";

            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, login);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        String storedPassword = rs.getString("password");
                        String storedRole = rs.getString("roleName");

                        System.out.println("User found: " + login + " | Role: " + storedRole);
                        System.out.println("Stored password: " + storedPassword);
                        System.out.println("Provided password: " + password);

                        if (storedPassword.equals(password)) {  
                            return new User(
                                rs.getString("email"),
                                rs.getString("userName"),
                                rs.getString("login"),
                                rs.getString("password"),
                                storedRole
                            );
                        } else {
                            System.out.println("Password mismatch for user: " + login);
                        }
                    } else {
                        System.out.println("No user found with login: " + login);
                    }
                }
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;  // Return null if login fails
}
   
    public static User getUserById(int userId) {
    User user = null;
    try {
        Class.forName("org.sqlite.JDBC");
        try (Connection connection = DriverManager.getConnection(DB_URL)) {
            String sql = "SELECT userId, email, userName, login, password, roles.roleName FROM users " +
                         "JOIN roles ON users.privilege = roles.roleId WHERE userId = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, userId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        user = new User(
                            rs.getString("email"),
                            rs.getString("userName"),
                            rs.getString("login"),
                            rs.getString("password"),
                            rs.getString("roleName")
                        );
                    }
                }
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return user;
}
    
    public static User getUserByLogin(String login) {
    User user = null;
    try {
        Class.forName("org.sqlite.JDBC");
        try (Connection connection = DriverManager.getConnection(DB_URL)) {
            String sql = "SELECT email, userName, login, password, roles.roleName FROM users " +
                         "JOIN roles ON users.privilege = roles.roleId WHERE login = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, login);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        user = new User(
                            rs.getString("email"),
                            rs.getString("userName"),
                            rs.getString("login"),
                            rs.getString("password"),
                            rs.getString("roleName")
                        );
                    }
                }
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return user;
}
    
public static boolean updateUser(String login, String email, String userName, String password, String privilege) {
    boolean updated = false;
    try {
        Class.forName("org.sqlite.JDBC");
        try (Connection connection = DriverManager.getConnection(DB_URL)) {
            // Only update fields that are not null
            String sql = "UPDATE users SET email = ?, userName = ?, password = ?, privilege = ? WHERE login = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, email);
                pstmt.setString(2, userName);
                pstmt.setString(3, password);
                pstmt.setString(4, "2");
                pstmt.setString(5, login);

                int affectedRows = pstmt.executeUpdate();
                updated = (affectedRows > 0);
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return updated;
}

    
    
    
    
    public static boolean deleteUserById(int userId) {
    boolean deleted = false;
    try {
        Class.forName("org.sqlite.JDBC");
        try (Connection connection = DriverManager.getConnection(DB_URL)) {
            String sql = "DELETE FROM users WHERE userId = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, userId);
                int affectedRows = pstmt.executeUpdate();
                deleted = (affectedRows > 0);
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return deleted;
}
    
public static boolean deleteUserByLogin(String login) {
    boolean deleted = false;
    try {
        Class.forName("org.sqlite.JDBC");
        try (Connection connection = DriverManager.getConnection(DB_URL)) {
            String sql = "DELETE FROM users WHERE login = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, login);
                int affectedRows = pstmt.executeUpdate();
                deleted = (affectedRows > 0);
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return deleted;
}

public static int getUserId(String userLogin) {
    String query = "SELECT userId FROM users WHERE login = ?";
    int userId = -1;

    try (Connection conn = DriverManager.getConnection(DB_URL);
         PreparedStatement stmt = conn.prepareStatement(query)) {
        
        stmt.setString(1, userLogin);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            userId = rs.getInt("userId");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return userId;
}


    
}
