package com.example.noted;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.HashMap;

public class LoginUnitTest {
    private final HashMap<String, String> testUsers = new HashMap<>();

    @Test
    public void login_isCorrect() {
        // Add test users to HashMap
        testUsers.put("asdf", "1234");
        testUsers.put("john", "abcd");

        // Test valid login
        assertTrue(LoginActivity.isValidLogin("asdf", "1234", testUsers));

        // Test invalid login
        assertFalse(LoginActivity.isValidLogin("asdf", "incorrect password", testUsers));
    }
}