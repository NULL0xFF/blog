package kr.null0xff.blog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "TagSummaryResponse", description = "Simplified response model for tag data when embedded in other responses")
public class TagSummaryResponse {

  @Schema(description = "Unique identifier of the tag", example = "15")
  private Long id;

  @Schema(description = "Name of the tag", example = "Spring Boot")
  private String name;

  @Schema(description = "URL-friendly slug for the tag", example = "spring-boot")
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