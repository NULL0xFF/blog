package kr.null0xff.blog.dto;

import kr.null0xff.blog.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Summary DTO for Tag entities (used in nested responses)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagSummaryResponse {

  private Long id;
  private String name;
  private String slug;

  /**
   * Convert a Tag entity to a TagSummaryResponse DTO
   */
  public static TagSummaryResponse fromEntity(Tag tag) {
    if (tag == null) {
      return null;
    }

    return TagSummaryResponse.builder()
        .id(tag.getId())
        .name(tag.getName())
        .slug(tag.getSlug())
        .build();
  }
}