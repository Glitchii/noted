package com.example.noted;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

public class LanguageActivity extends GlobalAppCompatActivity {
    float disabledAlpha = .3f;
    int currentQuestionIndex, exercisesPassed = 0;
    LanguageQuestionModel currentQuestion;
    ArrayList<LanguageQuestionModel> exercises = new ArrayList<>();
    TextView questionText, answerText, wrongAnswerText;
    Button nextButton, wrongNextButton, correctNextButton;
    LinearLayout wordsLayout;
    ConstraintLayout correctLayout, wrongLayout, rangeBackground, rangeSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.configure(R.layout.activity_language);

        questionText = findViewById(R.id.questionText);
        answerText = findViewById(R.id.answerText);
        wrongAnswerText = findViewById(R.id.wrongAnswerText);
        nextButton = findViewById(R.id.nextButton);
        wrongNextButton = findViewById(R.id.wrongNextButton);
        correctNextButton = findViewById(R.id.correctNextButton);
        wordsLayout = findViewById(R.id.wordsLayout);
        correctLayout = findViewById(R.id.correctLayout);
        wrongLayout = findViewById(R.id.wrongLayout);
        rangeBackground = findViewById(R.id.rangeBackground);
        rangeSlider = findViewById(R.id.rangeSlider);

        nextButton.setOnClickListener(v -> checkAnswer());

        // When next button is clicked in the 'wrong answer' layout box, hide the layout box and show a new question
        wrongNextButton.setOnClickListener(v -> {
            wrongLayout.setVisibility(ConstraintLayout.GONE);
            newQuestion();
        });

        correctNextButton.setOnClickListener(v -> {
            correctLayout.setVisibility(ConstraintLayout.GONE);
            newQuestion();
        });

        // Add some exercises to the exercises array
        exercises.add(new LanguageQuestionModel("We are going to the movies tonight", "Nous allons au cinéma ce soir", new String[]{"nous", "manger", "au", "cinéma", "tu", "allons", "ce", "soir", "pizza"}));
        exercises.add(new LanguageQuestionModel("She is drinking coffee at the cafe", "Elle boit du café au café", new String[]{"la", "café", "elle", "boit", "du", "au", "chaque", "à", "café"}));
        exercises.add(new LanguageQuestionModel("You are adopted", "Tu es adopté", new String[]{"ce", "adopter", "je", "adopté", "en", "tu", "es", "adaptateur", "du"}));

        newQuestion();
    }

    private void newQuestion() {
        // Reset navigation bar colour to default
        setBackground(chosenBackground == null ? defaultBackground : chosenBackground);

        // Check if the quiz is over to show scores
        if (currentQuestion != null && currentQuestionIndex + 1 == exercises.size()) {
            showScores();
            return;
        }

        currentQuestionIndex = currentQuestion == null ? 0 : currentQuestionIndex + 1;
        currentQuestion = exercises.get(currentQuestionIndex);

        answerText.setText("");
        questionText.setText(currentQuestion.getEnglishText());
        nextButton.setAlpha(disabledAlpha);
        nextButton.setEnabled(false);

        // Enable all option buttons and set their text
        for (int i = 0; i < wordsLayout.getChildCount(); i++) {
            LinearLayout row = (LinearLayout) wordsLayout.getChildAt(i);
            for (int j = 0; j < row.getChildCount(); j++) {
                TextView item = (TextView) row.getChildAt(j);
                item.setEnabled(true);
                item.setAlpha(1);

                // Set the text of the button to the corresponding word in the options array
                item.setText(currentQuestion.getOptions()[(i == 1 ? 3 : i == 2 ? 6 : 0) + j]);

                // Add click listener to each button
                item.setOnClickListener(v -> {
                    // Concat the word to the previously selected words in the answer box with the first letter capitalised
                    answerText.setText(Utils.cap((answerText.getText() + " " + item.getText()).trim()));
                    nextButton.setAlpha(1);
                    nextButton.setEnabled(true);

                    // Disable clicked option button
                    item.setEnabled(false);
                    item.setAlpha(disabledAlpha);
                });
            }
        }

        // Update range slider based to width of the track
        int width = rangeBackground.getWidth();
        int range = exercises.size() - 1;
        int current = currentQuestionIndex;
        // width / range = width per question on the slider, multiply by current question index to get slider width.
        int newWidth = width / range * current;

        rangeSlider.getLayoutParams().width = newWidth;
        rangeSlider.requestLayout(); // Apparently might be required https://stackoverflow.com/a/17066696/11848657
    }

    private void checkAnswer() {
        if (answerText.getText().toString().equalsIgnoreCase(currentQuestion.getTranslatedText())) {
            // Increment the number of exercises passed
            exercisesPassed++;
            // Show layout box telling the user they got the answer correct
            correctLayout.setVisibility(ConstraintLayout.VISIBLE);
            // Change nav bar colour to value of R.color.answer_correct_background_color_secondary (green-ish)
            getWindow().setNavigationBarColor(getColor(R.color.answer_correct_background_color_secondary));
        } else {
            // Show layout box telling the user they got the answer wrong
            wrongLayout.setVisibility(ConstraintLayout.VISIBLE);
            // Show correct answer in the 'wrong asnwer' layout box
            wrongAnswerText.setText(currentQuestion.getTranslatedText());
            // Change nav bar color to value of R.color.answer_wrong_background_color_secondary (red-ish)
            getWindow().setNavigationBarColor(getColor(R.color.answer_wrong_background_color_secondary));
        }
    }

    private void showScores() {
        Intent scoreIntent = new Intent(LanguageActivity.this, ScoreActivity.class);
        scoreIntent.putExtra("passedAndTotal", new int[]{exercisesPassed, exercises.size()});
        scoreIntent.putExtra("activity", "LanguageActivity");
        startActivity(scoreIntent);
    }
}