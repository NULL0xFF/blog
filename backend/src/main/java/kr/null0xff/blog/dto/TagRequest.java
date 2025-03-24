package kr.null0xff.blog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for Tag requests (both creation and update)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "TagRequest", description = "Request model for creating or updating a tag")
public class TagRequest {

  @Schema(description = "Name of the tag", example = "Spring Boot", requiredMode = Schema.RequiredMode.REQUIRED)
  @NotBlank(message = "Name is required")
  @Size(max = 50, message = "Name cannot exceed 50 characters")
  private String name;

  @Schema(description = "Description of the tag", example = "Topics related to Spring Boot framework",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @Size(max = 200, message = "Description cannot exceed 200 characters")
  private String description;

  @Schema(description = "URL-friendly slug for the tag", example = "spring-boot",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED,
      defaultValue = "Auto-generated from name if not provided")
  @Size(max = 200, message = "Slug cannot exceed 200 characters")
  private String slug;
}