package com.ysf.eazy.school.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ModelAndView showErrorPage(Exception ex) {
        if (ex instanceof NoResourceFoundException) {
            System.out.println("RESOURCE NOT FOUND EXCEPTION: "  + ex.getMessage());
        } else {
            ex.printStackTrace();
        }

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("error");
        modelAndView.addObject("errorMsg", ex.getMessage());

        return modelAndView;
    }
}
