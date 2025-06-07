package org.deltacore.delta.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class SwaggerConfig {

    @Value("${app.api.title}")
    private String title;

    @Value("${app.api.description}")
    private String description;

    @Value("${app.api.version}")
    private String version;

    @Value("${app.api.contact.name}")
    private String contactName;

    @Value("${app.api.contact.email}")
    private String contactEmail;

    @Value("${app.api.license.name}")
    private String licenseName;

    @Value("${app.api.license.url}")
    private String licenseUrl;

    @Value("${app.api.server.prod.url}")
    private String prodUrl;

    @Value("${app.api.server.prod.description}")
    private String prodDesc;

    @Value("${app.api.server.local.url}")
    private String localUrl;

    @Value("${app.api.server.local.description}")
    private String localDesc;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(title)
                        .description(description)
                        .version(version)
                        .contact(new Contact().name(contactName).email(contactEmail))
                        .license(new License().name(licenseName).url(licenseUrl)))
                .addServersItem(new Server().url(prodUrl).description(prodDesc))
                .addServersItem(new Server().url(localUrl).description(localDesc))

                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components().addSecuritySchemes("bearerAuth",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                ));
    }

    @Bean
    public GroupedOpenApi apiGroup() {
        return GroupedOpenApi.builder()
                .group("Activities API")
                .pathsToMatch("/activities/**")
                .packagesToScan("org.deltacore.delta.controller",
                        "org.deltacore.delta.dto")
                .build();
    }

    @Bean
    public GroupedOpenApi authApiGroup() {
        return GroupedOpenApi.builder()
                .group("Auth API")
                .pathsToMatch("/home/**")
                .packagesToScan("org.deltacore.delta.controller",
                        "org.deltacore.delta.dto")
                .build();
    }

}
