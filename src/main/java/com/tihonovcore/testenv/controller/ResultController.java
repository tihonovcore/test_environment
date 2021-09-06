package com.tihonovcore.testenv.controller;

import com.tihonovcore.testenv.model.Answer;
import com.tihonovcore.testenv.model.AnswerResultView;
import com.tihonovcore.testenv.model.Result;
import com.tihonovcore.testenv.model.Test;
import com.tihonovcore.testenv.repository.ResultRepository;
import com.tihonovcore.testenv.repository.TestRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class ResultController {
    private final ResultRepository resultRepository;
    private final TestRepository testRepository;

    public ResultController(ResultRepository resultRepository, TestRepository testRepository) {
        this.resultRepository = resultRepository;
        this.testRepository = testRepository;
    }

    @GetMapping("/user/{userId}/result/{resultId}")
    public String getResult(
            @PathVariable("userId") int userId,
            @PathVariable("resultId") int resultId,
            ModelMap model
    ) {
        Result result = resultRepository.getById(resultId);
        Test test = testRepository.getById(result.getTid());

        List<Answer> allAnswers = test.getQuestions().stream()
                .flatMap(q -> q.getAnswers().stream())
                .collect(Collectors.toList());

        Map<Integer, String> aidToAnswer = allAnswers.stream().collect(Collectors.toMap(Answer::getId, Answer::getAnswer));

        List<Integer> actualAnswers = result.getAnswers();
        List<Integer> correctAnswers = allAnswers.stream()
                .filter(Answer::isCorrect)
                .map(Answer::getId)
                .collect(Collectors.toList());

        List<AnswerResultView> answerResults = new ArrayList<>();
        for (int i = 0; i < test.getQuestions().size(); i++) {
            String question = test.getQuestions().get(i).getQuestion();
            String correctAnswer = aidToAnswer.get(correctAnswers.get(i));
            String actualAnswer = aidToAnswer.get(actualAnswers.get(i));

            answerResults.add(new AnswerResultView(question, correctAnswer, actualAnswer));
        }

        model.addAttribute("test", test);
        model.addAttribute("result", result);
        model.addAttribute("answerResults", answerResults);

        return "result";
    }

    @GetMapping("/user/{userId}/result/test/{testId}")
    public String getAllResultsOfTest(
            @PathVariable("userId") int userId,
            @PathVariable("testId") int testId,
            ModelMap model
    ) {
        Test test = testRepository.getById(testId);

        model.addAttribute("userId", userId);
        model.addAttribute("title", test.getTitle());
        model.addAttribute("results", test.getResults());

        return "allResults";
    }
}
