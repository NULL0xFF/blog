package kr.null0xff.blog.dto;

import java.time.LocalDateTime;
import kr.null0xff.blog.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for Tag responses with post count
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagWithPostCountResponse {

  private Long id;
  private String name;
  private String description;
  private String slug;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private Long postCount;

  /**
   * Convert a Tag entity and post count to a TagWithPostCountResponse DTO
   */
  public static TagWithPostCountResponse fromEntityAndCount(Tag tag, Long postCount) {
    if (tag == null) {
      return null;
    }

    return TagWithPostCountResponse.builder()
        .id(tag.getId())
        .name(tag.getName())
        .description(tag.getDescription())
        .slug(tag.getSlug())
        .createdAt(tag.getCreatedAt())
        .updatedAt(tag.getUpdatedAt())
        .postCount(postCount)
        .build();
  }
}