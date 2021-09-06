package com.tihonovcore.testenv.controller;

import com.tihonovcore.testenv.model.Answer;
import com.tihonovcore.testenv.model.Question;
import com.tihonovcore.testenv.model.Test;
import com.tihonovcore.testenv.model.User;
import com.tihonovcore.testenv.repository.QuestionRepository;
import com.tihonovcore.testenv.repository.TestRepository;
import com.tihonovcore.testenv.repository.UserRepository;
import com.tihonovcore.testenv.validation.QuestionValidator;
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
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;

    public TestsController(TestRepository testRepository, UserRepository userRepository, QuestionRepository questionRepository) {
        this.testRepository = testRepository;
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
    }

    @GetMapping("/user/{userId}/tests")
    public String tests(@PathVariable("userId") int userId, ModelMap model) {
        model.addAttribute("userId", userId);
        model.addAttribute("testsList", testRepository.findAll());
        return "tests";
    }

    @GetMapping("/user/{userId}/tests/add")
    public String testsAdd(@PathVariable("userId") int userId, ModelMap model) {
        model.addAttribute("authorId", userId);
        return "addTest";
    }

    @PostMapping("/user/{userId}/tests/new")
    public String testsNew(@PathVariable("userId") int authorId, HttpServletRequest request) {
        User author = userRepository.getById(authorId);

        String title = request.getParameter("title");
        String description = request.getParameter("description");

        Test test = new Test();
        test.setTitle(title);
        test.setAuthor(author);
        test.setDescription(description);
        testRepository.save(test);

        return "redirect:/user/{userId}/tests/" + test.getId() + "/edit";
    }

    @GetMapping("/user/{userId}/tests/{testId}/edit")
    public String testsEdit(
            @PathVariable("userId") int userId,
            @PathVariable("testId") int testId,
            ModelMap model
    ) {
        Test test = testRepository.getById(testId);

        model.addAttribute("userId", userId);
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

    @PostMapping("/user/{userId}/tests/{testId}/edit/question/add")
    public String testsAddNewQuestion(
            HttpServletRequest request,
            @PathVariable("userId") int userId,
            @PathVariable("testId") int testId
    ) {
        Question question = new Question();
        question.setQuestion(request.getParameter("question"));
        question.setAnswers(readAnswersFromRequest(request));

        Test test = testRepository.getById(testId);
        test.getQuestions().add(question);
        testRepository.save(test);

        return "redirect:/user/{userId}/tests/{testId}/edit";
    }

    @GetMapping("/user/{userId}/tests/{testId}/question/{questionId}/edit")
    public String editQuestion(
            @PathVariable("userId") int userId,
            @PathVariable("testId") int testId,
            @PathVariable("questionId") int questionId,
            ModelMap model
    ) {
        Question question = questionRepository.getById(questionId);
        model.addAttribute("question", question);
        model.addAttribute("userId", userId);
        model.addAttribute("testId", testId);

        return "editQuestion";
    }

    @PostMapping("/user/{userId}/tests/{testId}/question/{questionId}/edit")
    public ModelAndView editQuestion(
            HttpServletRequest request,
            @PathVariable("userId") int userId,
            @PathVariable("testId") int testId,
            @PathVariable("questionId") int questionId
    ) {
        List<Answer> answers = readAnswersFromRequest(request);

        Question question = questionRepository.getById(questionId);
        question.setQuestion(request.getParameter("question"));
        question.setAnswers(answers);

        if (QuestionValidator.validate(question)) {
            ModelAndView result = new ModelAndView("editQuestion");

            result.addObject("question", question);
            result.addObject("messages", QuestionValidator.messages);
            result.addObject("hasErrors", true);
            result.addObject("userId", userId);
            result.addObject("testId", testId);

            return result;
        }

        questionRepository.save(question);

        return new ModelAndView("redirect:/user/{userId}/tests/{testId}/edit");
    }
}
