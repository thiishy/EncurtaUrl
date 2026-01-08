package com.thiimont.encurtaurl.configuration;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("EncurtaUrl").version("1.0.0").description("Uma API REST para encurtamento de URLs. A funcionalidade de Authorize do Swagger UI não funciona com cookies, mas o endpoint de login cria e armazena o cookie no seu navegador automaticamente."))
                .components(new Components()
                .addSecuritySchemes("cookieAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.COOKIE)
                                .name("token")));
    }
}
