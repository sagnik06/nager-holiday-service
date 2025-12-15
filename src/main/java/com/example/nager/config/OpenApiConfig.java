package com.example.nager.config;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Configuration;
@OpenAPIDefinition(
    info = @Info(title = "Nager Holiday Service API", version = "1.4.0", description = "Reactive REST API for querying public holidays using Nager.Date", contact = @Contact(name = "Sagnik Routh")),
    servers = { @Server(url = "http://localhost:8080", description = "Local Dev") },
    tags = { @Tag(name = "Holidays", description = "Holiday queries") }
)
@Configuration
public class OpenApiConfig { }
