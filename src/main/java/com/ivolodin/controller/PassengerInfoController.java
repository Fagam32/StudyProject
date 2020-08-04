package com.ivolodin.controller;

import com.ivolodin.entities.User;
import com.ivolodin.model.PassengerInfoForm;
import com.ivolodin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.security.Principal;

@PreAuthorize(value = "hasAnyAuthority('USER', 'ADMIN')")
@Controller
@RequestMapping("/passengerInfo")
public class PassengerInfoController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ModelAndView getPassengerInformation(Principal principal) {
        ModelAndView modelAndView = new ModelAndView("passengerInfo");
        String userName = principal.getName();
        User user = userService.getUserByUsername(userName);
        modelAndView.addObject("email", user.getEmail());
        modelAndView.addObject("name", user.getName());
        modelAndView.addObject("surname", user.getSurname());
        modelAndView.addObject("birthDate", user.getBirthDate());
        return modelAndView;
    }

    @PostMapping
    public String getPassengerInformation(@Valid PassengerInfoForm form,
                                          BindingResult bindingResult,
                                          Principal principal,
                                          Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("message", "Enter correct data");
            return "redirect:/passengerInfo";
        }

        String userName = principal.getName();
        User user = userService.getUserByUsername(userName);
        user.setName(form.getName());
        user.setSurname(form.getSurname());
        user.setBirthDate(form.getBirthDate());
        userService.save(user);

        return "redirect:/passengerInfo";
    }
}
