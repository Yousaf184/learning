package com.ysf.eazy.school.service.jpa;

import com.ysf.eazy.school.dao.jpa.ContactRepository;
import com.ysf.eazy.school.model.jpa.ContactMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("contactServiceJPA")
public class ContactService {

    private final ContactRepository contactRepository;

    @Autowired
    public ContactService(@Qualifier("contactRepositoryJPA") ContactRepository repo) {
        this.contactRepository = repo;
    }

    public boolean saveContactMessage(ContactMessage contactMessage) {
        contactMessage.setStatus(ContactMessage.MessageStatus.OPEN);

        ContactMessage savedContactMessage = this.contactRepository.save(contactMessage);
        return savedContactMessage.getId() != null;
    }

    public Page<List<ContactMessage>> getContactMessages(String messageStatus, Map<String, Object> paginationParams) {
        boolean isValidStatus = false;
        for (ContactMessage.MessageStatus status : ContactMessage.MessageStatus.values()) {
            if (status.toString().equalsIgnoreCase(messageStatus)) {
                isValidStatus = true;
            }
        }

        if (!isValidStatus) {
            throw new IllegalArgumentException(messageStatus + " is not a valid contact message status");
        }

        ContactMessage.MessageStatus status = ContactMessage.MessageStatus.valueOf(messageStatus.toUpperCase());

        // pagination and sorting
        final int PAGE_SIZE = 5;
        int startPage = (int) paginationParams.get("currentPage") - 1; // page numbers start from zero
        String sortByField = (String) paginationParams.get("sortBy");
        String sortOrder = (String) paginationParams.get("sortOrder");

        Sort sorter = sortOrder.equals("asc")
                ? Sort.by(sortByField).ascending()
                : Sort.by(sortByField).descending();
        Pageable pageable = PageRequest.of(startPage, PAGE_SIZE, sorter);

        return this.contactRepository.findByStatus(status, pageable);
    }

    public boolean updateContactMessageStatus(
        Integer messageId,
        ContactMessage.MessageStatus newStatus
    ) {
        int updatedRowCount = this.contactRepository.updateMessageStatus(newStatus, messageId);
        return updatedRowCount == 1;
    }

    public void deleteMessageById(Integer id) {
        this.contactRepository.deleteById(id);
    }
}
