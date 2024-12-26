package com.ysf.eazy.school.controller;

import com.ysf.eazy.school.dto.Profile;
import com.ysf.eazy.school.model.jpa.Person;
import com.ysf.eazy.school.service.ProfileService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProfileController {

    private final ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/profile")
    public ModelAndView showProfilePage(Model model, HttpSession session) {
        Person loggedInUser = (Person) session.getAttribute("loggedInUser");
        Profile profile = this.profileService.getProfileOfLoggedInUser(loggedInUser);

        ModelAndView modelAndView = new ModelAndView("profile.html");
        modelAndView.addObject("profile", profile);

        if (model.containsAttribute("profileUpdated")) {
            modelAndView.addObject("profileUpdated", true);
        }

        return modelAndView;
    }

    @PostMapping("/profile")
    public String updateProfile(
        @Valid @ModelAttribute("profile") Profile profile,
        Errors errors,
        HttpSession session,
        RedirectAttributes redirectAttributes
    ) {
        if (errors.hasErrors()) {
            return "profile.html";
        }

        Person userToUpdate = (Person) session.getAttribute("loggedInUser");
        Person updatedPerson = this.profileService.updateUserProfile(profile, userToUpdate);

        session.setAttribute("loggedInUser", updatedPerson);

        redirectAttributes.addFlashAttribute("profileUpdated", true);
        return "redirect:/profile";
    }
}
