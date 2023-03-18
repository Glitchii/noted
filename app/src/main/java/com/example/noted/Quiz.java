package com.example.noted;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

public class Quiz extends GlobalAppCompatActivity {
    ArrayList<QuizQuestionModel> questions = new ArrayList<>();
    QuizQuestionModel currentQuestion;
    int currentQuestionIndex;
    float disabledAlpha = .3f;
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

        // Add a few questions to the quiz
        questions.add(new QuizQuestionModel(3, "Which is an open-source operating system?", -1, new String[]{"Windows", "MacOS", "iOS", "Linux"}));
        questions.add(new QuizQuestionModel(2, "What type of port is this?", R.drawable.mini_displayport, new String[]{"Mini HDMI", "Thunderbolt 2", "Mini DisplayPort", "Micro USB"}));
        questions.add(new QuizQuestionModel(2, "Which of the following is a popular front-end web development framework?", -1, new String[]{"Django", "Flask", "React", "Express"}));
        questions.add(new QuizQuestionModel(1, "Who is the founder of Microsoft Corporation?", -1, new String[]{"Steve Jobs", "Bill Gates", "Mark Zuckerberg", "Jeff Bezos"}));

        newQuestion();
    }

    private void newQuestion() {
        // Check if the quiz is over
        if (currentQuestion != null && currentQuestionIndex + 1 == questions.size()) {
            endQuiz();
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
            ConstraintLayout layout = (ConstraintLayout) answersLayout.getChildAt(i);
            TextView answer = (TextView) layout.getChildAt(0);
            RadioButton radio = (RadioButton) layout.getChildAt(1);

            radio.setChecked(false);
            radio.setEnabled(true);

            radio.setOnClickListener(v -> answerClicked(index));
            answer.setOnClickListener(v -> answerClicked(index));
            layout.setOnClickListener(v -> answerClicked(index));
            nextButton.setOnClickListener(v -> newQuestion());

            answer.setText(currentQuestion.getAnswers()[i]);
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
        // For some reason when the next button is pressed, *part* of the root layout colour is changed to black!
        findViewById(R.id.root).setBackground(new ColorDrawable(Color.parseColor(chosenBackground == null ? defaultBackground : chosenBackground)));
    }

    private void endQuiz() {
        Toast.makeText(this, "Quiz has ended", Toast.LENGTH_SHORT).show();
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

        // Check if the answer is correct
        if (index == currentQuestion.getAnswerIndex()) {
            layout.setBackground(getDrawable(R.drawable.answer_correct_background));
            radioButton.setButtonTintList(ColorStateList.valueOf(getColor(R.color.answer_correct_color)));
        } else {
            layout.setBackground(getDrawable(R.drawable.answer_wrong_background));
            radioButton.setButtonTintList(ColorStateList.valueOf(getColor(R.color.answer_wrong_color)));

            // Wait a few seconds then highlight the correct answer
            new android.os.Handler().postDelayed(() -> {
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
            }, 500);
        }

        // Set the next button to visible
        nextButton.setAlpha(1);
    }
}