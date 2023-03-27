package com.example.noted;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class CalculatorActivity extends GlobalAppCompatActivity {
    EditText feedback;
    TextView calculations;
    LinearLayout keypad;
    ArrayList<String> previousCalculations = new ArrayList<>();
    // Evaluation is done with ScriptEngineManager. User can only enter numbers and other items on the keypad as
    // keyboard will be disabled and non-arithmetic characters will not be evaluated, so they can't inject code. See evaluate()
    // https://stackoverflow.com/a/2605051/11848657, https://github.com/APISENSE/rhino-android.
    ScriptEngine engine = new ScriptEngineManager().getEngineByName("js");

    /**
     * Recursive method to turn arithmetic inside braces into absolute numbers
     * <br><br>
     * Eg. (2*7) becomes 14 and 31+((4+1)+2*7)*2 becomes 31+19*2 <br>
     *
     * @param expr The mathematical expression to evaluate.
     * @return The result of the expression.
     * @throws ScriptException if the expression is invalid or contains unsupported operators.
     */
    public String evalBraces(String expr) throws ScriptException {
        // Check if expression contains braces
        if (expr.contains("(")) {
            // Find the innermost set of braces
            int left = expr.lastIndexOf("(");
            int right = left + expr.substring(left).indexOf(")");
            // Evaluate the expression within the braces
            double result = (double) engine.eval(expr.substring(left + 1, right));
            // Replace the expression within the braces with its result
            expr = expr.substring(0, left) + result + expr.substring(right + 1);
            // Recursively evaluate any remaining expressions within braces
            expr = evalBraces(expr);
        }
        // Evaluate the final expression
        return expr;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.topSectionMatchesTheme = false;
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
        // Focus into EditText
        focusInto(feedback);

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

            if (textLength != 0) {
                if (getCurrentFocus() != feedback)
                    // If cursor is not in the feedback box, delete last character
                    feedback.getText().delete(textLength - 1, textLength);
                else if (curStart != curEnd)
                    // Otherwise delete everything selected
                    feedback.getText().delete(curStart, curEnd);
                else if (curStart != 0)
                    // Otherwise delete character before the cursor position
                    feedback.getText().delete(curStart - 1, curStart);

                // Evaluate results while typing
                evaluate();
            }
        });


        // History button to show previous calculations and remove them from the list
        findViewById(R.id.history).setOnClickListener(v -> {
            if (previousCalculations.size() < 1) {
                Toast.makeText(this, "No previous calculations from current session. Press = to add.", Toast.LENGTH_SHORT).show();
                return;
            }

            feedback.setText(previousCalculations.get(0));
            previousCalculations.remove(0);
            focusInto(feedback);
        });
    }

    private void onButtonClick(Button button) {
        int curStart = feedback.getSelectionStart();
        int curEnd = feedback.getSelectionEnd();

        // User might be able to select right to left instead of other way around, so curStart and curEnd might be flipped.
        // I use Math.max and Math.min to ensure that it's in the right order
        int start = Math.min(curStart, curEnd);
        int end = Math.max(curStart, curEnd);

        String buttonText = button.getText().toString().replace('·', '.');

        // Clear output feedback if AC button is pressed
        if (buttonText.equals("AC")) {
            feedback.getText().clear();
            calculations.setText("");
            return;
        }

        // Clear output feedback if AC button is pressed
        if (buttonText.equals("=")) {
            // See doc strings for difference between evaluate() and forceEvaluate()
            forceEvaluate();
            return;
        }

        // If brace button is clicked, we need to figure out which brace to add and where
        if (buttonText.equals("( )")) {
            String textFromStartToCursor = feedback.getText().toString().substring(0, start);

            // If the last character is an open parentheses, add additional open parentheses
            if (textFromStartToCursor.length() < 1 || textFromStartToCursor.charAt(textFromStartToCursor.length() - 1) == '(')
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

        evaluate();
    }

    private void focusInto(EditText editableView) {
        int start = editableView.getText().length();
        if (editableView.requestFocus())
            editableView.setSelection(start);
    }

    /**
     * Evaluates expressions and displays the result in the 'calculations' TextView field.
     * If the expression is invalid, the calculations field will be cleared.
     * Method is called when a button is pressed, unlike forceEvaluate() which is called only when equals button is pressed.
     */
    private void evaluate() {
        try {

            String text = feedback.getText().toString();

            // Check if input contains only numbers and operators before proceeding.
            if (!text.matches("^[0-9×÷\\-+^%()\\.]*$"))
                // The EditText view has its keyboard disabled to prevent users from entering invalid characters,
                // but it's still possible for them to copy and paste text, or use a physical keyboard (etc.) to input
                // characters that are not allowed. Doesn't make sense for them to do this but we'll handle it anyway.
                // Without this check, an input like 'true ? 1 : 0' would evaluate to 1.
                throw new Exception();

            // Replace out all operators with their corresponding script operators
            text = text
                    .replace('×', '*')
                    .replace('÷', '/')
                    .replace('·', '.')
                    .replace("%", "/100.0*"); // % is for percentage not modulo

            // We use 'evalBraces' method eval content is braces first, and replace everything including the braces with the result first
            // so that it's easier to replace some operators eg. ^ with their corresponding functions eg. Math.pow
            text = evalBraces(text)
                    // Replace ^ with Math.pow function using with regular expression groups (https://docs.oracle.com/javase/tutorial/essential/regex/groups.html, https://docs.oracle.com/javase/tutorial/essential/regex/pattern.html)
                    .replaceAll("(-?[.0-9]+)\\^(-?[.0-9]+)", "Math.pow($1, $2)") // Regex tested at https://regex101.com/r/vti1r5/3
                    // 5^2^5 would resulted to Math.pow(5, 2)^5 instead of Math.pow(Math.pow(5, 2), 5), so we need to do it again
                    .replaceAll("(Math.pow\\(-?[.0-9]+, *-?[.0-9]+\\))\\^(-?[.0-9]+)", "Math.pow($1, $2)");

            if (text.length() < 1)
                // Exception thrown below will be caught to clear the calculation field
                throw new IllegalArgumentException(); // RuntimeException?

            Double results = (Double) engine.eval(text);
            if (results == null)
                throw new NullPointerException();
            else if (results.toString().equals("Infinity") || results.toString().equals("NaN"))
                // Text could be "Infinity" if user tries to divide by zero
                // It could be "NaN" if user finds a way to inject letters into the expression
                throw new RuntimeException(); // ArithmeticException?
            // Show evaluation with the unnecessary .0 stripped from the end
            calculations.setText(String.valueOf(results).replaceFirst("\\.0$", ""));
        } catch (Exception e) {
            // Clear calculation field
            calculations.setText("");
        }
    }

    /**
     * Evaluates the expression only when the equals button is pressed unlike evaluate().
     * Error messages are shown if the expression is invalid.
     *
     * @see #evaluate()
     */
    private void forceEvaluate() {
        // Calling evaluate() should already show output in smaller result section
        evaluate();

        if (calculations.getText().equals("")) {
            calculations.setText("Invalid input");
            return;
        }

        // Append current input to previousCalculations
        previousCalculations.add(feedback.getText().toString());
        // Show output in feedback section
        feedback.setText(calculations.getText());
        // Clear the calculations section
        calculations.setText("");
        focusInto(feedback);
    }
}