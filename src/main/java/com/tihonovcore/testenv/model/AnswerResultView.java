package com.tihonovcore.testenv.model;

public class AnswerResultView {
    private String question;
    private String correctAnswer;
    private String actualAnswer;

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
