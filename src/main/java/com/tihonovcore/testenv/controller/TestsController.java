package com.tihonovcore.testenv.controller;

    import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class TestsController {
    @GetMapping("tests")
    public String tests() {
        return "tests";
    }

    @GetMapping("tests/add")
    public String testsAdd() {
        return "addTest";
    }

    @PostMapping("tests/new")
    public String testsNew(HttpServletRequest request) {
        System.out.println(request.getParameter("title"));
        System.out.println(request.getParameter("description"));
        return "redirect:/tests"; //edit
    }
}
