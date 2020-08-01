package com.ivolodin.controller;

import com.ivolodin.dao.UserDao;
import com.ivolodin.entities.Role;
import com.ivolodin.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;

@Controller
public class MainController {

    @GetMapping(value = {"/", "/index"})
    public ModelAndView main() {
        return new ModelAndView("index");
    }

}
