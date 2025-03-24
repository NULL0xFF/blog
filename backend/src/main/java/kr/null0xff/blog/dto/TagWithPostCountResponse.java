package kr.null0xff.blog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "TagWithPostCountResponse", description = "Response model for tag data including post count statistics")
public class TagWithPostCountResponse {

  @Schema(description = "Unique identifier of the tag", example = "15")
  private Long id;

  @Schema(description = "Name of the tag", example = "Spring Boot")
  private String name;

  @Schema(description = "Description of the tag", example = "Topics related to Spring Boot framework")
  private String description;

  @Schema(description = "URL-friendly slug for the tag", example = "spring-boot")
  private String slug;

  @Schema(description = "Date and time when the tag was created", example = "2023-02-05T09:15:30")
  private LocalDateTime createdAt;

  @Schema(description = "Date and time when the tag was last updated", example = "2023-03-10T11:20:45")
  private LocalDateTime updatedAt;

  @Schema(description = "Number of published posts using this tag", example = "42")
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