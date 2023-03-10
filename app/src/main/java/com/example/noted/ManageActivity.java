package com.example.noted;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class ManageActivity extends AppCompatActivity {
    ArrayList<FileCardModel> fileCardModels = new ArrayList<>();
    FileCardRecyclerViewAdapter fileCardRecyclerViewAdapter;
    private boolean firstActivityLoad = true;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflates the menu by adding menu items to action bar.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_dice_roller) {
            Toast.makeText(this, "Dice roller", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    public void setUpFileCardModels() {
        File[] files = FileManager.getCustomFilesDir(this).listFiles();
        TextView noFilesText = findViewById(R.id.noFilesText);
        RecyclerView fileList = findViewById(R.id.fileList);

        // Clear the ArrayList and RecyclerView
        fileCardModels.clear();
        fileList.removeAllViews();

        // Loop through all files in 'files' dir to add their models to the ArrayList
        for (File file : files)
            if (file.isFile()) {
                String fileName = file.getName();
                String fileSize = file.length() + " bytes";

                fileCardModels.add(new FileCardModel(fileName, fileSize));
            }

        toggleNoFilesText();
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
        Toast.makeText(this, "Logged in as " + username, Toast.LENGTH_SHORT).show();

        // Configure RecyclerView
        fileCardRecyclerViewAdapter = loadFileCards();

        // Floating action button setup
        String finalUsername = username;
        findViewById(R.id.fab).setOnClickListener(v -> {
            // Create new file on system
            String fileName = FileManager.createFile(FileManager.getCustomFilesDir(this), finalUsername);
            // Add filename and file size to ArrayList and update RecyclerView UI
            fileCardModels.add(new FileCardModel(fileName, "0 bytes"));
            fileCardRecyclerViewAdapter.notifyDataSetChanged();
            toggleNoFilesText();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (firstActivityLoad)
            firstActivityLoad = false;
        else
            // Reload file list on resume
            fileCardRecyclerViewAdapter = loadFileCards();
    }

    /**
     * Loads the file cards into the RecyclerView
     *
     * @return FileCardRecyclerViewAdapter
     * @see FileCardRecyclerViewAdapter
     * @see FileCardModel
     */
    private FileCardRecyclerViewAdapter loadFileCards() {
        // File list recycler view setup
        setUpFileCardModels();

        // Set up RecyclerView
        RecyclerView fileCardRecyclerView = findViewById(R.id.fileList);
        FileCardRecyclerViewAdapter fileCardRecyclerViewAdapter = new FileCardRecyclerViewAdapter(this, fileCardModels);
        fileCardRecyclerView.setAdapter(fileCardRecyclerViewAdapter);
        fileCardRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        fileCardRecyclerViewAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                toggleNoFilesText();
            }
        });

        return fileCardRecyclerViewAdapter;
    }

    /**
     * Sets the visibility of the 'no files' text based on the number of files in
     * the ArrayList
     */
    public void toggleNoFilesText() {
        // Set default text and RecyclerView visibility based on whether there are files
        TextView noFilesText = findViewById(R.id.noFilesText);
        RecyclerView fileList = findViewById(R.id.fileList);

        noFilesText.setVisibility(fileCardModels.size() > 0 ? TextView.GONE : TextView.VISIBLE);
        fileList.setVisibility(fileCardModels.size() > 0 ? RecyclerView.VISIBLE : RecyclerView.GONE);

        if (fileCardModels.size() > 0)
            // Smooth scroll to the bottom of the RecyclerView
            fileList.smoothScrollToPosition(fileCardModels.size() - 1);
    }
}