package kr.null0xff.blog.dto;

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
public class CommentResponse {

  private Long id;
  private String content;
  private boolean approved;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  private UserSummaryResponse author;
  private Long postId;
  private Long parentId;

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