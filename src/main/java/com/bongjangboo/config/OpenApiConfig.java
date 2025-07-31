package com.bongjangboo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class OpenApiConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public OpenAPI bongjangbooOpenAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .addServersItem(new Server()
                        .url("http://localhost:" + serverPort)
                        .description("로컬 개발 서버"))
                .addServersItem(new Server()
                        .url("https://api.bongjangboo.com")
                        .description("운영 서버"));
    }

    private Info apiInfo() {
        return new Info()
                .title("봉장부 2.0 API")
                .description("대학교 학생회를 위한 디지털 재무 관리 플랫폼 API")
                .version("v1.0.0")
                .contact(new Contact()
                        .name("chaewonJeong")
                        .email("chaewonjeong.dev@gmail.com")
                        .url("https://github.com/bong-jangboo/backend"))
                .license(new License()
                        .name("MIT License")
                        .url("https://opensource.org/licenses/MIT"));
    }

}