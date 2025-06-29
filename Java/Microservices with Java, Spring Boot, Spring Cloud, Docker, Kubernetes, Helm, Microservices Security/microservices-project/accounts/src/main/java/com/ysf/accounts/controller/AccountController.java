package com.ysf.accounts.controller;

import com.ysf.accounts.dto.AccountDetailsDto;
import com.ysf.accounts.dto.AccountDto;
import com.ysf.accounts.dto.CustomerDto;
import com.ysf.accounts.dto.ResponseDto;
import com.ysf.accounts.service.IAccountService;
import com.ysf.accounts.utils.ResponseUtils;
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
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
@Tag(name = "Account REST API", description = "REST API for managing accounts in EazyBank")
public class AccountController {

    private final IAccountService accountService;

    @PostMapping
    @Operation(
            summary = "Create new account",
            description = "This endpoint will create a new account, saving the customer details along with the newly created account details",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Account created successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schemaProperties = {
                                            @SchemaProperty(name = "status", schema = @Schema(type = "string", implementation = ResponseUtils.ResponseStatus.class)),
                                            @SchemaProperty(name = "message", schema = @Schema(type = "string", implementation = String.class)),
                                            @SchemaProperty(name = "data", schema = @Schema(type = "object", implementation = AccountDto.class)),
                                    },
                                    schema = @Schema(
                                            example = """
                                                    {
                                                      "status": "SUCCESS",
                                                      "message": "Account created successfully",
                                                      "data": {
                                                        "accountNumber": 2860838996,
                                                        "accountType": "CURRENT",
                                                        "branchAddress": "123 Test Street, NY, USA"
                                                      }
                                                    }"""
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Account creation failed because customer already exists",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schemaProperties = {
                                            @SchemaProperty(name = "status", schema = @Schema(type = "string")),
                                            @SchemaProperty(name = "message", schema = @Schema(type = "string"))
                                    },
                                    schema = @Schema(
                                            example = """
                                                    {
                                                        "status": "ERROR",
                                                        "message": "Customer already has an existing account"
                                                    }"""
                                    )
                            )
                    )
            }
    )
    public ResponseEntity<ResponseDto> createNewAccount(@Valid @RequestBody CustomerDto customerDetails) {
        AccountDto createdAccount = this.accountService.createNewAccount(customerDetails);

        String successResponseMsg = "Account created successfully";
        return ResponseUtils.getCreatedSuccessResponse(createdAccount, successResponseMsg);
    }

    @GetMapping("/{mobileNumber}")
    @Operation(
            summary = "Get account details",
            description = "This endpoint will return accounts details associated with a provided mobile number",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Account details retrieved successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schemaProperties = {
                                            @SchemaProperty(name = "status", schema = @Schema(type = "string", implementation = ResponseUtils.ResponseStatus.class)),
                                            @SchemaProperty(name = "data", schema = @Schema(type = "object", implementation = AccountDetailsDto.class))
                                    },
                                    schema = @Schema(
                                            example = """
                                                    {
                                                        "status": "SUCCESS",
                                                        "data": {
                                                            "customerDetails": {
                                                                "name": "Madan Reddy",
                                                                "email": "tutor@eazybytes",
                                                                "mobileNumber": "4354437687"
                                                            },
                                                            "accountDetails": {
                                                                "accountNumber": 4099473365,
                                                                "accountType": "CURRENT",
                                                                "branchAddress": "123 Test Street, NY, USA"
                                                            }
                                                        }
                                                    }"""
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Customer/Account associated with the given mobile number not found",
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
    public ResponseEntity<ResponseDto> getAccountDetails(@PathVariable("mobileNumber") String mobileNumber) {
        AccountDetailsDto accountDetails = this.accountService.getAccountDetails(mobileNumber);
        return ResponseUtils.getSuccessResponse(accountDetails);
    }

    @PutMapping("/{mobileNumber}")
    @Operation(
            summary = "Update account details",
            description = "This endpoint will update accounts details associated with a provided mobile number",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Account details updated successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schemaProperties = {
                                            @SchemaProperty(name = "status", schema = @Schema(type = "string", implementation = ResponseUtils.ResponseStatus.class)),
                                            @SchemaProperty(name = "message", schema = @Schema(type = "string", implementation = String.class))
                                    },
                                    schema = @Schema(
                                            example = """
                                                    {
                                                        "status": "SUCCESS",
                                                        "message": "Details updated successfully"
                                                    }"""
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = """
                                    - Customer/Account associated with the given mobile number not found
                                    - Request body doesn't contain anything to update
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
    public ResponseEntity<ResponseDto> updateAccountDetails(
        @PathVariable("mobileNumber") String mobileNumber,
        @RequestBody AccountDetailsDto accountDetailsDto
    ) {
        this.accountService.updateAccountDetails(mobileNumber, accountDetailsDto);

        String responseMsg = "Details updated successfully";
        return ResponseUtils.getSuccessResponse(responseMsg);
    }

    @DeleteMapping("/{mobileNumber}")
    @Operation(
            summary = "Delete account details",
            description = "This endpoint will delete accounts details associated with a provided mobile number",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Account details deleted successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schemaProperties = {
                                            @SchemaProperty(name = "status", schema = @Schema(type = "string", implementation = ResponseUtils.ResponseStatus.class)),
                                            @SchemaProperty(name = "message", schema = @Schema(type = "string", implementation = String.class))
                                    },
                                    schema = @Schema(
                                            example = """
                                                    {
                                                        "status": "SUCCESS",
                                                        "message": "Account deleted successfully"
                                                    }"""
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Customer/Account associated with the given mobile number not found",
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
    public ResponseEntity<ResponseDto> deleteAccountDetails(@PathVariable("mobileNumber") String mobileNumber) {
        this.accountService.deleteAccountDetails(mobileNumber);

        String responseMsg = "Account deleted successfully";
        return ResponseUtils.getSuccessResponse(responseMsg);
    }
}

