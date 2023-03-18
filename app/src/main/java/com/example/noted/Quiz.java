package com.example.noted;

import android.os.Bundle;
import android.view.View;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.configure(R.layout.activity_quiz);

        // Add a few questions to the quiz
        questions.add(new QuizQuestionModel(2, "Which of the following is an open-source operating system?", R.drawable.mini_displayport, new String[]{"Mini HDMI", "Thunderbolt 2", "Mini DisplayPort", "Micro USB"}));
        questions.add(new QuizQuestionModel(2, "Which of the following is a popular front-end web development framework?", -1, new String[]{"Django", "Flask", "React", "Express"}));
        questions.add(new QuizQuestionModel(3, "Who is the founder of Microsoft Corporation?", -1, new String[]{"Steve Jobs", "Bill Gates", "Mark Zuckerberg", "Jeff Bezos"}));
        questions.add(new QuizQuestionModel(1, "What is a server?", -1, new String[]{"A computer program that provides services to other computer programs", "A program that allows you to browse the web", "A program that allows you to send emails", "A program that allows you to play games online"}));
        questions.add(new QuizQuestionModel(0, "What is an operating system?", -1, new String[]{"A software that manages computer hardware and software resources", "A software that helps you create and edit documents", "A software that helps you play games", "A software that helps you connect to the internet"}));

        newQuestion();
    }

    private void newQuestion() {
        currentQuestion = questions.get(currentQuestion == null ? 0 : questions.indexOf(currentQuestion) + 1);

        // Check if the quiz is over
        if (questions.indexOf(currentQuestion) == questions.size() - 1) {
            endQuiz();
            return;
        }

        // Set the question text
        TextView questionText = findViewById(R.id.questionText);
        questionText.setText(currentQuestion.getQuestion());

        // Set the image
        ImageView questionImage = findViewById(R.id.questionImage);
        if (currentQuestion.getImageResource() == -1)
            questionImage.setVisibility(View.GONE);
        else {
            questionImage.setImageResource(currentQuestion.getImageResource());
            questionImage.setVisibility(View.VISIBLE);
        }

        // Set the answers
        LinearLayout answers = findViewById(R.id.answers);
        for (int i = 0; i < answers.getChildCount(); i++) {
            ConstraintLayout layout = (ConstraintLayout) answers.getChildAt(i);
            TextView answer = (TextView) layout.getChildAt(0);
            RadioButton radio = (RadioButton) layout.getChildAt(1);

            answer.setText(currentQuestion.getAnswers()[i]);
            if (i == currentQuestion.getAnswerIndex())
                radio.setChecked(true);

            // Set the on click listener for the radio button, answer, and layout
            int index = i;
            radio.setOnClickListener(v -> answerClicked(index));
            answer.setOnClickListener(v -> answerClicked(index));
            layout.setOnClickListener(v -> answerClicked(index));

            // Set the on click listener for the next button
            findViewById(R.id.nextButton).setOnClickListener(v -> newQuestion());
        }
    }

    private void endQuiz() {
        Toast.makeText(this, "Quiz has ended", Toast.LENGTH_SHORT).show();
    }

    private void answerClicked(int index) {
        Toast.makeText(this, "Question " + index + " answered", Toast.LENGTH_SHORT).show();
    }
}