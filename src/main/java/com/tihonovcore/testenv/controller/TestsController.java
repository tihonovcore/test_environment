package com.tihonovcore.testenv.controller;

import com.tihonovcore.testenv.dao.InMemoryDao;
import com.tihonovcore.testenv.model.Answer;
import com.tihonovcore.testenv.model.Question;
import com.tihonovcore.testenv.model.Test;
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
    private final InMemoryDao dao;

    public TestsController(InMemoryDao dao) {
        this.dao = dao;
    }

    @GetMapping("tests")
    public String tests(ModelMap model) {
        model.addAttribute("testsList", dao.getAllTests());
        return "tests";
    }

    @GetMapping("tests/add")
    public String testsAdd() {
        return "addTest";
    }

    @PostMapping("tests/new")
    public String testsNew(HttpServletRequest request) {
        String title = request.getParameter("title");
        String description = request.getParameter("description");

        Test test = new Test();
        test.setTitle(title);
        test.setDescription(description);
        test.setId(dao.getFreeId());
        dao.addTest(test);

        return "redirect:/tests/" + test.getId() + "/edit";
    }

    @GetMapping("tests/{id}/edit")
    public String testsEdit(@PathVariable("id") int id, ModelMap model) {
        Test test = dao.findTestById(id);

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
        question.setId(dao.getFreeId());
        question.setQuestion(request.getParameter("question"));
        question.setAnswers(readAnswersFromRequest(request));

        Test test = dao.findTestById(testId);
        Test newTest = new Test(test);
        newTest.addQuestion(question);
        dao.updateTest(testId, newTest);

        return "redirect:/tests/{testId}/edit";
    }

    @GetMapping("/tests/{testId}/question/{questionId}/edit")
    public String editQuestion(
            @PathVariable("testId") int testId,
            @PathVariable("questionId") int questionId,
            ModelMap model
    ) {
        Test test = dao.findTestById(testId);
        Question question = test.findQuestionById(questionId);

        model.addAttribute("question", question);
        model.addAttribute("testId", test.getId());

        return "editQuestion";
    }

    @PostMapping("/tests/{testId}/question/{questionId}/edit")
    public String editQuestion(
            HttpServletRequest request,
            @PathVariable("testId") int testId,
            @PathVariable("questionId") int questionId
    ) {
        Question question = new Question();
        question.setId(questionId);
        question.setQuestion(request.getParameter("question"));
        question.setAnswers(readAnswersFromRequest(request));

        Test test = dao.findTestById(testId);
        Test newTest = new Test(test);
        newTest.updateQuestionById(questionId, question);
        dao.updateTest(testId, newTest);

        return "redirect:/tests/" + testId + "/edit";
    }
}
