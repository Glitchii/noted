package com.example.noted;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ScoreActivity extends GlobalAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.configure(R.layout.activity_score);

        int[] passedAndTotal = intent.getIntArrayExtra("passedAndTotal");
        int passed = passedAndTotal[0];
        int total = passedAndTotal[1];
        int stars = passed == 0 ? 0 : passed < total / 2 ? 1 : passed == total ? 3 : 2;

        TextView scoreRange = findViewById(R.id.scoreRange);
        LinearLayout scoreStars = findViewById(R.id.stars);

        scoreRange.setText(passed + " / " + total);

        for (int i = 0; i < stars; i++)
            scoreStars.getChildAt(i).setAlpha(1);


        Button tryAgainButton = findViewById(R.id.tryAgainButton);
        tryAgainButton.setOnClickListener(v -> startActivity(new Intent(ScoreActivity.this, QuizActivity.class)));
    }
}