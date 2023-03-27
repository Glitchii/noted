package com.example.noted;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

public class QuizActivity extends GlobalAppCompatActivity {
    int currentQuestionIndex, questionsPassed = 0;
    float disabledAlpha = .3f;
    boolean highlightAnswers = true; // Whether to highlight correct/wrong answers as user clicks from options.
    QuizQuestionModel currentQuestion;
    ArrayList<QuizQuestionModel> questions = new ArrayList<>();
    TextView rangeText, questionText;
    ConstraintLayout rangeBackground, rangeSlider;
    LinearLayout answersLayout;
    ImageView questionImage;
    Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.configure(R.layout.activity_quiz);

        rangeText = findViewById(R.id.rangeText);
        rangeSlider = findViewById(R.id.rangeSlider);
        rangeBackground = findViewById(R.id.rangeBackground);
        questionImage = findViewById(R.id.questionImage);
        questionText = findViewById(R.id.questionText);
        answersLayout = findViewById(R.id.answers);
        nextButton = findViewById(R.id.nextButton);

        // Add a few exercises to the quiz
        questions.add(new QuizQuestionModel(3, "Which is an open-source operating system?", -1, new String[]{"Windows", "MacOS", "iOS", "Linux"}));
        questions.add(new QuizQuestionModel(2, "What type of port is this?", R.drawable.mini_displayport, new String[]{"Mini HDMI", "Thunderbolt 2", "Mini DisplayPort", "Micro USB"}));
        questions.add(new QuizQuestionModel(2, "Which of the following is a popular front-end web development framework?", -1, new String[]{"Django", "Flask", "React", "Express"}));
        questions.add(new QuizQuestionModel(1, "Who is the founder of Microsoft Corporation?", -1, new String[]{"Steve Jobs", "Bill Gates", "Mark Zuckerberg", "Jeff Bezos"}));

        nextButton.setOnClickListener(v -> newQuestion());
        newQuestion();
    }

    private void newQuestion() {
        // Check if the quiz is over to show scores
        if (currentQuestion != null && currentQuestionIndex + 1 == questions.size()) {
            showScores();
            return;
        }

        currentQuestionIndex = currentQuestion == null ? 0 : currentQuestionIndex + 1;
        currentQuestion = questions.get(currentQuestionIndex);

        questionText.setText(currentQuestion.getQuestion());
        nextButton.setAlpha(disabledAlpha);
        nextButton.setEnabled(false);

        // Set the image visibility based on weather the question has an image resource ID not set to -1
        if (currentQuestion.getImageResource() == -1)
            questionImage.setVisibility(View.GONE);
        else {
            questionImage.setImageResource(currentQuestion.getImageResource());
            questionImage.setVisibility(View.VISIBLE);
        }

        // Set answers and radio buttons with on click listeners
        for (int i = 0; i < answersLayout.getChildCount(); i++) {
            final int index = i; // Final variable is required for lambda expressions
            ConstraintLayout layout = (ConstraintLayout) answersLayout.getChildAt(index);
            TextView answer = (TextView) layout.getChildAt(0);
            RadioButton radio = (RadioButton) layout.getChildAt(1);

            radio.setChecked(false);
            radio.setEnabled(true);

            radio.setOnClickListener(v -> answerClicked(index));
            answer.setOnClickListener(v -> answerClicked(index));
            layout.setOnClickListener(v -> answerClicked(index));

            answer.setText(currentQuestion.getAnswers()[index]);
        }

        // Enable radio buttons, and reset colours from red or green to defaults
        for (int i = 0; i < answersLayout.getChildCount(); i++) {
            ConstraintLayout layout = (ConstraintLayout) answersLayout.getChildAt(i);
            layout.setBackground(getDrawable(R.drawable.file_card_background));

            RadioButton radio = (RadioButton) layout.getChildAt(1);
            radio.setButtonTintList(ColorStateList.valueOf(getColor(R.color.answer_color)));
            radio.setEnabled(false);
        }

        rangeText.setText(currentQuestionIndex + 1 + " / " + questions.size());

        // Update range slider based to width of the track
        int width = rangeBackground.getWidth();
        int range = questions.size() - 1;
        int current = currentQuestionIndex;
        // width / range = width per question on the slider, multiply by current question index to get slider width.
        int newWidth = width / range * current;

        rangeSlider.getLayoutParams().width = newWidth;
        rangeSlider.requestLayout(); // Apparently might be required https://stackoverflow.com/a/17066696/11848657
    }

    private void showScores() {
        Intent scoreIntent = new Intent(QuizActivity.this, ScoreActivity.class);
        scoreIntent.putExtra("passedAndTotal", new int[]{questionsPassed, questions.size()});
        scoreIntent.putExtra("activity", "QuizActivity");
        startActivity(scoreIntent);
    }

    private void answerClicked(int index) {
        // Check if an answer has already been selected and return so the user can't change their answer
        if (nextButton.getAlpha() != disabledAlpha)
            return;

        // Layout view of the clicked answer
        ConstraintLayout layout = (ConstraintLayout) answersLayout.getChildAt(index);
        // User might have clicked the text or background layout instead of the radio button, so we check it.
        RadioButton radioButton = (RadioButton) layout.getChildAt(1);
        radioButton.setChecked(true);
        nextButton.setEnabled(true);

        // Answer is correct if the index of the chosen option view is the same as the answer index
        if (index == currentQuestion.getAnswerIndex()) {
            questionsPassed++;
            if (highlightAnswers) {
                layout.setBackground(getDrawable(R.drawable.answer_correct_background));
                radioButton.setButtonTintList(ColorStateList.valueOf(getColor(R.color.answer_correct_color)));
            }
        } else if (highlightAnswers) {
            layout.setBackground(getDrawable(R.drawable.answer_wrong_background));
            radioButton.setButtonTintList(ColorStateList.valueOf(getColor(R.color.answer_wrong_color)));

            // Wait a few seconds then highlight the correct answer
            new Handler().postDelayed(() -> {
                // Make sure user hasn't quickly clicked to next question before the animation has finished
                if (nextButton.getAlpha() == disabledAlpha)
                    return;

                // Get the correct ConstraintLayout and RadioButton
                ConstraintLayout correctLayout = (ConstraintLayout) answersLayout.getChildAt(currentQuestion.getAnswerIndex());
                RadioButton correctRadio = (RadioButton) correctLayout.getChildAt(1);

                // Create a TransitionDrawable with the current background and the new background
                TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[]{
                        correctLayout.getBackground(), // Current background
                        getDrawable(R.drawable.answer_correct_background) // New background
                });

                // Set the new background and animate the transition
                correctLayout.setBackground(transitionDrawable);
                correctRadio.setButtonTintList(ColorStateList.valueOf(getColor(R.color.answer_correct_color)));
                transitionDrawable.startTransition(500); // 500 milliseconds is the duration of the transition
                // https://www.tutorialspoint.com/how-do-you-animate-the-change-of-background-color-of-a-view-on-android
            }, 300);
        }

        // Set the next button to visible
        nextButton.setAlpha(1);
    }
}