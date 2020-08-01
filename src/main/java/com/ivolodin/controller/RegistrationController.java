package com.ivolodin.controller;

import com.ivolodin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@RequiredArgsConstructor
@Controller
@RequestMapping("/registration")
public class RegistrationController {

    @Autowired
    private final UserService userService;

    @GetMapping
    public ModelAndView sendRegistrationForm() {
        return new ModelAndView("registration");
    }

    @PostMapping
    public String registration(@RequestParam String email,
                               @RequestParam String username,
                               @RequestParam String password) {

        boolean registered = userService.registerNewUser(email, username, password);

        if (registered) {
            return "redirect:/login";
        } else {
            return "redirect:/registration?userAlreadyExists=true";
        }

    }
}
