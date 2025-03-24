package kr.null0xff.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Comment Management", description = "APIs for managing blog comments and moderation")
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
  @Operation(summary = "Get approved comments for a post",
      description = "Retrieves approved comments for a specific post with pagination")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved comments",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = CommentResponse.class))),
      @ApiResponse(responseCode = "404", description = "Post not found",
          content = @Content)
  })
  @GetMapping("/post/{postId}")
  public ResponseEntity<Page<CommentResponse>> getApprovedCommentsForPost(
      @Parameter(description = "Post ID", required = true)
      @PathVariable Long postId,
      @Parameter(description = "Page number (0-based)")
      @RequestParam(defaultValue = "0") int page,
      @Parameter(description = "Number of items per page")
      @RequestParam(defaultValue = "20") int size,
      @Parameter(description = "Sort field")
      @RequestParam(defaultValue = "createdAt") String sortBy,
      @Parameter(description = "Sort direction (asc or desc)")
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
  @Operation(summary = "Get top-level comments for a post",
      description = "Retrieves only top-level comments (not replies) for a specific post with pagination")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved top-level comments",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = CommentResponse.class))),
      @ApiResponse(responseCode = "404", description = "Post not found",
          content = @Content)
  })
  @GetMapping("/post/{postId}/top-level")
  public ResponseEntity<Page<CommentResponse>> getTopLevelCommentsForPost(
      @Parameter(description = "Post ID", required = true)
      @PathVariable Long postId,
      @Parameter(description = "Page number (0-based)")
      @RequestParam(defaultValue = "0") int page,
      @Parameter(description = "Number of items per page")
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
  @Operation(summary = "Get replies to a comment",
      description = "Retrieves replies to a specific comment with pagination")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved replies",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = CommentResponse.class))),
      @ApiResponse(responseCode = "404", description = "Comment not found",
          content = @Content)
  })
  @GetMapping("/{commentId}/replies")
  public ResponseEntity<Page<CommentResponse>> getRepliesForComment(
      @Parameter(description = "Comment ID", required = true)
      @PathVariable Long commentId,
      @Parameter(description = "Page number (0-based)")
      @RequestParam(defaultValue = "0") int page,
      @Parameter(description = "Number of items per page")
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
  @Operation(summary = "Get comment by ID",
      description = "Retrieves a specific comment by its ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved the comment",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = CommentResponse.class))),
      @ApiResponse(responseCode = "404", description = "Comment not found",
          content = @Content)
  })
  @GetMapping("/{id}")
  public ResponseEntity<CommentResponse> getCommentById(
      @Parameter(description = "Comment ID", required = true)
      @PathVariable Long id) {
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
  @Operation(summary = "Get comments for moderation",
      description = "Retrieves all comments pending approval for moderation with pagination")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved comments for moderation",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = CommentResponse.class)))
  })
  @GetMapping("/moderation")
  public ResponseEntity<Page<CommentResponse>> getCommentsForModeration(
      @Parameter(description = "Page number (0-based)")
      @RequestParam(defaultValue = "0") int page,
      @Parameter(description = "Number of items per page")
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
  @Operation(summary = "Count comments for moderation",
      description = "Returns the number of comments pending approval")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved count",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = Long.class)))
  })
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
  @Operation(summary = "Get comments with replies",
      description = "Retrieves comments with their replies for a post in an efficient manner")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved comments with replies",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = CommentResponse.class))),
      @ApiResponse(responseCode = "404", description = "Post not found",
          content = @Content)
  })
  @GetMapping("/post/{postId}/with-replies")
  public ResponseEntity<List<CommentResponse>> getCommentsWithReplies(
      @Parameter(description = "Post ID", required = true)
      @PathVariable Long postId,
      @Parameter(description = "Page number (0-based)")
      @RequestParam(defaultValue = "0") int page,
      @Parameter(description = "Number of items per page")
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
  @Operation(summary = "Create a new comment",
      description = "Creates a new comment or reply on a post")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Comment successfully created",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = CommentResponse.class))),
      @ApiResponse(responseCode = "400", description = "Invalid input",
          content = @Content),
      @ApiResponse(responseCode = "404", description = "Post, author, or parent comment not found",
          content = @Content)
  })
  @PostMapping
  public ResponseEntity<CommentResponse> createComment(
      @Parameter(description = "Comment details", required = true)
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
  @Operation(summary = "Update a comment",
      description = "Updates the content of an existing comment")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Comment successfully updated",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = CommentResponse.class))),
      @ApiResponse(responseCode = "400", description = "Invalid input",
          content = @Content),
      @ApiResponse(responseCode = "404", description = "Comment not found",
          content = @Content)
  })
  @PutMapping("/{id}")
  public ResponseEntity<CommentResponse> updateComment(
      @Parameter(description = "Comment ID", required = true)
      @PathVariable Long id,
      @Parameter(description = "Updated comment details", required = true)
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
  @Operation(summary = "Approve a comment",
      description = "Approves a comment for public display")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Comment successfully approved",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = CommentResponse.class))),
      @ApiResponse(responseCode = "404", description = "Comment not found",
          content = @Content)
  })
  @PutMapping("/{id}/approve")
  public ResponseEntity<CommentResponse> approveComment(
      @Parameter(description = "Comment ID", required = true)
      @PathVariable Long id) {
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
  @Operation(summary = "Reject a comment",
      description = "Rejects and deletes a comment during moderation")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Comment successfully rejected",
          content = @Content),
      @ApiResponse(responseCode = "404", description = "Comment not found",
          content = @Content)
  })
  @DeleteMapping("/{id}/reject")
  public ResponseEntity<Void> rejectComment(
      @Parameter(description = "Comment ID", required = true)
      @PathVariable Long id) {
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
  @Operation(summary = "Delete a comment",
      description = "Deletes a comment and all its replies")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Comment successfully deleted",
          content = @Content),
      @ApiResponse(responseCode = "404", description = "Comment not found",
          content = @Content)
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteComment(
      @Parameter(description = "Comment ID", required = true)
      @PathVariable Long id) {
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
  @Operation(summary = "Get recent comments",
      description = "Retrieves recent comments across all posts")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved recent comments",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = CommentResponse.class)))
  })
  @GetMapping("/recent")
  public ResponseEntity<Page<CommentResponse>> getRecentComments(
      @Parameter(description = "Page number (0-based)")
      @RequestParam(defaultValue = "0") int page,
      @Parameter(description = "Number of items per page")
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
  @Operation(summary = "Count comments for a post",
      description = "Counts the number of approved comments for a specific post")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Count retrieved successfully",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = Long.class))),
      @ApiResponse(responseCode = "404", description = "Post not found",
          content = @Content)
  })
  @GetMapping("/post/{postId}/count")
  public ResponseEntity<Long> countCommentsForPost(
      @Parameter(description = "Post ID", required = true)
      @PathVariable Long postId) {
    log.info("Counting comments for post ID: {}", postId);

    long count = commentService.countCommentsForPost(postId);

    return ResponseEntity.ok(count);
  }
}