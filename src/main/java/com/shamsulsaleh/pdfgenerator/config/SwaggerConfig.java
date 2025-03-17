package com.shamsulsaleh.pdfgenerator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI springOpenAPI() {
        Contact contact = new Contact();
        contact.setName("Shamsul Saleh");
        contact.setEmail("shamsul.saleh@gmail.com");
        contact.setUrl("https://shamsulsaleh.com");
        return new OpenAPI()
                .info(new Info().title("PDFGenerator API")
                        .description("PDFGenerator Microservices")
                        .version("v1.0.0").contact(contact));
    }

}
