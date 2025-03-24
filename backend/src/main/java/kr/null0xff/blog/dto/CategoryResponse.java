package kr.null0xff.blog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import kr.null0xff.blog.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for Category responses
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "CategoryResponse", description = "Response model for category data")
public class CategoryResponse {

  @Schema(description = "Unique identifier of the category", example = "1")
  private Long id;

  @Schema(description = "Name of the category", example = "Technology")
  private String name;

  @Schema(description = "Description of the category", example = "Articles about technology, programming, and digital innovation")
  private String description;

  @Schema(description = "URL-friendly slug for the category", example = "technology")
  private String slug;

  @Schema(description = "Color code for the category (hex format)", example = "#3498DB")
  private String color;

  @Schema(description = "Date and time when the category was created", example = "2023-01-15T12:00:00")
  private LocalDateTime createdAt;

  @Schema(description = "Date and time when the category was last updated", example = "2023-02-20T15:30:00")
  private LocalDateTime updatedAt;

  /**
   * Convert a Category entity to a CategoryResponse DTO
   */
  public static CategoryResponse fromEntity(Category category) {
    if (category == null) {
      return null;
    }

    return CategoryResponse.builder()
        .id(category.getId())
        .name(category.getName())
        .description(category.getDescription())
        .slug(category.getSlug())
        .color(category.getColor())
        .createdAt(category.getCreatedAt())
        .updatedAt(category.getUpdatedAt())
        .build();
  }
}