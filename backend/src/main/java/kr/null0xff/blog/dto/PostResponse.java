package kr.null0xff.blog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "PostResponse", description = "Response model for blog post data")
public class PostResponse {

  @Schema(description = "Unique identifier of the post", example = "1")
  private Long id;

  @Schema(description = "Title of the blog post", example = "Introduction to Spring Boot")
  private String title;

  @Schema(description = "URL-friendly slug for the post", example = "introduction-to-spring-boot")
  private String slug;

  @Schema(description = "Brief description or excerpt of the post", example = "Learn the basics of Spring Boot and how to create your first application")
  private String description;

  @Schema(description = "Full content of the blog post in Markdown format", example = "# Introduction\n\nSpring Boot is a framework...")
  private String content;

  @Schema(description = "URL to the featured image for the post", example = "https://example.com/images/spring-boot.jpg")
  private String imageUrl;

  @Schema(description = "Flag indicating whether the post is published", example = "true")
  private boolean published;

  @Schema(description = "Date and time when the post was created", example = "2023-03-15T10:15:30")
  private LocalDateTime createdAt;

  @Schema(description = "Date and time when the post was last updated", example = "2023-03-20T14:25:10")
  private LocalDateTime updatedAt;

  @Schema(description = "Date and time when the post was published", example = "2023-03-16T09:00:00")
  private LocalDateTime publishedAt;

  @Schema(description = "Summary information about the post's author")
  private UserSummaryResponse author;

  @Schema(description = "Information about the category this post belongs to")
  private CategorySummaryResponse category;

  @Schema(description = "Set of tags associated with this post")
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