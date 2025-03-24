package kr.null0xff.blog.dto;

import java.time.LocalDateTime;
import kr.null0xff.blog.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for Category responses with post count
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryWithPostCountResponse {

  private Long id;
  private String name;
  private String description;
  private String slug;
  private String color;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private Long postCount;

  /**
   * Convert a Category entity and post count to a CategoryWithPostCountResponse DTO
   */
  public static CategoryWithPostCountResponse fromEntityAndCount(Category category,
      Long postCount) {
    if (category == null) {
      return null;
    }

    return CategoryWithPostCountResponse.builder()
        .id(category.getId())
        .name(category.getName())
        .description(category.getDescription())
        .slug(category.getSlug())
        .color(category.getColor())
        .createdAt(category.getCreatedAt())
        .updatedAt(category.getUpdatedAt())
        .postCount(postCount)
        .build();
  }
}
