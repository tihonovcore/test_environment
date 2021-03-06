package com.tihonovcore.testenv.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "tests")
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "test_has_question",
            joinColumns = { @JoinColumn(name = "tid") },
            inverseJoinColumns = { @JoinColumn(name = "qid") }
    )
    private List<Question> questions;

    @ManyToOne
    @JoinColumn(name = "author")
    private User author;

    @OneToMany(mappedBy = "test", cascade = CascadeType.ALL)
    private List<Result> results;

    public Test() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Question> getQuestions() {
        if (questions == null) {
            this.questions = new ArrayList<>();
        }
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public void addQuestion(Question question) {
        this.questions.add(question);
    }

    public Question findQuestionById(int questionId) {
        //noinspection OptionalGetWithoutIsPresent
        return this.questions.stream().filter(q -> q.getId() == questionId).findFirst().get();
    }

    public void updateQuestionById(int questionId, Question newValue) {
        this.questions.removeIf(q -> q.getId() == questionId);
        this.questions.add(newValue);
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }
}
