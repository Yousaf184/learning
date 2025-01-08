package com.ysf.eazy.school.controller;

import com.ysf.eazy.school.model.jpa.EazyClass;
import com.ysf.eazy.school.service.jpa.EazyClassService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final EazyClassService eazyClassService;

    @Autowired
    public AdminController(EazyClassService eazyClassService) {
        this.eazyClassService = eazyClassService;
    }

    @GetMapping("/classes")
    public ModelAndView displayClasses() {
        ModelAndView modelAndView = new ModelAndView("classes.html");

        modelAndView.addObject("eazyClass", new EazyClass());
        modelAndView.addObject("eazyClasses", this.eazyClassService.getAllClasses());

        return modelAndView;
    }

    @PostMapping("/class")
    public String saveNewClass(
        @Valid @ModelAttribute("eazyClass") EazyClass newClass,
        Errors errors
    ) {
        if (errors.hasErrors()) {
            return "classes.html";
        }

        this.eazyClassService.saveNewClass(newClass);

        return "redirect:/admin/classes";
    }
}
