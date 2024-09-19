package com.ysf.eazy.school.service;

import com.ysf.eazy.school.dao.ContactRepository;
import com.ysf.eazy.school.model.ContactMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactService {

    private final ContactRepository contactRepository;

    @Autowired
    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public boolean saveContactMessage(ContactMessage contactMessage) {
        int rowsUpdatedCount = this.contactRepository.saveContactMessage(contactMessage);
        return rowsUpdatedCount > 0;
    }

    public List<ContactMessage> getContactMessages(String messageStatus) {
        return this.contactRepository.getContactMessages(messageStatus);
    }

    public boolean updateContactMessageStatus(
            Integer messageId,
            ContactMessage.MessageStatus newStatus,
            String updatedBy
    ) {
        int rowsUpdatedCount = this.contactRepository.updateContactMessageStatus(
                messageId,
                newStatus.toString().toLowerCase(),
                updatedBy
        );

        return rowsUpdatedCount > 0;
    }
}
