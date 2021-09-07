package com.tihonovcore.testenv.controller;

import com.tihonovcore.testenv.model.Answer;
import com.tihonovcore.testenv.model.Question;
import com.tihonovcore.testenv.model.Test;
import com.tihonovcore.testenv.model.User;
import com.tihonovcore.testenv.repository.QuestionRepository;
import com.tihonovcore.testenv.repository.TestRepository;
import com.tihonovcore.testenv.repository.UserRepository;
import com.tihonovcore.testenv.validation.QuestionValidator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class TestsController {
    private final TestRepository testRepository;
    private final QuestionRepository questionRepository;

    public TestsController(TestRepository testRepository, QuestionRepository questionRepository) {
        this.testRepository = testRepository;
        this.questionRepository = questionRepository;
    }

    @GetMapping("/tests")
    public String tests(Authentication authentication, ModelMap model) {
        User user = (User) authentication.getPrincipal();

        model.addAttribute("userId", user.getId());
        model.addAttribute("testsList", testRepository.findAll());

        return "tests";
    }

    @GetMapping("/tests/add")
    public String testsAdd(Authentication authentication, ModelMap model) {
        User user = (User) authentication.getPrincipal();

        model.addAttribute("userId", user.getId());

        return "addTest";
    }

    @PostMapping("/tests/new")
    public String testsNew(HttpServletRequest request, Authentication authentication) {
        User author = (User) authentication.getPrincipal();

        String title = request.getParameter("title");
        String description = request.getParameter("description");

        Test test = new Test();
        test.setTitle(title);
        test.setAuthor(author);
        test.setDescription(description);
        testRepository.save(test);

        return "redirect:/tests/" + test.getId() + "/edit";
    }

    @GetMapping("/tests/{testId}/edit")
    public String testsEdit(
            @PathVariable("testId") int testId,
            Authentication authentication,
            ModelMap model
    ) {
        Test test = testRepository.getById(testId);
        User user = (User) authentication.getPrincipal();

        if (user.getId() != test.getAuthor().getId()) {
            System.out.println("No permissions");
            //TODO
        }

        model.addAttribute("userId", user.getId());
        model.addAttribute("testId", testId);
        model.addAttribute("title", test.getTitle());
        model.addAttribute("questions", test.getQuestions());

        return "editTest";
    }

    private List<Answer> readAnswersFromRequest(HttpServletRequest request) {
        int numberOfAnswers = Integer.parseInt(request.getParameter("select"));

        List<Answer> answers = new ArrayList<>();
        for (int i = 0; i < numberOfAnswers; i++) {
            Answer answer = new Answer();
            answer.setAnswer(request.getParameter("answer_" + (i + 1)));
            answer.setCorrect(request.getParameterMap().containsKey("answer_is_correct_" + (i + 1)));
            answers.add(answer);
        }

        return answers;
    }

    @PostMapping("/tests/{testId}/edit/question/add")
    public String testsAddNewQuestion(
            @PathVariable("testId") int testId,
            Authentication authentication,
            HttpServletRequest request
    ) {
        Test test = testRepository.getById(testId);
        User user = (User) authentication.getPrincipal();

        if (user.getId() != test.getAuthor().getId()) {
            System.out.println("No permissions");
            //TODO
        }

        Question question = new Question();
        question.setQuestion(request.getParameter("question"));
        question.setAnswers(readAnswersFromRequest(request));

        test.getQuestions().add(question);
        testRepository.save(test);

        return "redirect:/tests/{testId}/edit";
    }

    @GetMapping("/tests/{testId}/question/{questionId}/edit")
    public String editQuestion(
            @PathVariable("testId") int testId,
            @PathVariable("questionId") int questionId,
            Authentication authentication,
            ModelMap model
    ) {
        Test test = testRepository.getById(testId);
        User user = (User) authentication.getPrincipal();

        if (user.getId() != test.getAuthor().getId()) {
            System.out.println("No permissions");
            //TODO
        }

        Question question = questionRepository.getById(questionId);
        model.addAttribute("question", question);
        model.addAttribute("userId", user.getId());
        model.addAttribute("testId", testId);

        return "editQuestion";
    }

    @PostMapping("/tests/{testId}/question/{questionId}/edit")
    public ModelAndView editQuestion(
            @PathVariable("testId") int testId,
            @PathVariable("questionId") int questionId,
            Authentication authentication,
            HttpServletRequest request
    ) {
        Test test = testRepository.getById(testId);
        User user = (User) authentication.getPrincipal();

        if (user.getId() != test.getAuthor().getId()) {
            System.out.println("No permissions");
            //TODO
        }

        List<Answer> answers = readAnswersFromRequest(request);

        Question question = questionRepository.getById(questionId);
        question.setQuestion(request.getParameter("question"));
        question.setAnswers(answers);

        if (QuestionValidator.validate(question)) {
            ModelAndView result = new ModelAndView("editQuestion");

            result.addObject("question", question);
            result.addObject("messages", QuestionValidator.messages);
            result.addObject("hasErrors", true);
            result.addObject("userId", user.getId());
            result.addObject("testId", testId);

            return result;
        }

        questionRepository.save(question);

        return new ModelAndView("redirect:/tests/{testId}/edit");
    }
}
