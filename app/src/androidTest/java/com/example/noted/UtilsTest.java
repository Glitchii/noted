package com.example.noted;

import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

@RunWith(AndroidJUnit4.class)
public class UtilsTest {
    Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

    /**
     * Test returning existing or creating and returning custom files directory.
     * The custom files directory is the directory where files created by
     * a user from {@link ManageActivity} are are stored.
     */
    @Test
    public void getCustomFilesDirTest() {
        File custom_files_dir = Utils.getCustomFilesDir(appContext);

        // Test if folder exist. It should have been created from above if it didn't exist.
        assertTrue(custom_files_dir.exists());
    }

    /**
     * Test Utils.createFile method. 'deleteFileTest' test method replies on this one.
     * So this method should be called first to create the file to be deleted from 'deleteFileTest'
     */
    @Test
    public void createFileTest() {
        File fileToCreate = new File(Utils.getCustomFilesDir(appContext), "createFileTest");

        // Create the test file
        boolean createFile = Utils.createFile(fileToCreate);

        // Assert that file exists
        assertTrue(fileToCreate.exists());
    }

    /**
     * Uses Utils.deleteFile to delete file created from createFileTest.
     * This method tests Utils.deleteFile and relies on createFileTest which creates a file using Utils.deleteFile.
     */
    @Test
    public void deleteFileTest() {
        // Delete the test file. Utils.getCustomFilesDir is called for us in Utils.deleteFile
        boolean deleteFile = Utils.deleteFile(appContext, "createFileTest");

        // Assert that is deleted. 'deleteFile' is assigned a boolean value from Utils.deleteFile
        assertTrue(deleteFile);
    }
}