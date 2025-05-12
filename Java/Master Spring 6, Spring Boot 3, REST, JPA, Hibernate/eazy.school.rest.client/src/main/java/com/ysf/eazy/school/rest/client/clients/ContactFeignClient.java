package com.ysf.eazy.school.rest.client.clients;

import com.ysf.eazy.school.rest.client.model.ContactMessage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "contact", url = "http://localhost:8080/api/contact")
public interface ContactFeignClient {

    @GetMapping("/messages")
    ResponseEntity<Map<String, Object>> getMessagesByStatus(
        @RequestParam(name = "status", defaultValue = "OPEN", required = false) String messageStatus,
        @RequestParam(name = "page", defaultValue = "1", required = false) int page,
        @RequestParam(name = "sortBy", defaultValue = "name", required = false) String sortBy,
        @RequestParam(name = "sortDir", defaultValue = "asc", required = false) String sortOrder
    );

    @PostMapping("/message")
    ResponseEntity<Map<String, Object>> saveMessage(
        @RequestHeader(name = "invocationFrom", required = false) String invocationFrom,
        @RequestBody ContactMessage contact
    );
}
