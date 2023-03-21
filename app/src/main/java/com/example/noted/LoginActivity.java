package com.example.noted;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class LoginActivity extends GlobalAppCompatActivity {
    // Define a HashMap to store valid usernames and passwords
    private final HashMap<String, String> users = new HashMap<>();

    /**
     * Static method to check whether a username and corresponding password are correct and valid
     * based on a provided hashmap
     */
    public static boolean isValidLogin(String username, String password, HashMap<String, String> hashMap) {
        // Shouldn't matter the case of the username
        username = username.toLowerCase();

        // Return false if username or password is empty
        if (username.isEmpty() || password.isEmpty())
            return false;

        // Return whether the username and corresponding password are in the hashmap
        return hashMap.containsKey(username) && hashMap.get(username).equals(password);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Login is not required on login page
        loginRequired = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        // Add ten default usernames and passwords to the HashMap
        users.put("john", "password");
        users.put("luke", "123");
        users.put("mark", "abc");
        users.put("james", "321");
        users.put("peter", "cba");
        users.put("mary", "pwd");
        users.put("jane", "asdf");
        users.put("joseph", "Joseph");
        users.put("joshua", "Joshua123");
        users.put("david", "abc123");

        // View variable declarations
        Button submitButton = findViewById(R.id.submitButton);
        EditText usernameField = findViewById(R.id.usernameField);
        EditText passwordField = findViewById(R.id.passwordField);


        // Set click listener on submit button
        submitButton.setOnClickListener(v -> {
            // Get the values entered in the text fields
            String username = usernameField.getText().toString();
            String password = passwordField.getText().toString();

            if (!isValidLogin(username, password, users)) {
                Toast.makeText(this, "Incorrect username or password.", Toast.LENGTH_SHORT).show();
                return;
            }

            saveCofig("loggedInUser", username);
            replaceActivity(LoginActivity.this, ManageActivity.class);
        });
    }
}