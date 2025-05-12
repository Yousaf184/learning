package com.ysf.eazy.school.controller;

import com.ysf.eazy.school.dao.jpa.PersonRepository;
import com.ysf.eazy.school.model.jpa.Person;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final PersonRepository personRepository;

    @Autowired
    public DashboardController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @GetMapping("/dashboard")
    public String displayDashboardPage(Model model, Authentication auth, HttpSession session) {
        Person loggedInUser = this.personRepository.findByEmail(auth.getName());
        session.setAttribute("loggedInUser", loggedInUser);

        model.addAttribute("username", loggedInUser.getName());
        model.addAttribute("roles", auth.getAuthorities().toString());

        if (loggedInUser.getEazyClass() != null) {
            model.addAttribute("enrolledClass", loggedInUser.getEazyClass().getName());
        }

        return "dashboard.html";
    }
}
