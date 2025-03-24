package kr.null0xff.blog.dto;

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
public class CategorySummaryResponse {

  private Long id;
  private String name;
  private String slug;
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
