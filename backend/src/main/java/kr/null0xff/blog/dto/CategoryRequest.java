package kr.null0xff.blog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for Category requests (both creation and update)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "CategoryRequest", description = "Request model for creating or updating a category")
public class CategoryRequest {

  @Schema(description = "Name of the category", example = "Technology", requiredMode = Schema.RequiredMode.REQUIRED)
  @NotBlank(message = "Name is required")
  @Size(max = 50, message = "Name cannot exceed 50 characters")
  private String name;

  @Schema(description = "Description of the category", example = "Articles about technology, programming, and digital innovation", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @Size(max = 200, message = "Description cannot exceed 200 characters")
  private String description;

  @Schema(description = "URL-friendly slug for the category", example = "technology", requiredMode = Schema.RequiredMode.NOT_REQUIRED,
      defaultValue = "Auto-generated from name if not provided")
  @Size(max = 200, message = "Slug cannot exceed 200 characters")
  private String slug;

  @Schema(description = "Color code for the category (hex format)", example = "#3498DB", requiredMode = Schema.RequiredMode.NOT_REQUIRED,
      pattern = "^#[0-9A-Fa-f]{6}$")
  @Size(min = 7, max = 7, message = "Color must be a 7-character hex code (including #)")
  private String color;
}