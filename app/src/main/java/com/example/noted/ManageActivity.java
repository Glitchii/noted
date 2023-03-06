package com.example.noted;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class ManageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);
        getSupportActionBar().hide();

        String username;
        Intent intent = getIntent();
        TextView greetingName = findViewById(R.id.greetingName);

        username = intent.getStringExtra("username");
        // Capitalize the first letter of the username
        username = username.substring(0, 1).toUpperCase() + username.substring(1);

        greetingName.setText(username);
        Toast.makeText(this, "Logged in as " + username, Toast.LENGTH_LONG).show();
    }
}