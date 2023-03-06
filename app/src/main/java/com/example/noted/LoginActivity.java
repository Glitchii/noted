package com.example.noted;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    // Inserts text into the username and password fields for testing purposes.
    private void debugAutoFill(EditText username, EditText password, Button submit) {
        username.setText("john");
        password.setText("password");
        submit.performClick();
    }

    // Define a HashMap to store valid usernames and passwords
    private HashMap<String, String> users = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        // Add some valid usernames and passwords to the HashMap
        users.put("john", "password");
        users.put("luke", "123");
        users.put("mark", "abc");

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
            startActivity(intent);
        });

        // debugAutoFill(usernameField, passwordField, submitButton);
    }
}