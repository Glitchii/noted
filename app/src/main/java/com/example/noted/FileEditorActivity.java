package com.example.noted;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.nio.file.Files;

public class FileEditorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_editor);

        Intent intent = getIntent();
        String filename = intent.getStringExtra("filename");
        File oldFile = new File(Utils.getAbsPath(this, filename));
        String username = intent.getStringExtra("username");
        Button fileUpdateButton = findViewById(R.id.fileUpdateButton);

        EditText nameInput = findViewById(R.id.newFileNameField);
        EditText contentInput = findViewById(R.id.newFileContentField);
        
        Utils.actionBarConfig(this, username);
        nameInput.setText(filename);

        String content;

        try {
            // openFileInput() only reads relative paths (https://stackoverflow.com/a/5963552/11848657)
            // Using Files.readAllBytes() instead (https://stackoverflow.com/a/326440/11848657)
            content = new String(Files.readAllBytes(oldFile.toPath()));
            contentInput.setText(content);
        } catch (Exception e) {
            Log.d("DEBUG_EXCEPTION", e.toString());
            Toast.makeText(this, "Failed retrieving file content. You can still update it.", Toast.LENGTH_LONG).show();
        }

        fileUpdateButton.setOnClickListener(v -> {
            Toast.makeText(this, "Hello", Toast.LENGTH_LONG);
            String newFilename = nameInput.getText().toString();
            String newContent = contentInput.getText().toString();

            // Make sure new file name is not empty.
            if (newFilename.isEmpty()) {
                Toast.makeText(this, "File name cannot be empty.", Toast.LENGTH_SHORT).show();
                return;
            }

            File newFile = new File(Utils.getAbsPath(this, newFilename));

            // Make sure the old file exists before modifying.
            if (!oldFile.exists()) {
                Toast.makeText(this, "File no longer exists.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Rename file.
            if (!oldFile.renameTo(newFile.getAbsoluteFile())) {
                Log.d("DEBUG_LOG", "Failed moving " + oldFile.getAbsolutePath() + "to" + newFile.getAbsoluteFile());
                Toast.makeText(this, "Failed renaming file.", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                // Update file content.
                Files.write(newFile.toPath(), newContent.getBytes());
            } catch (Exception e) {
                Log.d("DEBUG_EXCEPTION", e.toString());
                Toast.makeText(this, (newFilename.equals(filename)) ? "Failed updating file." : "File renamed but failed updating content.", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, "File updated successfully.", Toast.LENGTH_SHORT).show();
            finish();

        });
    }
}