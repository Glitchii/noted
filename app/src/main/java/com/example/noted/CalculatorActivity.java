package com.example.noted;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CalculatorActivity extends GlobalAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.configure(R.layout.activity_calculator);

        EditText feedback = findViewById(R.id.feedback);
        TextView calculations = findViewById(R.id.calculations);
        LinearLayout keypad = findViewById(R.id.keypad);
        int color = getColor(R.color.calculator_display);

        // Change action bar background colour
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
        // Change status bar background colour
        getWindow().setStatusBarColor(color);
        // Change navigation bar background colour
        getWindow().setNavigationBarColor(getColor(R.color.calculator_background));
        // Disable keyboard (https://stackoverflow.com/a/57080982/11848657)
        feedback.setShowSoftInputOnFocus(false);

        for (int i = 0; i < keypad.getChildCount(); i++) {
            LinearLayout row = (LinearLayout) keypad.getChildAt(i);
            for (int j = 0; j < row.getChildCount(); j++) {
                Button button = (Button) row.getChildAt(j);

                button.setOnClickListener(v -> {
                    Toast.makeText(this, button.getText() + " clicked", Toast.LENGTH_SHORT).show();
                });
            }
        }
    }
}