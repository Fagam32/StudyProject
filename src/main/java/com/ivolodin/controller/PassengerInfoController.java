package com.ivolodin.controller;

import com.ivolodin.entities.User;
import com.ivolodin.service.UserService;
import com.ivolodin.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.time.LocalDate;

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
        modelAndView.addObject("birthDate", user.getBirthDate().toString());
        return modelAndView;
    }

    @PostMapping
    public String getPassengerInformation(Principal principal,
                                          @RequestParam String name,
                                          @RequestParam String surname,
                                          @RequestParam String birthDate) {
        LocalDate birthDateLocal = Utils.createDateFromString(birthDate);
        String userName = principal.getName();
        User user = userService.getUserByUsername(userName);
        if (name != null) user.setName(name);
        if (surname != null) user.setSurname(surname);
        if (birthDateLocal != null) user.setBirthDate(birthDateLocal);

        userService.save(user);

        return "redirect:/passengerInfo";
    }
}
