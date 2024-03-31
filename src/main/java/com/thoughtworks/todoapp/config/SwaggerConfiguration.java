package com.thoughtworks.todoapp.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI toDoOpenAPI(){
        String scheme="Token";
        return new OpenAPI()
                .info(new Info()
                        .title("ToDo Application API")
                        .description("These are ToDo Application apis developed and maintained by Thoughtworks")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Thoughtworks")
                                .email("Hiring-Email")
                                .url("Thoughtworks-Url"))
                        .license(new License()
                                .name("License-name")
                                .url("License-url")
                            )
                ).addSecurityItem(new SecurityRequirement().addList(scheme))
                .components(new Components()
                        .addSecuritySchemes(scheme,new SecurityScheme()
                                .name(scheme)
                                .type(SecurityScheme.Type.HTTP)
                                .bearerFormat("JWT")
                                .scheme("bearer")
                        )
                );


    }
}
