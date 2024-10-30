package com.ysf.eazy.school.controller;

import com.ysf.eazy.school.model.jpa.Person;
import com.ysf.eazy.school.service.jpa.PersonService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/user")
public class PersonController {

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("person", new Person());
        return "register.html";
    }

    @PostMapping("/register")
    public String registerUser(
        @Valid @ModelAttribute("person") Person user,
        Errors errors,
        RedirectAttributes redirectAttributes
    ) {
        if (errors.hasErrors()) {
            return "register.html";
        }

        boolean isRegistered = false;

        String message = isRegistered
                ? "Registration successful"
                : "Something went wrong while registering the user";

        redirectAttributes.addFlashAttribute("isUserRegistered", isRegistered);
        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/login?registered=true";
    }
}
