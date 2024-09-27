package com.ysf.eazy.school.controller;

import com.ysf.eazy.school.model.jpa.Person;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("person", new Person());
        return "register.html";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("person") Person user, Errors errors) {
        if (errors.hasErrors()) {
            return "register.html";
        }

        return "redirect:/login?registered=true";
    }
}
