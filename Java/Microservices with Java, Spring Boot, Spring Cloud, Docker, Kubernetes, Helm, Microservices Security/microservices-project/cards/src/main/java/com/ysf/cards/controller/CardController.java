package com.ysf.cards.controller;

import com.ysf.cards.dto.CardDto;
import com.ysf.cards.dto.ResponseDto;
import com.ysf.cards.service.ICardService;
import com.ysf.cards.utils.ResponseUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/card")
@RequiredArgsConstructor
@Tag(name = "Card REST API", description = "REST API for managing cards in EazyBank")
public class CardController {

    private final ICardService cardService;

    @Operation(
            summary = "Create new card",
            description = "Endpoint for creating new Card. Card details will be generated automatically",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Card created successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schemaProperties = {
                                            @SchemaProperty(name = "status", schema = @Schema(type = "string", implementation = ResponseUtils.ResponseStatus.class)),
                                            @SchemaProperty(name = "message", schema = @Schema(type = "string", implementation = String.class)),
                                            @SchemaProperty(name = "data", schema = @Schema(type = "object", implementation = CardDto.class)),
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = """
                                    Account creation failed because:
                                        - Mobile number is empty
                                        - Card associated with the provided mobile number already exists
                                    """,
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schemaProperties = {
                                            @SchemaProperty(name = "status", schema = @Schema(type = "string", example = "ERROR")),
                                            @SchemaProperty(name = "message", schema = @Schema(type = "string"))
                                    }
                            )
                    )
            }
    )
    @PostMapping
    public ResponseEntity<ResponseDto> createNewCard(@RequestParam("mobileNumber") String mobileNumber) {
        CardDto createdCard = this.cardService.createCard(mobileNumber);

        String responseMsg = "Card created successfully";
        return ResponseUtils.getCreatedSuccessResponse(createdCard, responseMsg);
    }

    @Operation(
            summary = "Get card details",
            description = "Endpoint to get card details associated with a provided mobile number",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Card details retrieved successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schemaProperties = {
                                            @SchemaProperty(name = "status", schema = @Schema(type = "string", example = "SUCCESS", implementation = ResponseUtils.ResponseStatus.class)),
                                            @SchemaProperty(name = "data", schema = @Schema(type = "object", implementation = CardDto.class))
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Card associated with the given mobile number not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schemaProperties = {
                                            @SchemaProperty(name = "status", schema = @Schema(type = "string", example = "ERROR")),
                                            @SchemaProperty(name = "message", schema = @Schema(type = "string"))
                                    }
                            )
                    )
            }
    )
    @GetMapping("/{mobileNumber}")
    public ResponseEntity<ResponseDto> getCard(@PathVariable("mobileNumber") String mobileNumber) {
        CardDto card = this.cardService.getCardDetails(mobileNumber);
        return ResponseUtils.getSuccessResponse(card);
    }

    @Operation(
            summary = "Update card details",
            description = "Endpoint to update card details associated with a provided card number",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Card details updated successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schemaProperties = {
                                            @SchemaProperty(name = "status", schema = @Schema(type = "string", example = "SUCCESS", implementation = ResponseUtils.ResponseStatus.class)),
                                            @SchemaProperty(name = "message", schema = @Schema(type = "string", implementation = String.class))
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Card associated with the given card number not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schemaProperties = {
                                            @SchemaProperty(name = "status", schema = @Schema(type = "string", example = "ERROR", implementation = ResponseUtils.ResponseStatus.class)),
                                            @SchemaProperty(name = "message", schema = @Schema(type = "string"))
                                    }
                            )
                    )
            }
    )
    @PutMapping("/{cardNumber}")
    public ResponseEntity<ResponseDto> updateCard(
            @PathVariable("cardNumber") String cardNumber,
            @Valid @RequestBody CardDto updatedCardDetails
    ) {
        this.cardService.updateCard(updatedCardDetails, cardNumber);

        String responseMsg = "Details updated successfully";
        return ResponseUtils.getSuccessResponse(responseMsg);
    }

    @Operation(
            summary = "Delete card details",
            description = "Endpoint to delete accounts details associated with a provided card number",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Card details deleted successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schemaProperties = {
                                            @SchemaProperty(name = "status", schema = @Schema(type = "string", example = "SUCCESS", implementation = ResponseUtils.ResponseStatus.class)),
                                            @SchemaProperty(name = "message", schema = @Schema(type = "string", implementation = String.class))
                                    }
                            )
                    )
            }
    )
    @DeleteMapping("/{cardNumber}")
    public ResponseEntity<ResponseDto> deleteCard(@PathVariable("cardNumber") String cardNumber) {
        this.cardService.deleteCard(cardNumber);

        String responseMsg = "Card deleted successfully";
        return ResponseUtils.getSuccessResponse(responseMsg);
    }
}
