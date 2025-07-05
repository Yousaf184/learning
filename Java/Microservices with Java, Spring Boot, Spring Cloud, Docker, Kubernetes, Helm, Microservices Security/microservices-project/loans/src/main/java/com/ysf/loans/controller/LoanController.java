package com.ysf.loans.controller;

import com.ysf.loans.dto.LoanDto;
import com.ysf.loans.dto.ResponseDto;
import com.ysf.loans.service.ILoanService;
import com.ysf.loans.utils.ResponseUtils;
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
@RequestMapping("/api/v1/loan")
@RequiredArgsConstructor
@Tag(name = "Loan REST API", description = "REST API for managing loans in EazyBank")
public class LoanController {

    private final ILoanService loanService;

    @Operation(
            summary = "Create new loan",
            description = "Endpoint for creating new loan. Loan details will be generated automatically",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Loan created successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schemaProperties = {
                                            @SchemaProperty(name = "status", schema = @Schema(type = "string", implementation = ResponseUtils.ResponseStatus.class)),
                                            @SchemaProperty(name = "message", schema = @Schema(type = "string", implementation = String.class)),
                                            @SchemaProperty(name = "data", schema = @Schema(type = "object", implementation = LoanDto.class)),
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = """
                                    Loan creation failed because:
                                    
                                        - Mobile number is empty
                                        - Loan associated with the provided mobile number already exists
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
    public ResponseEntity<ResponseDto> createNewLoan(@RequestParam("mobileNumber") String mobileNumber) {
        LoanDto createdLoan = this.loanService.createLoan(mobileNumber);

        String responseMsg = "Loan created successfully";
        return ResponseUtils.getCreatedSuccessResponse(createdLoan, responseMsg);
    }

    @Operation(
            summary = "Get loan details",
            description = "Endpoint to get loan details associated with a provided mobile number",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Loan details retrieved successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schemaProperties = {
                                            @SchemaProperty(name = "status", schema = @Schema(type = "string", example = "SUCCESS", implementation = ResponseUtils.ResponseStatus.class)),
                                            @SchemaProperty(name = "data", schema = @Schema(type = "object", implementation = LoanDto.class))
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Loan associated with the given mobile number not found",
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
    public ResponseEntity<ResponseDto> getLoan(@PathVariable("mobileNumber") String mobileNumber) {
        LoanDto loan = this.loanService.getLoanDetails(mobileNumber);
        return ResponseUtils.getSuccessResponse(loan);
    }

    @Operation(
            summary = "Update loan details",
            description = "Endpoint to update loan details associated with a provided card number",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Loan details updated successfully",
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
                            description = "Loan associated with the given card number not found",
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
    @PutMapping("/{mobileNumber}")
    public ResponseEntity<ResponseDto> updateLoan(
            @PathVariable("mobileNumber") String mobileNumber,
            @Valid @RequestBody LoanDto updatedLoanDetails
    ) {
        this.loanService.updateLoan(updatedLoanDetails, mobileNumber);

        String responseMsg = "Details updated successfully";
        return ResponseUtils.getSuccessResponse(responseMsg);
    }

    @Operation(
            summary = "Delete loan details",
            description = "Endpoint to delete loan details associated with a provided mobile number",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Loan details deleted successfully",
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
    @DeleteMapping("/{mobileNumber}")
    public ResponseEntity<ResponseDto> deleteLoan(@PathVariable("mobileNumber") String mobileNumber) {
        this.loanService.deleteLoan(mobileNumber);

        String responseMsg = "Loan deleted successfully";
        return ResponseUtils.getSuccessResponse(responseMsg);
    }
}
