package kr.null0xff.blog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for Comment update requests
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "CommentUpdateRequest", description = "Request model for updating an existing comment")
public class CommentUpdateRequest {

  @Schema(description = "Updated text content of the comment",
      example = "I really enjoyed this article! The code examples were very helpful.",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @NotBlank(message = "Content is required")
  private String content;
}