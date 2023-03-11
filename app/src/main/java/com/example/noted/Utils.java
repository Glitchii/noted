package com.example.noted;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    /**
     * Creates a new file using username and timestamp as the file name
     *
     * @return The new file name
     */
    public static String createFile(File filesDir, String username) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
        String fileName = username + " " + timestamp;

        try {
            new File(filesDir, fileName).createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return fileName;
    }

    /**
     * Deletes a file from the 'custom_files' directory
     *
     * @return boolean indicating success or failure
     */
    public static boolean deleteFile(Context context, String filename) {
        File file = new File(Utils.getCustomFilesDir(context), filename);
        return file.delete();
    }

    /**
     * Returns the 'custom_files' directory, creating it if it doesn't exist
     *
     * @return The 'custom_files' directory
     */
    public static File getCustomFilesDir(Context context) {
        File filesDir = new File(context.getFilesDir(), "custom_files");

        // Create 'custom_files' folder where files will be created
        if (!filesDir.exists())
            filesDir.mkdir();

        return filesDir;
    }

    /**
     * Expands the file name to the absolute path
     *
     * @return The full file path
     */
    public static String getAbsPath(Context context, String filename) {
        File filesDir = Utils.getCustomFilesDir(context);
        filesDir = new File(filesDir, filename);

        return filesDir.getAbsolutePath();
    }

    /**
     * Sets action bar text and background colour.
     *
     * @return ActionBar
     */
    public static ActionBar actionBarConfig(AppCompatActivity activity, String username) {
        ActionBar actionBar = activity.getSupportActionBar();
        activity.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(activity.getColor(R.color.uiBackground)));
        actionBar.setTitle(username);

        return actionBar;
    }
}
