package com.ysf.eazy.school.rest.client.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ContactMessage {

    public enum MessageStatus { OPEN, CLOSED };

    private Integer id;
    private String name;
    private String mobileNum;
    private String email;
    private String subject;
    private String message;

    private MessageStatus status;
}
