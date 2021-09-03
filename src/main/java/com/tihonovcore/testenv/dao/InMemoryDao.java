package com.tihonovcore.testenv.dao;

import com.tihonovcore.testenv.model.Test;

import java.util.ArrayList;
import java.util.List;

public class InMemoryDao {
    private final List<Test> tests = new ArrayList<>();
    private int freeId = 0;

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
