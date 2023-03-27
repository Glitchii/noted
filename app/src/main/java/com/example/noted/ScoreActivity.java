package com.example.noted;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.io.File;

public class ScoreActivity extends GlobalAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.configure(R.layout.activity_score);

        String activity = intent.getStringExtra("activity");
        int[] passedAndTotal = intent.getIntArrayExtra("passedAndTotal");
        int passed = passedAndTotal[0];
        int total = passedAndTotal[1];
        // Multiplying the percentage (passed / total) by 3, to scale the range 0 to 1 (the range of percentages) to 0 to 3 (the range of stars).
        int stars = Math.round((float) passed / total * 3);

        TextView scoreRange = findViewById(R.id.scoreRange);
        LinearLayout scoreStars = findViewById(R.id.stars);

        scoreRange.setText(passed + " / " + total);

        for (int i = 0; i < stars; i++)
            scoreStars.getChildAt(i).setAlpha(1);

        Button tryAgainButton = findViewById(R.id.tryAgainButton);
        Button saveToFileButton = findViewById(R.id.saveScore);
        ConstraintLayout buttonsLayout = findViewById(R.id.buttonsLayout);

        tryAgainButton.setOnClickListener(v -> {
            if (activity.equals("LanguageActivity"))
                // If the user is coming from the language activity, go back to the language
                // activity
                startActivity(new Intent(ScoreActivity.this, LanguageActivity.class));
            else
                // Otherwise, go back to the quiz activity
                startActivity(new Intent(ScoreActivity.this, QuizActivity.class));
        });

        // Results are only saved to file if the user is coming from the quiz activity
        if (activity.equals("QuizActivity"))
            saveToFileButton.setOnClickListener(v -> {
                try {
                    String fileName = "scores.txt", fileContent;
                    File file = new File(getFilesDir(), fileName);

                    Utils.createFile(file);
                    fileContent = Utils.readFile(file);

                    if (fileContent.length() > 0)
                        fileContent = fileContent.trim() + ", ";

                    fileContent += username + ": " + passed + " / " + total + "\n";
                    Utils.writeToFile(file, fileContent);
                    Toast.makeText(this, "Saved to " + file.getPath(), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(this, "Failed saving to file", Toast.LENGTH_LONG).show();
                }
            });

        else {
            // Remove the save to file button
            saveToFileButton.setVisibility(Button.GONE);

            // Set the layout width of it's parent (buttonsLayout) to wrap content
            // This should place the remaining "try again" button in the center horizontally
            buttonsLayout.getLayoutParams().width = ConstraintLayout.LayoutParams.WRAP_CONTENT;
            buttonsLayout.requestLayout();
        }

    }
}