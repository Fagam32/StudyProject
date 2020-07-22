package com.ivolodin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {


    @GetMapping(value = {"/", "/index"})
    public ModelAndView main() {
        return new ModelAndView("index");
    }

}
