package com.tihonovcore.testenv.controller;

import com.tihonovcore.testenv.model.Answer;
import com.tihonovcore.testenv.model.Question;
import com.tihonovcore.testenv.model.Test;
import com.tihonovcore.testenv.model.User;
import com.tihonovcore.testenv.repository.QuestionRepository;
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
public class TestsController {
    private final TestRepository testRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;

    public TestsController(TestRepository testRepository, UserRepository userRepository, QuestionRepository questionRepository) {
        this.testRepository = testRepository;
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
    }

    @GetMapping("tests")
    public String tests(ModelMap model) {
        model.addAttribute("testsList", testRepository.findAll());
        return "tests";
    }

    @GetMapping("/user/{id}/tests/add")
    public String testsAdd(@PathVariable("id") int id, ModelMap model) {
        model.addAttribute("authorId", id);
        return "addTest";
    }

    @PostMapping("/user/{id}/tests/new")
    public String testsNew(@PathVariable("id") int authorId, HttpServletRequest request) {
        User author = userRepository.getById(authorId);

        String title = request.getParameter("title");
        String description = request.getParameter("description");

        Test test = new Test();
        test.setTitle(title);
        test.setAuthor(author);
        test.setDescription(description);
        testRepository.save(test);

        return "redirect:/tests/" + test.getId() + "/edit";
    }

    @GetMapping("tests/{id}/edit")
    public String testsEdit(@PathVariable("id") int id, ModelMap model) {
        Test test = testRepository.getById(id);

        model.addAttribute("testId", test.getId());
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

    @PostMapping("tests/{testId}/edit/question/add")
    public String testsAddNewQuestion(
            HttpServletRequest request,
            @PathVariable("testId") int testId
    ) {
        Question question = new Question();
        question.setQuestion(request.getParameter("question"));
        question.setAnswers(readAnswersFromRequest(request));

        Test test = testRepository.getById(testId);
        test.getQuestions().add(question);
        testRepository.save(test);

        return "redirect:/tests/{testId}/edit";
    }

    @GetMapping("/tests/{testId}/question/{questionId}/edit")
    public String editQuestion(
            @PathVariable("testId") int testId,
            @PathVariable("questionId") int questionId,
            ModelMap model
    ) {
        Question question = questionRepository.getById(questionId);
        model.addAttribute("question", question);
        model.addAttribute("testId", testId);

        return "editQuestion";
    }

    @PostMapping("/tests/{testId}/question/{questionId}/edit")
    public String editQuestion(
            HttpServletRequest request,
            @PathVariable("testId") int testId,
            @PathVariable("questionId") int questionId
    ) {
        Question question = questionRepository.getById(questionId);
        question.setQuestion(request.getParameter("question"));
        question.setAnswers(readAnswersFromRequest(request));
        questionRepository.save(question);

        return "redirect:/tests/" + testId + "/edit";
    }
}
