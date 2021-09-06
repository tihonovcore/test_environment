package com.tihonovcore.testenv.controller;

import com.tihonovcore.testenv.model.User;
import com.tihonovcore.testenv.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class SimpleLoginController {
    private final UserRepository userRepository;

    public SimpleLoginController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String login() {
        return "login";
    }

    @GetMapping("/login")
    public String login(HttpServletRequest request) {
        return "redirect:/user/" + request.getParameter("userId");
    }

    @PostMapping("/register")
    public String register(HttpServletRequest request) {
        User user = new User();
        user.setName(request.getParameter("userName"));
        user = userRepository.save(user);

        return "redirect:/user/" + user.getId();
    }
}
