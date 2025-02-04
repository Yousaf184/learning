package com.ysf.eazy.school.rest.client.controller;

import com.ysf.eazy.school.rest.client.clients.IRestClient;
import com.ysf.eazy.school.rest.client.model.ContactMessage;
import com.ysf.eazy.school.rest.client.model.ErrorResponse;
import com.ysf.eazy.school.rest.client.model.SuccessResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;


@Slf4j
@RestController
@RequestMapping("/api/contact")
public class ContactMessageController {

    private static final String CONTACT_API_BASE_URL = "http://localhost:8080/api/contact";

    private final IRestClient apiClient;

    @Autowired
    public ContactMessageController(
        @Qualifier("myRestClient") IRestClient apiClient
    ) {
        this.apiClient = apiClient;
    }

    @GetMapping("/messages")
    public ResponseEntity<String> getMessagesByStatus(
        @RequestParam(name = "status", defaultValue = "OPEN", required = false) String messageStatus,
        @RequestParam(name = "page", defaultValue = "1", required = false) int page,
        @RequestParam(name = "sortBy", defaultValue = "name", required = false) String sortBy,
        @RequestParam(name = "sortDir", defaultValue = "asc", required = false) String sortOrder
    ) {
        String requestUrl = UriComponentsBuilder.fromUriString(CONTACT_API_BASE_URL)
                .path("/messages")
                .queryParam("status", messageStatus)
                .queryParam("page", page)
                .queryParam("sortBy", sortBy)
                .queryParam("sortDir", sortOrder)
                .toUriString();

        log.info("SENDING REQUEST TO: {}", requestUrl);

        return this.apiClient.getAll(requestUrl, String.class);
    }

    @PostMapping("/message")
    public ResponseEntity<?> saveMessage(
        @RequestHeader(name = "invocationFrom", required = false) String invocationFrom,
        @RequestBody ContactMessage contactMessage
    ) {
        log.info(String.format("Header invocationFrom = %s", invocationFrom));

        HttpHeaders customRequestHeaders = new HttpHeaders();
        if (invocationFrom != null) {
            customRequestHeaders.add("invocationFrom", invocationFrom);
        }

        String requestUrl = UriComponentsBuilder.fromUriString(CONTACT_API_BASE_URL)
                .path("/message")
                .toUriString();

        log.info("SENDING POST REQUEST TO: {}", requestUrl);

        Class<SuccessResponse> successResponseClassType = SuccessResponse.class;
        Class<ErrorResponse> errorResponseClassType = ErrorResponse.class;
        return this.apiClient.post(
                requestUrl,
                contactMessage,
                customRequestHeaders,
                successResponseClassType,
                errorResponseClassType);
    }
}
