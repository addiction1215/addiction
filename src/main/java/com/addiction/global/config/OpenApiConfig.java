package com.addiction.global.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {

    private static final String BEARER_AUTH = "bearerAuth";

    @Bean
    public GroupedOpenApi userOpenApi() {
        return GroupedOpenApi.builder()
                .group("user")
                .pathsToMatch("/api/v1/user", "/api/v1/user/**")
                .addOpenApiCustomizer(this::customizeUserOpenApi)
                .build();
    }

    private void customizeUserOpenApi(OpenAPI openApi) {
        if (openApi.getComponents() == null) {
            openApi.setComponents(new Components());
        }

        openApi.getComponents().addSecuritySchemes(BEARER_AUTH,
                new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT"));
        openApi.info(new Info()
                .title("QuitMate User API")
                .description("사용자 정보, 설문, 금연 현황 API입니다.")
                .version("v1"));
        openApi.addSecurityItem(new SecurityRequirement().addList(BEARER_AUTH));
    }
}
