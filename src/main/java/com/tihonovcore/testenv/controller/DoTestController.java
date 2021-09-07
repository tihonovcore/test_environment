package com.tihonovcore.testenv.controller;

import com.tihonovcore.testenv.model.Result;
import com.tihonovcore.testenv.model.Test;
import com.tihonovcore.testenv.model.User;
import com.tihonovcore.testenv.repository.AnswerRepository;
import com.tihonovcore.testenv.repository.TestRepository;
import com.tihonovcore.testenv.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class DoTestController {
    private final UserRepository userRepository;
    private final TestRepository testRepository;
    private final AnswerRepository answerRepository;

    public DoTestController(
            UserRepository userRepository,
            TestRepository testRepository,
            AnswerRepository answerRepository
    ) {
        this.userRepository = userRepository;
        this.testRepository = testRepository;
        this.answerRepository = answerRepository;
    }

    @GetMapping("/dotest/{testId}")
    public String doTest(
            @PathVariable("testId") int testId,
            Authentication authentication,
            ModelMap model
    ) {
        User user = (User) authentication.getPrincipal();

        model.addAttribute("userId", user.getId());
        model.addAttribute("test", testRepository.getById(testId));

        return "doTest";
    }

    @PostMapping("/dotest/{testId}")
    public String checkTest(
            @PathVariable("testId") int testId,
            Authentication authentication,
            HttpServletRequest request
    ) {
        int score = 0;
        List<Integer> answers = new ArrayList<>();
        for (String answer : request.getParameterMap().keySet()) {
            String[] qidAndAid = answer.split("_");
            int qid = Integer.parseInt(qidAndAid[0]);
            int aid = Integer.parseInt(qidAndAid[1]);

            answers.add(aid);

            if (answerRepository.getById(aid).isCorrect()) {
                score++;
            }
        }

        User user = (User) authentication.getPrincipal();
        user = userRepository.getById(user.getId());

        Test test = testRepository.getById(testId);

        Result result = new Result();
        result.setTest(test);
        result.setUser(user);
        result.setScore(score);
        result.setAnswers(answers);

        user.getPass().add(result);
        userRepository.save(user);

        return "redirect:/user/" + user.getId();
    }
}
