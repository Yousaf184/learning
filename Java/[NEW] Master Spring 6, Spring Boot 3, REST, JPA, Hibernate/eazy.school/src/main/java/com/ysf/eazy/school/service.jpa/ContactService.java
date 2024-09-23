package com.ysf.eazy.school.service.jpa;

import com.ysf.eazy.school.dao.jpa.ContactRepository;
import com.ysf.eazy.school.model.jpa.ContactMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service("contactServiceJPA")
public class ContactService {

    private final ContactRepository contactRepository;

    @Autowired
    public ContactService(@Qualifier("contactRepositoryJPA") ContactRepository repo) {
        this.contactRepository = repo;
    }

    public boolean saveContactMessage(ContactMessage contactMessage) {
        contactMessage.setStatus(ContactMessage.MessageStatus.OPEN);
        contactMessage.setCreatedAt(LocalDateTime.now());
        contactMessage.setCreatedBy("Anonymous");

        ContactMessage savedContactMessage = this.contactRepository.save(contactMessage);
        return savedContactMessage.getId() != null;
    }

    public List<ContactMessage> getContactMessages(String messageStatus) {
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
        return this.contactRepository.findByStatus(status);
    }

    public boolean updateContactMessageStatus(
            Integer messageId,
            ContactMessage.MessageStatus newStatus,
            String updatedBy
    ) {
        Optional<ContactMessage> contactMsgOptional = this.contactRepository.findById(messageId);
        ContactMessage contactMsg = contactMsgOptional.orElse(null);
        ContactMessage updatedContactMsg = null;

        if (contactMsg != null) {
            contactMsg.setStatus(newStatus);
            contactMsg.setUpdatedBy(updatedBy);
            contactMsg.setUpdatedAt(LocalDateTime.now());

            updatedContactMsg = this.contactRepository.save(contactMsg);
        }

        return updatedContactMsg != null;
    }
}
