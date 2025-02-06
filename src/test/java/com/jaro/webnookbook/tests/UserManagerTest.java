package com.jaro.webnookbook.tests;

import com.jaro.webnookbook.managers.UserManager;
import com.jaro.webnookbook.models.User;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserManagerTest {

    private static final String DB_URL = "jdbc:sqlite:C:\\webnookbook\\sqlite\\nookbook.db";
    private UserManager userManager;
@Disabled 
    @BeforeEach
void setUp() throws SQLException {
    Connection conn = DriverManager.getConnection(DB_URL);

    // Delete the existing user before inserting
    String deleteSql = "DELETE FROM users WHERE login = 'testuser'";
    PreparedStatement deleteStmt = conn.prepareStatement(deleteSql);
    deleteStmt.executeUpdate();

    // Insert a new test user
    String insertSql = "INSERT INTO users (email, userName, login, password, privilege) VALUES ('email@com', 'Test Er', 'testuser', 'testpass', '2')";
    PreparedStatement insertStmt = conn.prepareStatement(insertSql);
    insertStmt.executeUpdate();

    conn.close();
}

    @Disabled
    @Test
    void testValidUserAuthentication() {
        User user = userManager.authenticateUser("testuser", "testpass");
        assertNotNull(user, "User should be authenticated");
        assertEquals("Customer", user.getPrivilege(), "User role should be Customer");
    }
    
//    @Disabled
    @Test
    void testInvalidUserAuthentication() {
        User user = userManager.authenticateUser("wrongUser", "wrongPassword");
        assertNull(user, "User should not be authenticated");
    }
    
    @Disabled
    @Test
    void testNonExistentUser() {
        User user = userManager.authenticateUser("noUser", "noPass");
        assertNull(user, "User should not exist in the database");
    }
    
    @Test
void testSQLInjectionAttempt() {
    User user = UserManager.authenticateUser("' OR '1'='1", "' OR '1'='1");
    assertNull(user, "Authentication should fail for SQL injection attempt");
}

@Test
void testEmptyCredentials() {
    User user = UserManager.authenticateUser("", "");
    assertNull(user, "Authentication should fail for empty credentials");
}

}
