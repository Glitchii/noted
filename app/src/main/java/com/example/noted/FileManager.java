package com.example.noted;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileManager extends AppCompatActivity {

    /**
     * Creates a new file using username and timestamp as the file name
     *
     * @param filesDir
     * @param username
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
     * @param context
     * @param filename
     * @return boolean indicating success or failure
     */
    public static boolean deleteFile(Context context, String filename) {
        File file = new File(FileManager.getCustomFilesDir(context), filename);
        return file.delete();
    }

    /**
     * Returns the 'custom_files' directory, creating it if it doesn't exist
     *
     * @param context
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
     * @param context
     * @param filename
     * @return The full file path
     */
    public static String getAbsPath(Context context, String filename) {
        File filesDir = FileManager.getCustomFilesDir(context);
        filesDir = new File(filesDir, filename);

        return filesDir.getAbsolutePath();
    }
}
