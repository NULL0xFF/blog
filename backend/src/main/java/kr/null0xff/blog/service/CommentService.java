package kr.null0xff.blog.service;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kr.null0xff.blog.entity.Comment;
import kr.null0xff.blog.entity.Post;
import kr.null0xff.blog.entity.User;
import kr.null0xff.blog.repository.CommentRepository;
import kr.null0xff.blog.repository.PostRepository;
import kr.null0xff.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentService {

  private final CommentRepository commentRepository;
  private final PostRepository postRepository;
  private final UserRepository userRepository;

  /**
   * Get all approved comments for a post with pagination
   */
  @Transactional(readOnly = true)
  public Page<Comment> getApprovedCommentsForPost(Long postId, Pageable pageable) {
    log.debug("Finding approved comments for post with ID: {}", postId);

    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new EntityNotFoundException("Post not found with ID: " + postId));

    return commentRepository.findByPostAndApprovedTrue(post, pageable);
  }

  /**
   * Get top-level comments (not replies) for a post with pagination
   */
  @Transactional(readOnly = true)
  public Page<Comment> getTopLevelCommentsForPost(Long postId, Pageable pageable) {
    log.debug("Finding top-level comments for post with ID: {}", postId);

    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new EntityNotFoundException("Post not found with ID: " + postId));

    return commentRepository.findByPostAndParentIsNullAndApprovedTrue(post, pageable);
  }

  /**
   * Get replies to a specific comment with pagination
   */
  @Transactional(readOnly = true)
  public Page<Comment> getRepliesForComment(Long commentId, Pageable pageable) {
    log.debug("Finding replies for comment with ID: {}", commentId);

    Comment parentComment = commentRepository.findById(commentId)
        .orElseThrow(() -> new EntityNotFoundException("Comment not found with ID: " + commentId));

    return commentRepository.findByParentAndApprovedTrue(parentComment, pageable);
  }

  /**
   * Get a comment by its ID
   */
  @Transactional(readOnly = true)
  public Comment getCommentById(Long id) {
    log.debug("Finding comment with ID: {}", id);
    return commentRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Comment not found with ID: " + id));
  }

  /**
   * Get all comments pending approval (for moderation)
   */
  @Transactional(readOnly = true)
  public Page<Comment> getCommentsForModeration(Pageable pageable) {
    log.debug("Finding comments for moderation");
    return commentRepository.findByApprovedFalse(pageable);
  }

  /**
   * Get the number of pending comments for moderation
   */
  @Transactional(readOnly = true)
  public long countCommentsForModeration() {
    return commentRepository.countByApprovedFalse();
  }

  /**
   * Get the reply counts for a list of comments This is useful for displaying reply counts in the
   * UI without loading all replies
   */
  @Transactional(readOnly = true)
  public Map<Long, Long> getReplyCountsForComments(List<Comment> comments) {
    List<Long> commentIds = comments.stream()
        .map(Comment::getId)
        .collect(Collectors.toList());

    List<Object[]> results = commentRepository.countRepliesByParentIds(commentIds);

    return results.stream()
        .collect(Collectors.toMap(
            row -> (Long) row[0], // parent comment ID
            row -> (Long) row[1]  // reply count
        ));
  }

  /**
   * Create a new comment
   */
  @Transactional
  public Comment createComment(Comment comment, Long postId, Long authorId, Long parentCommentId) {
    log.debug("Creating new comment for post with ID: {}", postId);

    // Set the post
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new EntityNotFoundException("Post not found with ID: " + postId));
    comment.setPost(post);

    // Set the author
    User author = userRepository.findById(authorId)
        .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + authorId));
    comment.setAuthor(author);

    // Set the parent comment if it's a reply
    if (parentCommentId != null) {
      Comment parentComment = commentRepository.findById(parentCommentId)
          .orElseThrow(() -> new EntityNotFoundException(
              "Parent comment not found with ID: " + parentCommentId));

      // Make sure the parent comment belongs to the same post
      if (!parentComment.getPost().getId().equals(postId)) {
        throw new IllegalArgumentException("Parent comment does not belong to the specified post");
      }

      comment.setParent(parentComment);
    }

    // By default, comments require approval unless specified otherwise
    if (!comment.isApproved()) {
      comment.setApproved(false);
    }

    return commentRepository.save(comment);
  }

  /**
   * Update an existing comment This would typically only allow updating the content
   */
  @Transactional
  public Comment updateComment(Long commentId, String newContent) {
    log.debug("Updating comment with ID: {}", commentId);

    Comment comment = commentRepository.findById(commentId)
        .orElseThrow(() -> new EntityNotFoundException("Comment not found with ID: " + commentId));

    comment.setContent(newContent);

    return commentRepository.save(comment);
  }

  /**
   * Approve a comment
   */
  @Transactional
  public Comment approveComment(Long commentId) {
    log.debug("Approving comment with ID: {}", commentId);

    Comment comment = commentRepository.findById(commentId)
        .orElseThrow(() -> new EntityNotFoundException("Comment not found with ID: " + commentId));

    comment.setApproved(true);

    return commentRepository.save(comment);
  }

  /**
   * Reject a comment
   */
  @Transactional
  public void rejectComment(Long commentId) {
    log.debug("Rejecting comment with ID: {}", commentId);

    if (!commentRepository.existsById(commentId)) {
      throw new EntityNotFoundException("Comment not found with ID: " + commentId);
    }

    commentRepository.deleteById(commentId);
  }

  /**
   * Delete a comment
   */
  @Transactional
  public void deleteComment(Long commentId) {
    log.debug("Deleting comment with ID: {}", commentId);

    if (!commentRepository.existsById(commentId)) {
      throw new EntityNotFoundException("Comment not found with ID: " + commentId);
    }

    commentRepository.deleteById(commentId);
  }

  /**
   * Get recent comments across all posts Useful for displaying recent activity
   */
  @Transactional(readOnly = true)
  public Page<Comment> getRecentComments(Pageable pageable) {
    log.debug("Finding recent comments");
    return commentRepository.findByApprovedTrueOrderByCreatedAtDesc(pageable);
  }

  /**
   * Count the number of comments for a post
   */
  @Transactional(readOnly = true)
  public long countCommentsForPost(Long postId) {
    log.debug("Counting comments for post with ID: {}", postId);

    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new EntityNotFoundException("Post not found with ID: " + postId));

    return commentRepository.countByPostAndApprovedTrue(post);
  }

  /**
   * Function to fetch comments with their replies in an efficient manner This uses a two-step
   * process to reduce database queries
   */
  @Transactional(readOnly = true)
  public List<Comment> getCommentsWithReplies(Long postId, Pageable pageable) {
    log.debug("Getting comments with replies for post with ID: {}", postId);

    // First, get the top-level comments
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new EntityNotFoundException("Post not found with ID: " + postId));

    Page<Comment> topLevelComments = commentRepository.findByPostAndParentIsNullAndApprovedTrue(
        post, pageable);
    List<Comment> result = topLevelComments.getContent();

    // If there are no comments, return empty list
    if (result.isEmpty()) {
      return result;
    }

    // Now, get all replies for these comments in a single query
    List<Long> parentIds = result.stream()
        .map(Comment::getId)
        .collect(Collectors.toList());

    // Get all replies for these parent comments using our custom query method
    List<Comment> allReplies = commentRepository.findApprovedRepliesByParentIds(parentIds);

    // Group replies by parent ID
    Map<Long, List<Comment>> repliesByParentId = allReplies.stream()
        .collect(Collectors.groupingBy(c -> c.getParent().getId()));

    // Attach replies to their parent comments
    result.forEach(comment -> {
      List<Comment> replies = repliesByParentId.get(comment.getId());
      if (replies != null) {
        comment.setReplies(replies);
      }
    });

    return result;
  }
}