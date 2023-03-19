package com.example.noted;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class Utils {

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

    public static String readFile(File file) throws Exception {
        // openFileInput() only reads relative paths (https://stackoverflow.com/a/5963552/11848657)
        // Files.readAllBytes() is short and works (https://stackoverflow.com/a/326440/11848657)
        // But might not be great option when reading large files.
        // Using BufferedReader is more Efficient (https://mkyong.com/java/java-how-to-read-a-file/)
        // Especially when used with StringBuilder().append() instead of (+)

        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;

        while ((line = reader.readLine()) != null)
            stringBuilder.append(line);

        reader.close();
        return stringBuilder.toString();
    }

    public static void writeToFile(File file, String content) throws Exception {
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(content.getBytes());
        fos.close();
    }

    /**
     * Creates a new file on the system
     *
     * @return Boolean output of createNewFile()
     */
    public static boolean createFile(File filesDir, String fileName) {
        boolean created = false;
        try {
            created = new File(filesDir, fileName).createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return created;
    }
}
