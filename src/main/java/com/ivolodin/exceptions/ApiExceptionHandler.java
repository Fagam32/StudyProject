package com.ivolodin.exceptions;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ModelAndView handleExceptions(Exception e) {
        e.printStackTrace();
        ModelAndView modelAndView = new ModelAndView("errorPage");
        modelAndView.addObject("message", e.getMessage());
        return modelAndView;
    }
}
