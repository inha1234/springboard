package com.springboard.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("local")
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SpringBoard API")
                        .version("1.0")
                        .description("JWT 기반 인증을 위한 Swagger 문서입니다."))
                .addSecurityItem(new SecurityRequirement()
                        .addList("AccessToken")
                        .addList("RefreshToken"))
                .components(new Components()
                        .addSecuritySchemes("AccessToken", new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name("Authorization")
                                .description("Access Token 입력 (예: Bearer eyJ...)"))
                        .addSecuritySchemes("RefreshToken", new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name("RefreshToken")
                                .description("Refresh Token 입력 (예: Bearer eyJ...)")));
    }
}
