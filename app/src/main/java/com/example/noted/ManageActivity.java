package com.example.noted;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.StatusBarManager;
import android.content.Context;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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

        String username;
        Intent intent = getIntent();
        ActionBar actionBar = getSupportActionBar();

        // Get username from intent shared from LoginActivity
        username = intent.getStringExtra("username");
        // Capitalize the first letter of the username
        username = username.substring(0, 1).toUpperCase() + username.substring(1);

        actionBar.setTitle(username);
        Toast.makeText(this, "Logged in as " + username, Toast.LENGTH_LONG).show();

        try {
            FileOutputStream fileOutputStream = openFileOutput("lol.txt", Context.MODE_PRIVATE);
            fileOutputStream.write("Hello".getBytes());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Get current timestamp
        // https://strftime.org | https://stackoverflow.com/a/23068721/11848657
        Log.d("DEBUG_TAG", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        // Create 'notes' folder where all note files will be created
        File notesDir = new File(getFilesDir(), "notes");
        if (!notesDir.exists())
            notesDir.mkdir();

        // Loop through the 'notes' directory to find any files to be displayed in list
        File[] files = notesDir.listFiles();
        for (File file : files)
            Log.d("DEBUG_TAG", file.getAbsolutePath());

        // Create new file with current time-stamp
        // String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        // String filename = username + "-" + timestamp;
        // try {
        //     if (new File(notesDir, filename).createNewFile())
        //         Log.d("DEBUG_TAG", "File " + filename + " created.");
        // } catch (IOException e) {
        //     throw new RuntimeException(e);
        // }
    }
}