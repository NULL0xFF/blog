package kr.null0xff.blog.dto;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import kr.null0xff.blog.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for Post responses
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {

  private Long id;
  private String title;
  private String slug;
  private String description;
  private String content;
  private String imageUrl;
  private boolean published;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private LocalDateTime publishedAt;

  private UserSummaryResponse author;
  private CategorySummaryResponse category;
  private Set<TagSummaryResponse> tags;

  /**
   * Convert a Post entity to a PostResponse DTO
   */
  public static PostResponse fromEntity(Post post) {
    if (post == null) {
      return null;
    }

    return PostResponse.builder()
        .id(post.getId())
        .title(post.getTitle())
        .slug(post.getSlug())
        .description(post.getDescription())
        .content(post.getContent())
        .imageUrl(post.getImageUrl())
        .published(post.isPublished())
        .createdAt(post.getCreatedAt())
        .updatedAt(post.getUpdatedAt())
        .publishedAt(post.getPublishedAt())
        .author(UserSummaryResponse.fromEntity(post.getAuthor()))
        .category(CategorySummaryResponse.fromEntity(post.getCategory()))
        .tags(post.getTags().stream()
            .map(TagSummaryResponse::fromEntity)
            .collect(Collectors.toSet()))
        .build();
  }
}
