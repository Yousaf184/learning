package com.ysf.eazy.school.controller;

import com.ysf.eazy.school.model.jpa.EazyClass;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/classes")
    public ModelAndView displayClasses() {
        ModelAndView modelAndView = new ModelAndView("classes.html");
        modelAndView.addObject("eazyClass", new EazyClass());
        return modelAndView;
    }
}
