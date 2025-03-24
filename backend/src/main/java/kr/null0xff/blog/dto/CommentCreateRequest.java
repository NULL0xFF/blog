package kr.null0xff.blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for Comment creation requests
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentCreateRequest {

  @NotBlank(message = "Content is required")
  private String content;

  @NotNull(message = "Post ID is required")
  private Long postId;

  @NotNull(message = "Author ID is required")
  private Long authorId;

  private Long parentCommentId;
}
