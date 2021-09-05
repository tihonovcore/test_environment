package com.tihonovcore.testenv.controller;

import com.tihonovcore.testenv.model.Result;
import com.tihonovcore.testenv.model.User;
import com.tihonovcore.testenv.repository.AnswerRepository;
import com.tihonovcore.testenv.repository.TestRepository;
import com.tihonovcore.testenv.repository.UserRepository;
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

    @GetMapping("/user/{userId}/dotest/{testId}")
    public String doTest(
            @PathVariable("userId") int userId,
            @PathVariable("testId") int testId,
            ModelMap model
    ) {
        model.addAttribute("user", userRepository.getById(userId));
        model.addAttribute("test", testRepository.getById(testId));

        return "doTest";
    }

    @PostMapping("/user/{userId}/dotest/{testId}")
    public String checkTest(
            HttpServletRequest request,
            @PathVariable("userId") int userId,
            @PathVariable("testId") int testId
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

        Result result = new Result();
        result.setTid(testId);
        result.setScore(score);
        result.setAnswers(answers);

        User user = userRepository.getById(userId);
        user.getPass().add(result);
        userRepository.save(user);

        return "redirect:/user/" + userId;
    }
}
