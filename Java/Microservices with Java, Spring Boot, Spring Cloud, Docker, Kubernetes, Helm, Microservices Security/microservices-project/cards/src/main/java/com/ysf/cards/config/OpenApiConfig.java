package com.ysf.cards.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Cards Microservice for EazyBank",
                description = "REST API for managing cards in EazyBank",
                version = "v1",
                contact = @Contact(
                        name = "EazyBank",
                        url = "http://eazybank.com",
                        email = "eazybank@email.com"
                ),
                license = @License(
                        name = "License (MIT)",
                        url = "https://choosealicense.com/licenses/mit/"
                )
        ),
        servers = {
                @Server(url = "http://localhost:9000", description = "Localhost")
        }
)
public class OpenApiConfig {
}
