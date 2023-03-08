package com.example.noted;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.StatusBarManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

public class ManageActivity extends AppCompatActivity {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);
        getSupportActionBar().hide();

        String username;
        Intent intent = getIntent();
        TextView greetingName = findViewById(R.id.greetingName);

        // Get username from intent shared from LoginActivity
        username = intent.getStringExtra("username");
        // Capitalize the first letter of the username
        username = username.substring(0, 1).toUpperCase() + username.substring(1);

        greetingName.setText(username);
        ActionBar actionBar = getSupportActionBar();


        actionBar.setTitle("Hello, " + username);
        Toast.makeText(this, "Logged in as " + username, Toast.LENGTH_LONG).show();
    }
}