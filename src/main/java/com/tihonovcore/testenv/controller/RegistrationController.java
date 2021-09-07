package com.tihonovcore.testenv.controller;

import com.tihonovcore.testenv.model.User;
import com.tihonovcore.testenv.service.UserService;
import com.tihonovcore.testenv.validation.UserValidation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

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
    public ModelAndView registration(@ModelAttribute("user") User user) {
        if (UserValidation.validate(user)) {
            ModelAndView result = new ModelAndView("/registration");
            result.addObject("errorMessage", UserValidation.message);
            return result;
        }

        if (!userService.saveUser(user)) {
            ModelAndView result = new ModelAndView("/registration");
            String message = "User with name '" + user.getName() + "' is already exists";
            result.addObject("errorMessage", message);
            return result;
        }

        return new ModelAndView("redirect:/login");
    }
}
