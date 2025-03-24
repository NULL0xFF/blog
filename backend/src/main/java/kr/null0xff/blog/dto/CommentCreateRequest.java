package kr.null0xff.blog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "CommentCreateRequest", description = "Request model for creating a new comment or reply")
public class CommentCreateRequest {

  @Schema(description = "Text content of the comment", example = "Great article! I learned a lot from it.",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @NotBlank(message = "Content is required")
  private String content;

  @Schema(description = "ID of the post being commented on", example = "42",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @NotNull(message = "Post ID is required")
  private Long postId;

  @Schema(description = "ID of the user creating the comment", example = "15",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @NotNull(message = "Author ID is required")
  private Long authorId;

  @Schema(description = "ID of the parent comment (if this is a reply)", example = "123",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED,
      nullable = true)
  private Long parentCommentId;
}