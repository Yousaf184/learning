package com.ysf.eazy.school.rest.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ysf.eazy.school.model.jpa.ContactMessage;
import com.ysf.eazy.school.service.jpa.ContactService;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

@WebMvcTest(ContactRestController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ContactRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ContactService contactService;

    @Autowired
    private ObjectMapper objectMapper;

    @Captor
    private ArgumentCaptor<ContactMessage> contactMsgArgumentCaptor;

    @Captor
    private ArgumentCaptor<ContactMessage.MessageStatus> contactMsgStatusCaptor;

    @Test
    @DisplayName("Get open messages")
    @Tag("GET")
    void getMessagesByStatus() throws Exception {
        final String STATUS = "Open";

        String messageListStr = """
            [
                {
                    "id": 5,
                    "name": "Adam",
                    "mobileNum": "2176436587",
                    "email": "zadam@gmail.com",
                    "subject": "Regarding a job",
                    "message": "Wanted to join as teacher",
                    "status": "OPEN"
                }
           ]
        """;

        List<ContactMessage> contactMessages = this.objectMapper.readValue(messageListStr, new TypeReference<List<ContactMessage>>() {});
        // Wrap the list inside a list to match the expected type of List<ContactMessage>
        List<List<ContactMessage>> listOfContactMessages = new ArrayList<>();
        listOfContactMessages.add(contactMessages);
        Page<List<ContactMessage>> pagedContactMessageList = new PageImpl<>(listOfContactMessages);

        Mockito.when(
            this.contactService.getContactMessages(Mockito.eq(STATUS), Mockito.anyMap())
        ).thenReturn(pagedContactMessageList);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/api/contact/messages?status={status}", STATUS)
                .accept(MediaType.APPLICATION_JSON);

        this.mockMvc
                .perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.sortField").value("name"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currentPage").value("1"));
    }

    @Test
    @DisplayName("Save new message")
    @Tag("POST")
    void saveNewMessage() throws Exception {
        String messageStr = """
            {
                "name": "Adam",
                "mobileNum": "2176436587",
                "email": "zadam@gmail.com",
                "subject": "Regarding a job",
                "message": "Wanted to join as teacher"
            }
        """;

        Mockito.when(
                this.contactService.saveContactMessage(Mockito.any(ContactMessage.class))
        ).thenReturn(true);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/api/contact/message")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(messageStr);

        this.mockMvc
                .perform(request)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"));

        Mockito.verify(this.contactService, Mockito.atLeast(1))
                .saveContactMessage(this.contactMsgArgumentCaptor.capture());

        ContactMessage capturedArg = this.contactMsgArgumentCaptor.getValue();
        ContactMessage contactMessage = this.objectMapper.readValue(messageStr, ContactMessage.class);

        Assertions.assertEquals(contactMessage.getName(), capturedArg.getName());
        Assertions.assertNull(capturedArg.getId());
        Assertions.assertNull(capturedArg.getStatus());
    }

    @Test
    @DisplayName("Mark messages as closed")
    @Tag("PATCH")
    void markMessageAsClosed() throws Exception {
        String messageStr = """
            {
                "id": 5,
                "name": "Adam",
                "mobileNum": "2176436587",
                "email": "zadam@gmail.com",
                "subject": "Regarding a job",
                "message": "Wanted to join as teacher",
                "status": "OPEN"
            }
        """;

        ContactMessage contactMsgToUpdate = this.objectMapper.readValue(messageStr, ContactMessage.class);

        Mockito.when(
            this.contactService.updateContactMessageStatus(
                    Mockito.eq(contactMsgToUpdate.getId()),
                    Mockito.eq(ContactMessage.MessageStatus.CLOSED)
            )
        ).thenReturn(true);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .patch("/api/contact/message/close/{messageId}", contactMsgToUpdate.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(messageStr);

        this.mockMvc
                .perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"));

        Mockito.verify(this.contactService, Mockito.atMostOnce())
                .updateContactMessageStatus(
                        Mockito.eq(contactMsgToUpdate.getId()),
                        this.contactMsgStatusCaptor.capture()
                );

        ContactMessage.MessageStatus capturedArg = this.contactMsgStatusCaptor.getValue();
        Assertions.assertEquals(ContactMessage.MessageStatus.CLOSED, capturedArg);
    }
}