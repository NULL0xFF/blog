package kr.null0xff.blog.dto;

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
public class CommentUpdateRequest {

  @NotBlank(message = "Content is required")
  private String content;
}
