package com.ivolodin.controller;

import com.ivolodin.entities.User;
import com.ivolodin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/users")
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ModelAndView getAllUsers() {
        ModelAndView modelAndView = new ModelAndView("users");
        List<User> userList = userService.getAllUsers();
        modelAndView.addObject("userList", userList);
        return modelAndView;
    }

    @PostMapping
    public String ModelAndView(@RequestParam("userId") Integer userId,
                               @RequestParam(value = "admin", required = false) boolean adminRole,
                               @RequestParam(value = "user", required = false) boolean userRole) {

        userService.updateUserRoles(userId, adminRole, userRole);
        return "redirect:/users";
    }
}
