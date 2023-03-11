package com.example.noted;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.widget.EditText;

public class BackgroundSwitcherActivity extends GlobalAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.configure(R.layout.activity_background_switcher);

        EditText hexBox = findViewById(R.id.hexBox);

        // Focus into hex box and show keyboard after 200ms (https://stackoverflow.com/a/7784904/11848657)
        new Handler().postDelayed(new Runnable() {
            public void run() {
                // Focus into input field. Might not show keyboard
                hexBox.requestFocus();
                // Additional code to show the keyboard
                hexBox.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0f, 0f, 0));
                hexBox.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0f, 0f, 0));
            }
        }, 200);
    }
}