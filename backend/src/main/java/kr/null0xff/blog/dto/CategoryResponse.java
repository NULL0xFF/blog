package kr.null0xff.blog.dto;

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
public class CategoryResponse {

  private Long id;
  private String name;
  private String description;
  private String slug;
  private String color;
  private LocalDateTime createdAt;
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
