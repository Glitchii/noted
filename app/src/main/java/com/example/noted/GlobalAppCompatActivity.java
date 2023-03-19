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
 * <li>Loads configs eg. custom background set by user and applies to page</li>
 * <li>Sets up action bar with title and background colour if needed.</li>
 * <li><i>etc.</i></li>
 */
public class GlobalAppCompatActivity extends AppCompatActivity {
    protected Intent intent;
    protected boolean loginRequired = true, topSectionMatchesTheme = true;
    protected String username, chosenBackground, defaultBackground = "#1B1A1D";

    /**
     * Sets action bar text and background colour.
     *
     * @return ActionBar
     */
    private ActionBar actionBarConfig(AppCompatActivity activity, String username) {
        ActionBar actionBar = activity.getSupportActionBar();
        activity.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(activity.getColor(R.color.uiBackground)));
        actionBar.setTitle(username);

        return actionBar;
    }

    /**
     * Loads layout and configures the action bar if necessary, username, etc.
     *
     * @param layout ID of layout resource to load
     */
    protected void configure(int layout) {
        setContentView(layout);
        configure();
    }

    /**
     * Replaces activity pressing back button from newActivityClass doesn't go to currentActivityContext
     *
     * @param currentActivityContext eg. X.this
     * @param newActivityClass       eg. Y.class
     */
    protected void replaceActivity(Context currentActivityContext, Class newActivityClass) {
        Intent newIntent = new Intent(currentActivityContext, newActivityClass);
        // Set flags to replace currentActivityContext without saving stack history (developer.android.com/reference/android/content/Intent#constants_1)
        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(newIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkConfigs();

        if (chosenBackground == null)
            setBackground(defaultBackground);
    }

    private void checkConfigs() {
        if (loginRequired)
            try {
                // Check whether any user is still logged in.
                username = getConfigValue("loggedInUser");
                if (username != null)
                    // Capitalise the first letter of the username
                    username = username.substring(0, 1).toUpperCase() + username.substring(1);
                else {
                    // Start login activity if no one is logged in.
                    toLoginActivity();
                    return;
                }

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

    protected boolean saveCofig(String key, String value) {
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

    protected void logout() {
        deleteConfig("loggedInUser");
        toLoginActivity();
    }

    protected void deleteConfig(String key) {
        File file = new File(getFilesDir(), '.' + key);
        if (file.exists())
            file.delete();
    }

    protected String getConfigValue(String key) throws Exception {
        File file = new File(getFilesDir(), '.' + key);
        return file.exists() ? Utils.readFile(file) : null;
    }

    protected void toLoginActivity() {
        replaceActivity(this, LoginActivity.class);
    }

    protected void setBackground(String hex) {
        int color = Color.parseColor(hex);
        ColorDrawable colorDrawable = new ColorDrawable(color);
        View root = findViewById(R.id.root);

        if (root == null)
            return;

        // Change background colour
        root.setBackgroundColor(color);
        // Change navigation bar background colour
        getWindow().setNavigationBarColor(color);

        if (topSectionMatchesTheme) {
            // Change action bar background colour
            getSupportActionBar().setBackgroundDrawable(colorDrawable);
            // Change status bar background colour
            getWindow().setStatusBarColor(color);
        }
    }

    /**
     * Configures without loading layout
     *
     * @see #configure(int layout)
     */
    protected void configure() {
        // Required to get data from previous intents
        intent = getIntent();
        // Retrieve username from intent/storage and configure it for the activity.
        checkConfigs();
        // Configure action bar colour and background
        actionBarConfig(this, username);
    }

    /**
     * Fade in animation
     *
     * @param v View to fade in
     * @see #fadeIn(View v, int duration)
     */
    protected void fadeIn(View v) {
        fadeIn(v, 1);
    }

    protected void fadeOut(View v) {
        fadeOut(v, 500);
    }

    /**
     * Fade in animation (https://stackoverflow.com/a/36660424/11848657)
     *
     * @param v        View to fade in
     * @param duration duration
     */
    protected void fadeIn(View v, int duration) {
        fadeIn(v, 1, duration);
    }

    protected void fadeIn(View v, float maxAlpha) {
        fadeIn(v, maxAlpha, 500);
    }

    protected void fadeOut(View v, int duration) {
        v.animate().alpha(0f).setDuration(duration).start();
    }

    protected void fadeIn(View v, float maxAlpha, int duration) {
        v.animate().alpha(maxAlpha).setDuration(duration).start();
    }
}
