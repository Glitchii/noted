package com.example.noted;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    // Define a HashMap to store valid usernames and passwords
    private final HashMap<String, String> users = new HashMap<>();

    // Inserts text into the username and password fields for testing purposes.
    private void debugAutoFill(EditText username, EditText password, Button submit) {
        username.setText("john");
        password.setText("password");
        submit.performClick();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        // Find views
        Button submitButton = findViewById(R.id.submitButton);
        EditText usernameField = findViewById(R.id.usernameField);
        EditText passwordField = findViewById(R.id.passwordField);


        // Set click listener on submit button
        submitButton.setOnClickListener(v -> {
            // Get the values entered in the text fields
            String username = usernameField.getText().toString().toLowerCase();
            String password = passwordField.getText().toString();

            // Check if username and password are empty
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter a username and password.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if username and password are correct using the HashMap
            if (!(users.containsKey(username) && users.get(username).equals(password))) {
                Toast.makeText(this, "Incorrect username or password.", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(LoginActivity.this, ManageActivity.class);
            intent.putExtra("username", username);
            //developer.android.com/reference/android/content/Intent#constants_1
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        debugAutoFill(usernameField, passwordField, submitButton);
    }
}