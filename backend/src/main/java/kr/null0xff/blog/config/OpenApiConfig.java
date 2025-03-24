package kr.null0xff.blog.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import java.util.Arrays;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for OpenAPI/Swagger documentation. This class defines the global
 * documentation settings for the Blog API.
 */
@Configuration
public class OpenApiConfig {

  /**
   * Creates and configures the OpenAPI definition for the application. Defines API information,
   * servers, security schemes, and organizes APIs by tags.
   *
   * @return The configured OpenAPI instance
   */
  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("Blog API")
            .description("RESTful API for a blog application built with Spring Boot. " +
                "The API provides endpoints for managing blog posts, categories, tags, comments, and users. "
                +
                "It supports features such as content creation, moderation, searching, and basic analytics.")
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
                .description("Enter JWT Bearer token **_only_**")))
        .tags(Arrays.asList(
            new Tag().name("Post Management")
                .description(
                    "APIs for managing blog posts, including creation, publishing, and searching content"),
            new Tag().name("Category Management")
                .description(
                    "APIs for managing blog categories and their relationships with posts"),
            new Tag().name("Tag Management")
                .description(
                    "APIs for managing blog tags, including popular tags and tag association with posts"),
            new Tag().name("Comment Management")
                .description(
                    "APIs for managing blog comments, including creation, moderation, and threading"),
            new Tag().name("User Management")
                .description(
                    "APIs for managing blog users, including registration, authentication, and profile management")
        ));
  }
}