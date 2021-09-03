package com.tihonovcore.testenv.model;

import java.util.ArrayList;
import java.util.List;

public class Question {
    private int id;
    private String question;
    private List<Answer> answers;

    public Question() {}

    public Question(Question question) {
        this.id = question.id;
        this.question = question.question;
        this.answers = new ArrayList<>();
        for (Answer a : question.answers) {
            this.answers.add(new Answer(a));
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public String getAnswerByIndex(int index) {
        Answer answer = getOrNull(answers, index);
        return answer != null ? answer.getAnswer() : "";
    }

    public String getIsCorrectByIndex(int index) {
        Answer answer = getOrNull(answers, index);
        if (answer == null) return "false";

        return Boolean.toString(answer.isCorrect());
    }

    private <T> T getOrNull(List<T> list, int index) {
        if (index >= list.size()) {
            return null;
        }

        return list.get(index);
    }
}
