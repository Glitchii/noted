package com.example.noted;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

public class FileEditorActivity extends GlobalAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.configure(R.layout.activity_file_editor);

        String filename = intent.getStringExtra("filename");
        File oldFile = new File(Utils.getAbsPath(this, filename));
        Button fileUpdateButton = findViewById(R.id.fileUpdateButton);
        EditText nameInput = findViewById(R.id.newFileNameField);
        EditText contentInput = findViewById(R.id.newFileContentField);

        nameInput.setText(filename);

        try {
            contentInput.setText(Utils.readFile(oldFile));
        } catch (Exception e) {
            Toast.makeText(this, "Failed retrieving file content. You can still update it.", Toast.LENGTH_LONG).show();
        }

        fileUpdateButton.setOnClickListener(v -> {
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

            if (!oldFile.renameTo(newFile.getAbsoluteFile())) {
                Toast.makeText(this, "Failed renaming file.", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                Utils.writeToFile(newFile, newContent);
            } catch (Exception e) {
                Toast.makeText(this, (newFilename.equals(filename)) ? "Failed updating file." : "File renamed but failed updating content.", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, "File updated successfully.", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}