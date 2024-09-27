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
        @RequestParam(name = "registered", required = false) boolean isRegistered,
        Model model
    ) {
        String message = "";
        boolean isSuccessMsg = false;

        if (isError) {
            message = "Incorrect username / password";
        } else if (isLogout) {
            message = "You have been logged out";
        } else if (isRegistered) {
            message = "Registration successful";
            isSuccessMsg = true;
        }

        model.addAttribute("isSuccessMsg", isSuccessMsg);
        model.addAttribute("message", message);
        return "login.html";
    }
}
