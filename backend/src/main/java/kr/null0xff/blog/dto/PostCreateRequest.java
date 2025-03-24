package kr.null0xff.blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for Post creation requests
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostCreateRequest {

  @NotBlank(message = "Title is required")
  @Size(max = 200, message = "Title cannot exceed 200 characters")
  private String title;

  @Size(max = 200, message = "Slug cannot exceed 200 characters")
  private String slug;

  @NotBlank(message = "Description is required")
  @Size(max = 500, message = "Description cannot exceed 500 characters")
  private String description;

  @NotBlank(message = "Content is required")
  private String content;

  @Size(max = 255, message = "Image URL cannot exceed 255 characters")
  private String imageUrl;

  private boolean published;

  @NotNull(message = "Author ID is required")
  private Long authorId;

  private Long categoryId;

  private Set<String> tags;
}
