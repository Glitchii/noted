package com.example.noted;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Inherited by Activity classes, inherits AppCompatActivity.
 * <li>Checks if user is logged in before rendering views.</li>
 * <li>Sets up action bar if needed.</li>
 * ...
 */
public class GlobalAppCompatActivity extends AppCompatActivity {
    protected String username;
    protected Intent intent;

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
        else
            checkLogin();

        // Capitalise the first letter of the username
        Log.d("DEBUG_USERNAME", username);
        username = username.substring(0, 1).toUpperCase() + username.substring(1);
        // Configure action bar
        actionBarConfig(this, username);

        Log.d("DEBUG_CREATED", "Activity created");
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkLogin();
    }

    /**
     * Checks whether any user is still logged in.
     * Useful when starting an activity that requires a login.
     * Starts login activity if no one is logged in.
     */
    private void checkLogin() {
        try {
            username = getLoggedInUser();
            if (username == null)
                toLoginActivity();
        } catch (Exception e) {
            Log.d("DEBUG_LOGIN_SAVE", e.toString());
            Toast.makeText(this, "Failed finding who is logged in", Toast.LENGTH_SHORT).show();
            toLoginActivity();
        }
    }

    public void saveLogin(String username) {
        // Could use Shared-Preferences (https://developer.android.com/training/data-storage/shared-preferences)
        // Instead of creating a custom file but no need for a small project like this
        // since this also works.

        try {
            FileOutputStream fos = openFileOutput(".loggedInUser", Context.MODE_PRIVATE);
            fos.write(username.getBytes());
            fos.close();
        } catch (Exception e) {
            Log.d("DEBUG_LOGIN", e.toString());
            Toast.makeText(this, "Failed saving login.", Toast.LENGTH_LONG).show();
        }
    }

    public void logout() {
        File file = new File(getFilesDir(), ".loggedInUser");
        if (file.exists())
            file.delete();

        toLoginActivity();
    }

    public String getLoggedInUser() throws Exception {
        File file = new File(getFilesDir(), ".loggedInUser");
        return file.exists() ? Utils.readFile(file) : null;
    }

    public boolean loggedInAs(String username) throws Exception {
        String content = getLoggedInUser();
        return content != null && content.equals(username);
    }

    private void toLoginActivity() {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        //developer.android.com/reference/android/content/Intent
        //developer.android.com/reference/android/content/Intent#constants_1
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginIntent);
    }
}
