package com.ysf.eazy.school.controller;

import com.ysf.eazy.school.model.jpa.ContactMessage;
import com.ysf.eazy.school.service.jpa.ContactService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/contact")
public class ContactController {

    private final ContactService contactService;

    @Autowired
    public ContactController(@Qualifier("contactServiceJPA") ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping
    public String displayContactPage(Model model) {
        model.addAttribute("contactMsg", new ContactMessage());
        return "contact.html";
    }

    @PostMapping("/save")
    public String saveContactFormData(
        @Valid @ModelAttribute("contactMsg") ContactMessage contactMessage,
        Errors errors,
        RedirectAttributes redirectAttributes
    ) {
        if (errors.hasErrors()) {
            return "contact.html";
        }

        boolean isSaved = this.contactService.saveContactMessage(contactMessage);

        String message = isSaved
                ? "contact saved successfully"
                : "Something went wrong, couldn't save the message";

        redirectAttributes.addFlashAttribute("isContactMessageSaved", isSaved);
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/contact";
    }

    @GetMapping("/messages")
    public ModelAndView displayContactMessagesForAdmin(
        @RequestParam(name = "status", defaultValue = "OPEN", required = false) String messageStatus,
        @RequestParam(name = "page", defaultValue = "1", required = false) int page,
        @RequestParam(name = "sortBy", defaultValue = "name", required = false) String sortBy,
        @RequestParam(name = "sortDir", defaultValue = "asc", required = false) String sortOrder
    ) {
        Map<String, Object> paginationParams = Map.of(
            "currentPage", page,
            "sortBy", sortBy,
            "sortOrder", sortOrder
        );
        Page<List<ContactMessage>> pageResponse =
                this.contactService.getContactMessages(messageStatus, paginationParams);

        ModelAndView modelAndView = new ModelAndView("messages.html");
        modelAndView.addObject("messages", pageResponse.getContent());

        // parameters used for pagination and sorting
        modelAndView.addObject("currentPage", page);
        modelAndView.addObject("totalPages", pageResponse.getTotalPages());
        modelAndView.addObject("sortBy", sortBy);
        modelAndView.addObject("currentSortOrder", sortOrder);
        String reverseSortOrder = sortOrder.equals("asc") ? "desc" : "asc";
        modelAndView.addObject("reverseSortOrder", reverseSortOrder);

        return modelAndView;
    }

    @GetMapping("/closeMsg")
    public String closeContactMessage(
            @RequestParam("id") Integer messageId,
            RedirectAttributes redirectAttributes
    ) {
        boolean isUpdated = false;

        if (messageId != null) {
            isUpdated = this.contactService.updateContactMessageStatus(
                    messageId,
                    ContactMessage.MessageStatus.CLOSED
            );
       }

        String message = isUpdated
                ? "Message closed successfully"
                : "Something went wrong while closing the message";

        redirectAttributes.addFlashAttribute("isMessageClosed", isUpdated);
        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/contact/messages?page=1&sortBy=name&sortDir=asc";
    }
}
