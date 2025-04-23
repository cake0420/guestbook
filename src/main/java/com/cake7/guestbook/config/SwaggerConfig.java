package com.cake7.guestbook.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        Info info = new Info()
                        .title("my title")
                        .description("my description")
                        .version("1.0.0");
//        String jwtSchemaName = "JWT TOKEN";
//        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemaName);
//        Components components = new Components()
//                .addSecuritySchemes(jwtSchemaName, new SecurityScheme()
//                        .name(jwtSchemaName)
//                        .type(SecurityScheme.Type.HTTP)
//                        .scheme("bearer")
//                        .bearerFormat("JWT"));

        String jwtCookieName = "jwt_token";

        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtCookieName);
        Components components = new Components()
                .addSecuritySchemes(jwtCookieName,
                        new SecurityScheme()
                                .name(jwtCookieName)
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.COOKIE)  // ← 핵심!
                );

        return new OpenAPI()
                .addServersItem(new Server().url("/"))
                .info(info)
                .addSecurityItem(securityRequirement)
                .components(components);

    }
}
