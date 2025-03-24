package kr.null0xff.blog.controller;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import kr.null0xff.blog.dto.CommentCreateRequest;
import kr.null0xff.blog.dto.CommentResponse;
import kr.null0xff.blog.dto.CommentUpdateRequest;
import kr.null0xff.blog.entity.Comment;
import kr.null0xff.blog.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@Slf4j
public class CommentController {

  private final CommentService commentService;

  /**
   * Get all approved comments for a post with pagination
   *
   * @param postId Post ID
   * @param page   Page number (0-based)
   * @param size   Number of items per page
   * @return ResponseEntity with a page of comments
   */
  @GetMapping("/post/{postId}")
  public ResponseEntity<Page<CommentResponse>> getApprovedCommentsForPost(
      @PathVariable Long postId,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size,
      @RequestParam(defaultValue = "createdAt") String sortBy,
      @RequestParam(defaultValue = "desc") String direction) {

    log.info("Fetching comments for post ID: {} - page: {}, size: {}", postId, page, size);

    Sort sort = direction.equalsIgnoreCase("asc") ?
        Sort.by(sortBy).ascending() :
        Sort.by(sortBy).descending();

    Pageable pageable = PageRequest.of(page, size, sort);
    Page<Comment> commentsPage = commentService.getApprovedCommentsForPost(postId, pageable);

    // Convert entity to DTO
    Page<CommentResponse> responseBody = commentsPage.map(CommentResponse::fromEntity);

    return ResponseEntity.ok(responseBody);
  }

  /**
   * Get top-level comments (not replies) for a post with pagination
   *
   * @param postId Post ID
   * @param page   Page number (0-based)
   * @param size   Number of items per page
   * @return ResponseEntity with a page of top-level comments
   */
  @GetMapping("/post/{postId}/top-level")
  public ResponseEntity<Page<CommentResponse>> getTopLevelCommentsForPost(
      @PathVariable Long postId,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size) {

    log.info("Fetching top-level comments for post ID: {} - page: {}, size: {}", postId, page,
        size);

    Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
    Page<Comment> commentsPage = commentService.getTopLevelCommentsForPost(postId, pageable);

    // Convert entity to DTO
    Page<CommentResponse> responseBody = commentsPage.map(CommentResponse::fromEntity);

    return ResponseEntity.ok(responseBody);
  }

  /**
   * Get replies to a specific comment with pagination
   *
   * @param commentId Comment ID
   * @param page      Page number (0-based)
   * @param size      Number of items per page
   * @return ResponseEntity with a page of replies
   */
  @GetMapping("/{commentId}/replies")
  public ResponseEntity<Page<CommentResponse>> getRepliesForComment(
      @PathVariable Long commentId,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size) {

    log.info("Fetching replies for comment ID: {} - page: {}, size: {}", commentId, page, size);

    Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());
    Page<Comment> commentsPage = commentService.getRepliesForComment(commentId, pageable);

    // Convert entity to DTO
    Page<CommentResponse> responseBody = commentsPage.map(CommentResponse::fromEntity);

    return ResponseEntity.ok(responseBody);
  }

  /**
   * Get a comment by its ID
   *
   * @param id Comment ID
   * @return ResponseEntity with the comment
   */
  @GetMapping("/{id}")
  public ResponseEntity<CommentResponse> getCommentById(@PathVariable Long id) {
    log.info("Fetching comment with ID: {}", id);

    Comment comment = commentService.getCommentById(id);
    CommentResponse responseBody = CommentResponse.fromEntity(comment);

    return ResponseEntity.ok(responseBody);
  }

  /**
   * Get all comments pending approval (for moderation)
   *
   * @param page Page number (0-based)
   * @param size Number of items per page
   * @return ResponseEntity with a page of unapproved comments
   */
  @GetMapping("/moderation")
  public ResponseEntity<Page<CommentResponse>> getCommentsForModeration(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size) {

    log.info("Fetching comments for moderation - page: {}, size: {}", page, size);

    Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());
    Page<Comment> commentsPage = commentService.getCommentsForModeration(pageable);

    // Convert entity to DTO
    Page<CommentResponse> responseBody = commentsPage.map(CommentResponse::fromEntity);

    return ResponseEntity.ok(responseBody);
  }

  /**
   * Get the number of pending comments for moderation
   *
   * @return ResponseEntity with the count
   */
  @GetMapping("/moderation/count")
  public ResponseEntity<Long> countCommentsForModeration() {
    log.info("Counting comments for moderation");

    long count = commentService.countCommentsForModeration();

    return ResponseEntity.ok(count);
  }

  /**
   * Get comments with their replies in an efficient manner
   *
   * @param postId Post ID
   * @param page   Page number (0-based)
   * @param size   Number of items per page
   * @return ResponseEntity with a list of comments with their replies
   */
  @GetMapping("/post/{postId}/with-replies")
  public ResponseEntity<List<CommentResponse>> getCommentsWithReplies(
      @PathVariable Long postId,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size) {

    log.info("Fetching comments with replies for post ID: {} - page: {}, size: {}", postId, page,
        size);

    Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
    List<Comment> comments = commentService.getCommentsWithReplies(postId, pageable);

    // Convert entity to DTO
    List<CommentResponse> responseBody = comments.stream()
        .map(CommentResponse::fromEntityWithReplies)
        .collect(Collectors.toList());

    return ResponseEntity.ok(responseBody);
  }

  /**
   * Create a new comment
   *
   * @param request Comment creation request
   * @return ResponseEntity with the created comment
   */
  @PostMapping
  public ResponseEntity<CommentResponse> createComment(
      @Valid @RequestBody CommentCreateRequest request) {
    log.info("Creating new comment for post ID: {}", request.getPostId());

    // Create a Comment entity from the request
    Comment comment = new Comment();
    comment.setContent(request.getContent());
    comment.setApproved(false); // New comments require approval by default

    // Create the comment
    Comment createdComment = commentService.createComment(
        comment,
        request.getPostId(),
        request.getAuthorId(),
        request.getParentCommentId());

    // Convert entity to DTO
    CommentResponse responseBody = CommentResponse.fromEntity(createdComment);

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(responseBody);
  }

  /**
   * Update an existing comment
   *
   * @param id      Comment ID
   * @param request Comment update request
   * @return ResponseEntity with the updated comment
   */
  @PutMapping("/{id}")
  public ResponseEntity<CommentResponse> updateComment(
      @PathVariable Long id,
      @Valid @RequestBody CommentUpdateRequest request) {

    log.info("Updating comment with ID: {}", id);

    Comment updatedComment = commentService.updateComment(id, request.getContent());

    // Convert entity to DTO
    CommentResponse responseBody = CommentResponse.fromEntity(updatedComment);

    return ResponseEntity.ok(responseBody);
  }

  /**
   * Approve a comment
   *
   * @param id Comment ID
   * @return ResponseEntity with the approved comment
   */
  @PutMapping("/{id}/approve")
  public ResponseEntity<CommentResponse> approveComment(@PathVariable Long id) {
    log.info("Approving comment with ID: {}", id);

    Comment approvedComment = commentService.approveComment(id);

    // Convert entity to DTO
    CommentResponse responseBody = CommentResponse.fromEntity(approvedComment);

    return ResponseEntity.ok(responseBody);
  }

  /**
   * Reject a comment
   *
   * @param id Comment ID
   * @return ResponseEntity with no content
   */
  @DeleteMapping("/{id}/reject")
  public ResponseEntity<Void> rejectComment(@PathVariable Long id) {
    log.info("Rejecting comment with ID: {}", id);

    commentService.rejectComment(id);

    return ResponseEntity.noContent().build();
  }

  /**
   * Delete a comment
   *
   * @param id Comment ID
   * @return ResponseEntity with no content
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
    log.info("Deleting comment with ID: {}", id);

    commentService.deleteComment(id);

    return ResponseEntity.noContent().build();
  }

  /**
   * Get recent comments across all posts
   *
   * @param page Page number (0-based)
   * @param size Number of items per page
   * @return ResponseEntity with a page of recent comments
   */
  @GetMapping("/recent")
  public ResponseEntity<Page<CommentResponse>> getRecentComments(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {

    log.info("Fetching recent comments - page: {}, size: {}", page, size);

    Pageable pageable = PageRequest.of(page, size);
    Page<Comment> commentsPage = commentService.getRecentComments(pageable);

    // Convert entity to DTO
    Page<CommentResponse> responseBody = commentsPage.map(CommentResponse::fromEntity);

    return ResponseEntity.ok(responseBody);
  }

  /**
   * Count the number of comments for a post
   *
   * @param postId Post ID
   * @return ResponseEntity with the count
   */
  @GetMapping("/post/{postId}/count")
  public ResponseEntity<Long> countCommentsForPost(@PathVariable Long postId) {
    log.info("Counting comments for post ID: {}", postId);

    long count = commentService.countCommentsForPost(postId);

    return ResponseEntity.ok(count);
  }
}