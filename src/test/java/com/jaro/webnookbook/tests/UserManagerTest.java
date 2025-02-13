package com.jaro.webnookbook.tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import com.jaro.webnookbook.managers.UserManager;
import com.jaro.webnookbook.tests.TestDatabaseUtil;
import org.junit.jupiter.api.Disabled;

public class UserManagerTest {
    @Disabled
    @BeforeAll
    public static void setup() {
        TestDatabaseUtil.setupTestDatabase();
    }
@Disabled
    @Test
    public void testGetUserBalance() {
        double balance = UserManager.getUserBalance("testUser");
        assertEquals(100.00, balance, 0.01, "Balance should be 100.00 for testUser.");
    }
@Disabled
    @Test
    public void testUpdateUserBalance() {
        UserManager.updateUserBalance("testUser", 80.50);
        double balance = UserManager.getUserBalance("testUser");
        assertEquals(80.50, balance, 0.01, "Balance should be updated to 80.50.");
    }
}
