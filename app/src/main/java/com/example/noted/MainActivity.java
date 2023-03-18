package com.example.noted;

import android.os.Bundle;

public class MainActivity extends GlobalAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.configure();

        if (username == null)
            // If a username is not available, it means no one is logged in.
            replaceActivity(MainActivity.this, LoginActivity.class);
        else
            // Otherwise if someone is logged in, we skip login page util they logout.
            replaceActivity(MainActivity.this, QuizActivity.class);
    }
}