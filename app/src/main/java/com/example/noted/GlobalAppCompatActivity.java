package com.example.noted;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Inherited by Activity classes, inherits AppCompatActivity.
 * <li>Checks if user is logged in before rendering views.</li>
 * <li>Sets up action bar if needed.</li>
 * ...
 */
public class GlobalAppCompatActivity extends AppCompatActivity {
    protected Intent intent;
    protected String username, chosenBackground, defaultBackground = "#1B1A1D";

    /**
     * Sets action bar text and background colour.
     *
     * @return ActionBar
     */
    public ActionBar actionBarConfig(AppCompatActivity activity, String username) {
        ActionBar actionBar = activity.getSupportActionBar();
        activity.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(activity.getColor(R.color.uiBackground)));
        actionBar.setTitle(username);

        return actionBar;
    }

    /**
     * Loads layout and configures the action bar if necessary.
     *
     * @param layout ID of layout resource to load
     */
    protected void configure(int layout) {
        setContentView(layout);

        intent = getIntent();
        username = intent.getStringExtra("username");

        if (username != null)
            saveLogin(username);

        checkConfigs();

        // Capitalise the first letter of the username
        username = username.substring(0, 1).toUpperCase() + username.substring(1);
        // Configure action bar
        actionBarConfig(this, username);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkConfigs();

        if (chosenBackground == null)
            setBackground(defaultBackground);
    }

    private void checkConfigs() {
        try {
            // Check whether any user is still logged in.
            username = getConfigValue("loggedInUser");
            if (username == null)
                // Starts login activity if no one is logged in.
                toLoginActivity();
        } catch (Exception e) {
            Log.d("DEBUG_LOGIN_SAVE", e.toString());
            Toast.makeText(this, "Failed finding who is logged in", Toast.LENGTH_SHORT).show();
            toLoginActivity();
        }

        try {
            // Check whether a custom background was set colour
            chosenBackground = getConfigValue("background"); // #RRGGBB
            if (chosenBackground != null)
                setBackground(chosenBackground);
        } catch (Exception e) {
            Snackbar.make(findViewById(android.R.id.content), e.toString(), Snackbar.LENGTH_LONG);
            Toast.makeText(this, chosenBackground, Toast.LENGTH_SHORT).show();
        }
    }

    public void saveLogin(String username) {
        saveCofig("loggedInUser", username);
    }

    public boolean saveCofig(String key, String value) {
        // Could use Shared-Preferences (https://developer.android.com/training/data-storage/shared-preferences)
        // Instead of creating a custom file but no need for a small project like this since this also works.

        try {
            FileOutputStream fos = openFileOutput('.' + key, Context.MODE_PRIVATE);
            fos.write(value.getBytes());
            fos.close();
        } catch (Exception e) {
            Log.d("DEBUG_LOGIN", e.toString());
            Toast.makeText(this, "Failed saving info.", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    public void logout() {
        deleteConfig("loggedInUser");
        toLoginActivity();
    }

    public void deleteConfig(String key) {
        File file = new File(getFilesDir(), '.' + key);
        if (file.exists())
            file.delete();
    }

    public String getConfigValue(String key) throws Exception {
        File file = new File(getFilesDir(), '.' + key);
        return file.exists() ? Utils.readFile(file) : null;
    }

    private void toLoginActivity() {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        //developer.android.com/reference/android/content/Intent
        //developer.android.com/reference/android/content/Intent#constants_1
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginIntent);
    }

    protected void setBackground(String hex) {
        View root = findViewById(R.id.root);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor(hex));

        if (root == null)
            return;

        // Change background colour
        root.setBackground(colorDrawable);
        // Change action bar background colour
        getSupportActionBar().setBackgroundDrawable(colorDrawable);
        // Change status bar background colour
        getWindow().setStatusBarColor(Color.parseColor(hex));
        // Change navigation bar background colour
        getWindow().setNavigationBarColor(Color.parseColor(hex));
    }
}
