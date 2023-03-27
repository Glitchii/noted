package com.example.noted;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

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
     * Method to create a HashMap of valid usernames and passwords
     *
     * @return HashMap of usernames and passwords
     */
    public static HashMap<String, String> getAccountsMap() {
        HashMap<String, String> accounts = new HashMap<>();

        // Add ten default usernames and passwords to the HashMap
        accounts.put("john", "password");
        accounts.put("luke", "123");
        accounts.put("mark", "abc");
        accounts.put("james", "321");
        accounts.put("peter", "cba");
        accounts.put("mary", "pwd");
        accounts.put("jane", "asdf");
        accounts.put("joseph", "Joseph");
        accounts.put("joshua", "Joshua123");
        accounts.put("david", "abc123");

        return accounts;
    }

    /**
     * Returns the 'custom_files' directory, creating it if it doesn't exist.
     * The custom files directory is the directory where files created by
     * a user from {@link ManageActivity} are are stored.
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

    /**
     * Writes to file given the path to it and content
     *
     * @throws Exception
     */
    public static void writeToFile(File file, String content) throws Exception {
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(content.getBytes());
        fos.close();
    }

    /**
     * Creates a new file on the system
     *
     * @param file File object of the file to be created
     * @return Boolean output of (File.)createNewFile()
     */
    public static boolean createFile(File file) {
        boolean created = false;
        try {
            created = file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return created;
    }

    /**
     * Capitalises the first letter of a string and lowercases the rest
     *
     * @param text text to transform
     * @return transformed text
     */
    public static String cap(String text) {
        String char0 = text.substring(0, 1).toUpperCase();
        String otherChars = text.substring(1).toLowerCase();
        return char0 + otherChars;
    }
}
