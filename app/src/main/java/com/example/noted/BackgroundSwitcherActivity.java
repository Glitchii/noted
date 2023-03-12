package com.example.noted;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class BackgroundSwitcherActivity extends GlobalAppCompatActivity {
    private TextView resetButton;
    private boolean resetButtonShown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.configure(R.layout.activity_background_switcher);

        EditText hexBox = findViewById(R.id.hexBox);
        LinearLayout suggestionsBox = findViewById(R.id.suggestionsBox);
        resetButton = findViewById(R.id.resetButton);

        // Focus into hex box and show keyboard after 200ms (https://stackoverflow.com/a/7784904/11848657)
        new Handler().postDelayed(() -> {
            // Focus into input field. Might not show keyboard
            hexBox.requestFocus();
            // Additional code to show the keyboard
            hexBox.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0f, 0f, 0));
            hexBox.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0f, 0f, 0));


        }, 2500);

        new Handler().postDelayed(() -> Toast.makeText(this, "Colour persists after app restarts", Toast.LENGTH_SHORT).show(), 200);

        if (chosenBackground != null)
            setResetButtonAlpha(1f);

        hexBox.addTextChangedListener(new TextWatcher() {
            // https://developer.android.com/reference/android/text/TextWatcher
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString();
                if (text.length() > 0 && !text.startsWith("#")) {
                    hexBox.setText("#" + text);
                    hexBox.setSelection(hexBox.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                if (text.matches("#[0-9a-fA-F]{6}")) {
                    saveBackground(text);
                }
            }
        });

        for (int i = 0; i < suggestionsBox.getChildCount(); i++) {
            View view = suggestionsBox.getChildAt(i);

            view.setOnClickListener(l -> {
                if (view.getId() == R.id.cs1)
                    saveBackground("#131714");
                else if (view.getId() == R.id.cs2)
                    saveBackground("#191211");
                else if (view.getId() == R.id.cs3)
                    saveBackground("#171721");
                else if (view.getId() == R.id.cs4)
                    saveBackground("#181406");
            });
        }
    }

    protected void saveBackground(String hex) {
        super.setBackground(hex);

        // Save colour to be used in other activities and when app is restarted
        if (hex != defaultBackground)
            saveCofig("background", hex);
        else
            deleteConfig("background");

        // Show reset button
        if (!resetButtonShown) {
            // Fade in animation (https://stackoverflow.com/a/36660424/11848657)
            setResetButtonAlpha(1f);
            resetButtonShown = true;
        }

        resetButton.setOnClickListener(v -> {
            saveBackground(defaultBackground);
            resetButtonShown = false;
            setResetButtonAlpha(0f);
            deleteConfig("background");
        });
    }

    private void setResetButtonAlpha(float v) {
        resetButton.animate().alpha(v).setDuration(500).start();
    }
}