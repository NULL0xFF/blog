package kr.null0xff.blog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kr.null0xff.blog.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for Comment responses
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "CommentResponse", description = "Response model for comment data")
public class CommentResponse {

  @Schema(description = "Unique identifier of the comment", example = "123")
  private Long id;

  @Schema(description = "Text content of the comment", example = "Great article! I learned a lot from it.")
  private String content;

  @Schema(description = "Flag indicating whether the comment has been approved by moderation", example = "true")
  private boolean approved;

  @Schema(description = "Date and time when the comment was created", example = "2023-04-12T18:25:43")
  private LocalDateTime createdAt;

  @Schema(description = "Date and time when the comment was last updated", example = "2023-04-12T19:30:15")
  private LocalDateTime updatedAt;

  @Schema(description = "Information about the comment author")
  private UserSummaryResponse author;

  @Schema(description = "ID of the post this comment belongs to", example = "42")
  private Long postId;

  @Schema(description = "ID of the parent comment (if this is a reply)", example = "100", nullable = true)
  private Long parentId;

  @Schema(description = "List of replies to this comment", nullable = true)
  private List<CommentResponse> replies;

  /**
   * Convert a Comment entity to a CommentResponse DTO (without replies)
   */
  public static CommentResponse fromEntity(Comment comment) {
    if (comment == null) {
      return null;
    }

    return CommentResponse.builder()
        .id(comment.getId())
        .content(comment.getContent())
        .approved(comment.isApproved())
        .createdAt(comment.getCreatedAt())
        .updatedAt(comment.getUpdatedAt())
        .author(UserSummaryResponse.fromEntity(comment.getAuthor()))
        .postId(comment.getPost().getId())
        .parentId(comment.getParent() != null ? comment.getParent().getId() : null)
        .replies(new ArrayList<>()) // Empty list for replies
        .build();
  }

  /**
   * Convert a Comment entity to a CommentResponse DTO with its replies
   */
  public static CommentResponse fromEntityWithReplies(Comment comment) {
    if (comment == null) {
      return null;
    }

    CommentResponse response = fromEntity(comment);

    if (comment.getReplies() != null && !comment.getReplies().isEmpty()) {
      response.setReplies(comment.getReplies().stream()
          .map(CommentResponse::fromEntity)
          .collect(Collectors.toList()));
    }

    return response;
  }
}