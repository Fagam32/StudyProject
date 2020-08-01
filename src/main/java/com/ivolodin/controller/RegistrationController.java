package com.ivolodin.controller;

import com.ivolodin.entities.Role;
import com.ivolodin.entities.User;
import com.ivolodin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;

@RequiredArgsConstructor
@Controller
public class RegistrationController {

    @Autowired
    private final UserService userService;

    @GetMapping("/registration")
    public ModelAndView sendRegistrationForm() {
        return new ModelAndView("registration");
    }

    @PostMapping("/registration")
    public String registration(@RequestParam String email,
                               @RequestParam String username,
                               @RequestParam String password) {
        User user = userService.getUserByEmail(email);
        user = userService.getUserByUsername(username);
        System.out.println("Email: " + email + "\nUsername: " + username + "\nPassword: " + password);
        if (user != null) {
            return "redirect:/registration?userAlreadyExists=true";
        } else {
            user = new User();
            user.setUserName(username);
            user.setEmail(email);
            user.setPassword(password);
            user.setRoles(Collections.singleton(Role.USER));
            userService.addUser(user);
        }
        return "redirect:/login";
    }
}
