package com.ysf.eazy.school.rest.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ysf.eazy.school.model.jpa.ContactMessage;
import com.ysf.eazy.school.service.jpa.ContactService;
import org.junit.jupiter.api.*;
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

    @Test
    @DisplayName("Get Open Messages")
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
}