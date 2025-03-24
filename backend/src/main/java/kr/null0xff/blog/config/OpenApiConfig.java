package kr.null0xff.blog.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for Swagger/OpenAPI documentation.
 */
@Configuration
public class OpenApiConfig {

  /**
   * Creates and configures the OpenAPI definition for the application.
   *
   * @return The configured OpenAPI instance
   */
  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("Blog API")
            .description("RESTful API for a blog application built with Spring Boot")
            .version("v1.0.0")
            .contact(new Contact()
                .name("Null0xFF")
                .url("https://null0xff.kr")
                .email("admin@null0xff.kr"))
            .license(new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT")))
        .servers(List.of(
            new Server().url("/").description("Default Server URL")))
        .components(new Components()
            .addSecuritySchemes("bearer-jwt", new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("Enter JWT Bearer token **_only_**")));
  }
}