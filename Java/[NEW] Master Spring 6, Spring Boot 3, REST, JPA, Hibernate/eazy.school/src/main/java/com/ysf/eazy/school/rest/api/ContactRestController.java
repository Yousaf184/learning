package com.ysf.eazy.school.rest.api;

import com.ysf.eazy.school.model.jpa.ContactMessage;
import com.ysf.eazy.school.service.jpa.ContactService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(
    path = "/api/contact",
    produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }
)
@CrossOrigin(origins = "*")
public class ContactRestController {

    private final ContactService contactService;

    @Autowired
    public ContactRestController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping("/messages")
    public ResponseEntity<Map<String, Object>> getMessagesByStatus(
        @RequestParam(name = "status", defaultValue = "OPEN", required = false) String messageStatus,
        @RequestParam(name = "page", defaultValue = "1", required = false) int page,
        @RequestParam(name = "sortBy", defaultValue = "name", required = false) String sortBy,
        @RequestParam(name = "sortDir", defaultValue = "asc", required = false) String sortOrder
    ) {
        Map<String, Object> paginationParams = Map.of(
            "currentPage", page,
            "sortBy", sortBy,
            "sortOrder", sortOrder
        );
        Page<List<ContactMessage>> result = this.contactService.getContactMessages(messageStatus, paginationParams);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("status", "success");
        responseMap.put("data", result.getContent());
        responseMap.put("currentPage", page);
        responseMap.put("totalPages", result.getTotalPages());
        responseMap.put("totalRecords", result.getTotalElements());
        responseMap.put("sortField", sortBy);
        responseMap.put("sortOrder", sortOrder);

        return ResponseEntity.ok(responseMap);
    }

    @PostMapping("/message")
    public ResponseEntity<Map<String, Object>> saveMessage(
        @RequestHeader(name = "invocationFrom", required = false) String invocationFrom,
        @Valid @RequestBody ContactMessage contact
    ) {
        log.info(String.format("Header invocationFrom = %s", invocationFrom));

        this.contactService.saveContactMessage(contact);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("status", "success");
        responseMap.put("message", "Contact message saved successfully");

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("isMsgSaved", "true")
                .body(responseMap);
    }

    @DeleteMapping("/message")
    public ResponseEntity<Map<String, Object>> deleteMessage(RequestEntity<ContactMessage> requestEntity) {
        HttpHeaders headers = requestEntity.getHeaders();
        headers.forEach((key, value) -> log.info(String.format("Header '%s' = %s", key, value)));

        ContactMessage contactMsg = requestEntity.getBody();
        if (contactMsg != null) {
            this.contactService.deleteMessageById(contactMsg.getId());
        }

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("status", "success");
        responseMap.put("message", "Contact message deleted successfully");

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseMap);
    }

    @PatchMapping("/message/close/{messageId}")
    public ResponseEntity<Map<String, Object>> closeMessage(
        @PathVariable("messageId") Integer messageId
    ) {
        boolean isUpdated = this.contactService.updateContactMessageStatus(messageId, ContactMessage.MessageStatus.CLOSED);

        String responseStatus = isUpdated ? "success" : "error";
        String responseMsg = isUpdated
                ? "Message closed successfully"
                : "Something went wrong while updating the message";

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("status", responseStatus);
        responseMap.put("message", responseMsg);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseMap);
    }
}
