package com.tihonovcore.testenv.model;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity(name = "answers")
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "answer", nullable = false)
    @Size(min = 1, max = 255, message = "Answer size should be between 1 and 255")
    private String answer;

    @Column(name = "iscorrect", nullable = false)
    private boolean isCorrect;

    public Answer() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    @Override
    public String toString() {
        return answer;
    }
}
