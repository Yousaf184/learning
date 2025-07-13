package com.ysf.cards.controller;

import com.ysf.cards.dto.AccountContactInfoDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/meta-info")
public class MetaInfoController {

    @Value("${build.version}")
    private String buildVersion;

    private final Environment environment;

    private final AccountContactInfoDto accountContactInfoDto;

    public MetaInfoController(AccountContactInfoDto accountContactInfoDto, Environment env) {
        this.accountContactInfoDto = accountContactInfoDto;
        this.environment = env;
    }

    @Operation(
            summary = "Get Build information",
            description = "Endpoint for getting the build information that is deployed into cards microservice",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Build information retrieved successfully"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Error while fetching build information"
                    )
            }
    )
    @GetMapping("/build-info")
    public ResponseEntity<String> getBuildInfo() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(buildVersion);
    }

    @Operation(
            summary = "Get Java version",
            description = "Endpoint for getting the java version that is installed on the server running the cards microservice",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Java version information retrieved successfully"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Error while fetching java version information"
                    )
            }
    )
    @GetMapping("/java-version")
    public ResponseEntity<String> getJavaVersion() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(environment.getProperty("JAVA_HOME"));
    }

    @Operation(
            summary = "Get contact information",
            description = "Endpoint for getting the contact information that can be reached out in case of any issues",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Contact information retrieved successfully"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Error while fetching contact information"
                    )
            }
    )
    @GetMapping("/contact-info")
    public ResponseEntity<AccountContactInfoDto> getContactInfo() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(accountContactInfoDto);
    }
}
