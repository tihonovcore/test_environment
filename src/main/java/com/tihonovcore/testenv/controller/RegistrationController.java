package com.tihonovcore.testenv.controller;

import com.tihonovcore.testenv.model.User;
import com.tihonovcore.testenv.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {
    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("user") User user) {
        if (!userService.saveUser(user)) {
            return "/registration";
        }

        return "redirect:/login";
    }
}
