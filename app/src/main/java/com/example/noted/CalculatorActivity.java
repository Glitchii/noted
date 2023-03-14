package com.example.noted;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CalculatorActivity extends GlobalAppCompatActivity {
    EditText feedback;
    TextView calculations;
    LinearLayout keypad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.configure(R.layout.activity_calculator);

        feedback = findViewById(R.id.feedback);
        calculations = findViewById(R.id.calculations);
        keypad = findViewById(R.id.keypad);

        int color = getColor(R.color.calculator_display);
        // Change action bar background colour
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
        // Change status bar background colour
        getWindow().setStatusBarColor(color);
        // Change navigation bar background colour
        getWindow().setNavigationBarColor(getColor(R.color.calculator_background));
        // Disable keyboard for EditText (https://stackoverflow.com/a/57080982/11848657)
        feedback.setShowSoftInputOnFocus(false);

        // Add click event listeners to all keypad buttons
        for (int i = 0; i < keypad.getChildCount(); i++) {
            LinearLayout row = (LinearLayout) keypad.getChildAt(i);
            for (int j = 0; j < row.getChildCount(); j++) {
                Button button = (Button) row.getChildAt(j);
                button.setOnClickListener(v -> onButtonClick(button));
            }
        }

        // Event listener for delete button that deletes character at position of cursor in the EditText or everything selected
        findViewById(R.id.delete).setOnClickListener(v -> {
            int curStart = feedback.getSelectionStart();
            int curEnd = feedback.getSelectionEnd();
            int textLength = feedback.getText().length();

            if (textLength != 0)
                if (getCurrentFocus() != feedback)
                    // If cursor is not in the feedback box, delete last character
                    feedback.getText().delete(textLength - 1, textLength);
                else if (curStart != curEnd)
                    // Otherwise delete everything selected
                    feedback.getText().delete(curStart, curEnd);
                else if (curStart != 0)
                    // Otherwise delete character before the cursor position
                    feedback.getText().delete(curStart - 1, curStart);
        });
    }

    private void onButtonClick(Button button) {
        // Focus cursor at the end in the feedback field if it's not already there
        if (getCurrentFocus() != feedback)
            if (feedback.requestFocus())
                feedback.setSelection(feedback.getText().length());

        String buttonText = button.getText().toString();
        int curStart = feedback.getSelectionStart();
        int curEnd = feedback.getSelectionEnd();

        // User might be able to select right to left instead of other way around, so curStart and curEnd might be flipped.
        // I use Math.max and Math.min to ensure that it's in the right order
        int start = Math.min(curStart, curEnd);
        int end = Math.max(curStart, curEnd);

        // Clear output feedback if AC button is pressed
        if (buttonText.equals("AC")) {
            feedback.getText().clear();
        }

        // If brace button is clicked, we need to figure out which brace to add and where
        else if (buttonText.equals("( )")) {
            String textFromStartToCursor = feedback.getText().toString().substring(0, start);
            if (textFromStartToCursor.length() < 1)
                textFromStartToCursor = feedback.getText().toString();

            // If the last character is an open parentheses, add additional open parentheses
            if (textFromStartToCursor.charAt(textFromStartToCursor.length() - 1) == '(')
                buttonText = "(";
            else {
                int openParentheses = 0, closedParentheses = 0;

                for (int i = 0; i < textFromStartToCursor.length(); i++)
                    if (textFromStartToCursor.charAt(i) == '(')
                        openParentheses++;
                    else if (textFromStartToCursor.charAt(i) == ')')
                        closedParentheses++;

                buttonText = openParentheses > closedParentheses ? ")" : "(";
            }

            // Add the new buttonText string to position of cursor or to end if cursor wasn't successfully focussed in EditText
            if (getCurrentFocus() == feedback) feedback.getText().insert(start, buttonText);
            else feedback.append(buttonText);
        }

        // At this point user has either pressed a number of an operator, we just need to append it.
        // If cursor is not in the feedback box, append to the end
        else if (getCurrentFocus() != feedback) feedback.append(buttonText);
        else feedback.getText().replace(start, end, buttonText);
    }
}