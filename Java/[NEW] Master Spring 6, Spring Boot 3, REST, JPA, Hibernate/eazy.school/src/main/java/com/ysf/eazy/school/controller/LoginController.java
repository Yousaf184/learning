package com.ysf.eazy.school.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @RequestMapping(value = "/login", method = { RequestMethod.GET, RequestMethod.POST })
    public String displayLoginPage(
        @RequestParam(name = "error", required = false) boolean isError,
        @RequestParam(name = "logout", required = false) boolean isLogout,
        Model model
    ) {
        String message = "";

        if (isError) {
            message = "Incorrect username / password";
        } else if (isLogout) {
            message = "You have been logged out";
        }

        model.addAttribute("message", message);
        return "login.html";
    }
}
