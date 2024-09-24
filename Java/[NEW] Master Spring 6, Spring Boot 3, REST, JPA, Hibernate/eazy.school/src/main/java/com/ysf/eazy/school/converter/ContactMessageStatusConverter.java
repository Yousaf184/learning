package com.ysf.eazy.school.converter;

import com.ysf.eazy.school.model.jpa.ContactMessage;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class ContactMessageStatusConverter implements AttributeConverter<ContactMessage.MessageStatus, String> {

    @Override
    public String convertToDatabaseColumn(ContactMessage.MessageStatus status) {
        return switch (status) {
            case OPEN -> "open";
            case CLOSED -> "closed";
        };
    }

    @Override
    public ContactMessage.MessageStatus convertToEntityAttribute(String status) {
        return switch (status) {
            case "open" -> ContactMessage.MessageStatus.OPEN;
            case "closed" -> ContactMessage.MessageStatus.CLOSED;
            default -> throw new IllegalArgumentException(status + " is not a valid message status");
        };
    }
}
