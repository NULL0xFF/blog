package kr.null0xff.blog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for Post creation requests
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "PostCreateRequest", description = "Request model for creating a new blog post")
public class PostCreateRequest {

  @Schema(description = "Title of the blog post", example = "Introduction to Spring Boot", requiredMode = Schema.RequiredMode.REQUIRED)
  @NotBlank(message = "Title is required")
  @Size(max = 200, message = "Title cannot exceed 200 characters")
  private String title;

  @Schema(description = "URL-friendly slug for the post", example = "introduction-to-spring-boot", requiredMode = Schema.RequiredMode.NOT_REQUIRED,
      defaultValue = "Auto-generated from title if not provided")
  @Size(max = 200, message = "Slug cannot exceed 200 characters")
  private String slug;

  @Schema(description = "Brief description or excerpt of the post", example = "Learn the basics of Spring Boot and how to create your first application",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @NotBlank(message = "Description is required")
  @Size(max = 500, message = "Description cannot exceed 500 characters")
  private String description;

  @Schema(description = "Full content of the blog post in Markdown format", example = "# Introduction\n\nSpring Boot is a framework...",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @NotBlank(message = "Content is required")
  private String content;

  @Schema(description = "URL to the featured image for the post", example = "https://example.com/images/spring-boot.jpg",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @Size(max = 255, message = "Image URL cannot exceed 255 characters")
  private String imageUrl;

  @Schema(description = "Flag indicating whether the post should be published immediately", example = "false",
      defaultValue = "false")
  private boolean published;

  @Schema(description = "ID of the user who authors the post", example = "1",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @NotNull(message = "Author ID is required")
  private Long authorId;

  @Schema(description = "ID of the category this post belongs to", example = "3",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  private Long categoryId;

  @Schema(description = "Set of tag names to associate with this post", example = "[\"Spring\", \"Java\", \"Tutorial\"]",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  private Set<String> tags;
}