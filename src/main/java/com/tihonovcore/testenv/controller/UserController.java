package com.tihonovcore.testenv.controller;

import com.tihonovcore.testenv.model.User;
import com.tihonovcore.testenv.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/user/{id}")
    public String getUser(@PathVariable("id") int id, ModelMap model) {
        User user = userRepository.getById(id);
        model.addAttribute("user", user);

        return "user";
    }
}
