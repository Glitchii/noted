package com.example.noted;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class ManageActivity extends GlobalAppCompatActivity {
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
        Class activity = null;

        if (id == R.id.action_logout)
            logout();
        else if (id == R.id.action_quiz)
            activity = QuizActivity.class;
        else if (id == R.id.action_dice_roller)
            activity = DiceRollerActivity.class;
        else if (id == R.id.action_background_changer)
            activity = BackgroundSwitcherActivity.class;
        else if (id == R.id.action_calculator)
            activity = CalculatorActivity.class;

        if (activity != null)
            startActivity(new Intent(ManageActivity.this, activity));

        return super.onOptionsItemSelected(item);
    }

    public void setUpFileCardModels() {
        File[] files = Utils.getCustomFilesDir(this).listFiles();
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
        super.configure(R.layout.activity_manage);

        // Setup file list RecyclerView
        fileCardRecyclerViewAdapter = loadFileCards();

        // Floating 'add' action button setup
        findViewById(R.id.fab).setOnClickListener(v -> {
            // Create new file on system
            String fileName = Utils.createFile(Utils.getCustomFilesDir(this), username);
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