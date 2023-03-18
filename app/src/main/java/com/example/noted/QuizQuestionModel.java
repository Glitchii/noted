package com.example.noted;

public class QuizQuestionModel {
    protected int answerIndex;
    protected int imageResource;
    protected String[] answers;
    protected String question;

    public QuizQuestionModel(int answerIndex, String question, int imageResource, String[] answers) {
        this.answerIndex = answerIndex;
        this.question = question;
        this.imageResource = imageResource;
        this.answers = answers;
    }

    public String getQuestion() {
        return question;
    }

    public int getImageResource() {
        return imageResource;
    }

    public int getAnswerIndex() {
        return answerIndex;
    }

    public String[] getAnswers() {
        return answers;
    }
}
