package com.ysf.eazy.school.rest.client.controller;

import com.ysf.eazy.school.rest.client.clients.ContactFeignClient;
import com.ysf.eazy.school.rest.client.clients.IRestClient;
import com.ysf.eazy.school.rest.client.clients.IRestClientAsync;
import com.ysf.eazy.school.rest.client.model.ContactMessage;
import com.ysf.eazy.school.rest.client.model.CustomErrorResponse;
import com.ysf.eazy.school.rest.client.model.SuccessResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/api/contact")
public class ContactMessageController {

    private static final String CONTACT_API_BASE_URL = "http://localhost:8080/api/contact";

    private final IRestClient apiClient;
    private final IRestClientAsync apiClientAsync;
    private final ContactFeignClient contactFeignClient;

    @Autowired
    public ContactMessageController(
        @Qualifier("myRestTemplate") IRestClient apiClient,
        IRestClientAsync apiClientAsync,
        ContactFeignClient contactFeignClient
    ) {
        this.apiClient = apiClient;
        this.apiClientAsync = apiClientAsync;
        this.contactFeignClient = contactFeignClient;
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

    @GetMapping("/messages/async")
    public Mono<ResponseEntity<String>> getMessagesByStatusAsync(
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

        log.info("SENDING ASYNC GET REQUEST TO: {}", requestUrl);

        return this.apiClientAsync.getAllAsync(requestUrl, String.class);
    }

    @GetMapping("/messages/feign")
    public ResponseEntity<Map<String, Object>> getMessagesByStatusUsingFeignClient(
        @RequestParam(name = "status", defaultValue = "OPEN", required = false) String messageStatus,
        @RequestParam(name = "page", defaultValue = "1", required = false) int page,
        @RequestParam(name = "sortBy", defaultValue = "name", required = false) String sortBy,
        @RequestParam(name = "sortDir", defaultValue = "asc", required = false) String sortOrder
    ) {
        log.info("SENDING GET REQUEST USING FEIGN CLIENT");
        return this.contactFeignClient.getMessagesByStatus(messageStatus, page, sortBy, sortOrder);
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
        Class<CustomErrorResponse> errorResponseClassType = CustomErrorResponse.class;
        return this.apiClient.post(
                requestUrl,
                contactMessage,
                customRequestHeaders,
                successResponseClassType,
                errorResponseClassType);
    }

    @PostMapping("/message/async")
    public Mono<ResponseEntity<?>> saveMessageAsync(
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

        log.info("SENDING ASYNC POST REQUEST TO: {}", requestUrl);

        Class<SuccessResponse> successResponseClassType = SuccessResponse.class;
        Class<CustomErrorResponse> errorResponseClassType = CustomErrorResponse.class;
        return this.apiClientAsync.postAsync(
                requestUrl,
                contactMessage,
                customRequestHeaders,
                successResponseClassType,
                errorResponseClassType);
    }

    @PostMapping("/message/feign")
    public ResponseEntity<Map<String, Object>> saveMessageUsingFeignClient(
        @RequestHeader(name = "invocationFrom", required = false) String invocationFrom,
        @RequestBody ContactMessage contactMessage
    ) {
        log.info("SENDING POST REQUEST USING FEIGN CLIENT");
        return this.contactFeignClient.saveMessage(invocationFrom, contactMessage);
    }
}
