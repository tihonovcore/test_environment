package com.tihonovcore.testenv.dao;

import com.tihonovcore.testenv.model.Answer;
import com.tihonovcore.testenv.model.Question;
import com.tihonovcore.testenv.model.Test;

import java.util.ArrayList;
import java.util.List;

public class InMemoryDao {
    private final List<Test> tests = new ArrayList<>();
    private int freeId = 0;

    {
        Answer first = new Answer();
        first.setAnswer("test ans 1");
        first.setCorrect(true);
        Answer second = new Answer();
        second.setAnswer("test ans 2");
        second.setCorrect(false);

        Question question = new Question();
        question.setQuestion("test question");
        question.setAnswers(List.of(first, second));

        Test test = new Test();
        test.setId(freeId++);
        test.setTitle("test title");
        test.setDescription("test descr");
        test.setQuestions(List.of(question));

        tests.add(test);
    }

    public List<Test> getAllTests() {
        return tests;
    }

    public void addTest(Test test) {
        tests.add(test);
    }

    public void updateTest(int id, Test newValue) {
        assert newValue.getId() == id;

        tests.removeIf(test -> test.getId() == id);
        tests.add(newValue);
    }

    public int getFreeId() {
        return freeId++;
    }

    public Test findTestById(int id) {
        //noinspection OptionalGetWithoutIsPresent
        return tests.stream().filter(test -> test.getId() == id).findFirst().get();
    }
}
