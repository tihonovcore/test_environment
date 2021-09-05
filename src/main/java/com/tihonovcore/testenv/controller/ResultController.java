package com.tihonovcore.testenv.controller;

import com.tihonovcore.testenv.model.Result;
import com.tihonovcore.testenv.repository.ResultRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ResultController {
    private final ResultRepository resultRepository;

    public ResultController(ResultRepository resultRepository) {
        this.resultRepository = resultRepository;
    }

    @GetMapping("/result/{resultId}")
    public String getResult(
            @PathVariable("resultId") int resultId,
            ModelMap model
    ) {
        Result result = resultRepository.getById(resultId);
        model.addAttribute("result", result);
        //TODO: pass correct answers

        return "result";
    }
}
