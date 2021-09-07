package com.tihonovcore.testenv.model;

public class AnswerResultView {
    private final String question;
    private final String correctAnswer;
    private final String actualAnswer;

    public AnswerResultView(String question, String correctAnswer, String actualAnswer) {
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.actualAnswer = actualAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String getActualAnswer() {
        return actualAnswer;
    }
}
