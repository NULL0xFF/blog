package kr.null0xff.blog.dto;

import java.time.LocalDateTime;
import kr.null0xff.blog.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for Tag responses
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagResponse {

  private Long id;
  private String name;
  private String description;
  private String slug;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  /**
   * Convert a Tag entity to a TagResponse DTO
   */
  public static TagResponse fromEntity(Tag tag) {
    if (tag == null) {
      return null;
    }

    return TagResponse.builder()
        .id(tag.getId())
        .name(tag.getName())
        .description(tag.getDescription())
        .slug(tag.getSlug())
        .createdAt(tag.getCreatedAt())
        .updatedAt(tag.getUpdatedAt())
        .build();
  }
}
