package kr.null0xff.blog.repository;

import java.util.List;
import kr.null0xff.blog.entity.Comment;
import kr.null0xff.blog.entity.Post;
import kr.null0xff.blog.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>,
    JpaSpecificationExecutor<Comment> {

  /**
   * Find all approved comments for a specific post
   *
   * @param post     the post to find comments for
   * @param pageable pagination information
   * @return a Page of approved comments
   */
  Page<Comment> findByPostAndApprovedTrue(Post post, Pageable pageable);

  /**
   * Find all top-level comments (not replies) for a post
   *
   * @param post     the post to find comments for
   * @param pageable pagination information
   * @return a Page of top-level comments
   */
  Page<Comment> findByPostAndParentIsNullAndApprovedTrue(Post post, Pageable pageable);

  /**
   * Find all replies to a specific parent comment
   *
   * @param parent   the parent comment
   * @param pageable pagination information
   * @return a Page of replies
   */
  Page<Comment> findByParentAndApprovedTrue(Comment parent, Pageable pageable);

  /**
   * Find all comments by a specific author
   *
   * @param author   the author of the comments
   * @param pageable pagination information
   * @return a Page of comments by the author
   */
  Page<Comment> findByAuthor(User author, Pageable pageable);

  /**
   * Find all unapproved comments (for moderation)
   *
   * @param pageable pagination information
   * @return a Page of unapproved comments
   */
  Page<Comment> findByApprovedFalse(Pageable pageable);

  /**
   * Find recent comments across all posts
   *
   * @param pageable pagination information
   * @return a Page of recent comments
   */
  Page<Comment> findByApprovedTrueOrderByCreatedAtDesc(Pageable pageable);

  /**
   * Count the number of approved comments for a post
   *
   * @param post the post to count comments for
   * @return the number of approved comments
   */
  long countByPostAndApprovedTrue(Post post);

  /**
   * Count the number of comments by a specific author
   *
   * @param author the author to count comments for
   * @return the number of comments by the author
   */
  long countByAuthor(User author);

  /**
   * Count the number of unapproved comments (for moderation queue)
   *
   * @return the number of unapproved comments
   */
  long countByApprovedFalse();

  /**
   * Get the count of replies for each comment in a list of parent comments This is useful for
   * showing reply counts without loading all the replies
   *
   * @param parentIds list of parent comment IDs
   * @return list of counts for each parent
   */
  @Query("SELECT c.parent.id, COUNT(c) FROM Comment c WHERE c.parent.id IN ?1 AND c.approved = true GROUP BY c.parent.id")
  List<Object[]> countRepliesByParentIds(List<Long> parentIds);

  /**
   * Find all approved replies for the given parent comment IDs
   *
   * @param parentIds list of parent comment IDs
   * @return list of replies
   */
  @Query("SELECT c FROM Comment c WHERE c.parent.id IN ?1 AND c.approved = true")
  List<Comment> findApprovedRepliesByParentIds(List<Long> parentIds);
}