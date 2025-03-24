package kr.null0xff.blog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.null0xff.blog.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Summary DTO for Category entities (used in nested responses)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "CategorySummaryResponse", description = "Simplified response model for category data when embedded in other responses")
public class CategorySummaryResponse {

  @Schema(description = "Unique identifier of the category", example = "1")
  private Long id;

  @Schema(description = "Name of the category", example = "Technology")
  private String name;

  @Schema(description = "URL-friendly slug for the category", example = "technology")
  private String slug;

  @Schema(description = "Color code for the category (hex format)", example = "#3498DB")
  private String color;

  /**
   * Convert a Category entity to a CategorySummaryResponse DTO
   */
  public static CategorySummaryResponse fromEntity(Category category) {
    if (category == null) {
      return null;
    }

    return CategorySummaryResponse.builder()
        .id(category.getId())
        .name(category.getName())
        .slug(category.getSlug())
        .color(category.getColor())
        .build();
  }
}