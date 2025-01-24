package com.ysf.eazy.school.controller;

import com.ysf.eazy.school.model.jpa.Person;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/student")
public class StudentController {

    @GetMapping("/courses")
    public ModelAndView displayCourses(HttpSession session) {
        Person student = (Person) session.getAttribute("loggedInUser");

        ModelAndView modelAndView = new ModelAndView("courses_enrolled.html");
        modelAndView.addObject("student", student);

        return modelAndView;
    }
}
