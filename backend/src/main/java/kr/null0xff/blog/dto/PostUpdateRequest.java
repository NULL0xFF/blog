package kr.null0xff.blog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for Post update requests
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "PostUpdateRequest", description = "Request model for updating an existing blog post")
public class PostUpdateRequest {

  @Schema(description = "Updated title of the blog post", example = "Introduction to Spring Boot 3.0",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @NotBlank(message = "Title is required")
  @Size(max = 200, message = "Title cannot exceed 200 characters")
  private String title;

  @Schema(description = "Updated URL-friendly slug for the post", example = "introduction-to-spring-boot-3-0",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED,
      defaultValue = "Auto-generated from title if not provided")
  @Size(max = 200, message = "Slug cannot exceed 200 characters")
  private String slug;

  @Schema(description = "Updated brief description or excerpt of the post",
      example = "Discover the new features and improvements in Spring Boot 3.0 and how to migrate your applications",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @NotBlank(message = "Description is required")
  @Size(max = 500, message = "Description cannot exceed 500 characters")
  private String description;

  @Schema(description = "Updated full content of the blog post in Markdown format",
      example = "# Introduction\n\nSpring Boot 3.0 brings several exciting new features...",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @NotBlank(message = "Content is required")
  private String content;

  @Schema(description = "Updated URL to the featured image for the post",
      example = "https://example.com/images/spring-boot-3.jpg",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @Size(max = 255, message = "Image URL cannot exceed 255 characters")
  private String imageUrl;

  @Schema(description = "Updated flag indicating whether the post should be published",
      example = "true")
  private boolean published;

  @Schema(description = "Updated ID of the category this post belongs to",
      example = "3",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  private Long categoryId;

  @Schema(description = "Updated set of tag names to associate with this post",
      example = "[\"Spring\", \"Java\", \"Tutorial\", \"Spring Boot 3\"]",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  private Set<String> tags;
}