package com.tihonovcore.testenv.controller;

import com.tihonovcore.testenv.model.*;
import com.tihonovcore.testenv.repository.ResultRepository;
import com.tihonovcore.testenv.repository.TestRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

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

    @GetMapping("/result/{resultId}")
    public ModelAndView getResult(
            @PathVariable("resultId") int resultId,
            Authentication authentication,
            ModelMap model
    ) {
        Result result = resultRepository.getById(resultId);
        Test test = testRepository.getById(result.getTid());
        User user = (User) authentication.getPrincipal();

        if (user.getId() != result.getUser().getId() && user.getId() != test.getAuthor().getId()) {
            ModelAndView modelAndView = new ModelAndView("noPermission");
            modelAndView.addObject("userId", user.getId());
            return modelAndView;
        }

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

        return new ModelAndView("result");
    }

    @GetMapping("/result/test/{testId}")
    public ModelAndView getAllResultsOfTest(
            @PathVariable("testId") int testId,
            Authentication authentication,
            ModelMap model
    ) {
        Test test = testRepository.getById(testId);
        User user = (User) authentication.getPrincipal();

        if (user.getId() != test.getAuthor().getId()) {
            ModelAndView modelAndView = new ModelAndView("noPermission");
            modelAndView.addObject("userId", user.getId());
            return modelAndView;
        }

        model.addAttribute("userId", user.getId());
        model.addAttribute("title", test.getTitle());
        model.addAttribute("results", test.getResults());

        return new ModelAndView("allResults");
    }
}
