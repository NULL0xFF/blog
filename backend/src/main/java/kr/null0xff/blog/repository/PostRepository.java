package kr.null0xff.blog.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import kr.null0xff.blog.entity.Category;
import kr.null0xff.blog.entity.Post;
import kr.null0xff.blog.entity.Tag;
import kr.null0xff.blog.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

  /**
   * Find a post by its slug
   *
   * @param slug the slug to search for
   * @return an Optional containing the post if found
   */
  Optional<Post> findBySlug(String slug);

  /**
   * Find all published posts
   *
   * @param pageable pagination information
   * @return a Page of published posts
   */
  Page<Post> findByPublishedTrue(Pageable pageable);

  /**
   * Find all published posts by a specific author
   *
   * @param author   the author of the posts
   * @param pageable pagination information
   * @return a Page of published posts by the author
   */
  Page<Post> findByAuthorAndPublishedTrue(User author, Pageable pageable);

  /**
   * Find all published posts in a specific category
   *
   * @param category the category to filter by
   * @param pageable pagination information
   * @return a Page of published posts in the category
   */
  Page<Post> findByCategoryAndPublishedTrue(Category category, Pageable pageable);

  /**
   * Find all published posts with a specific tag
   *
   * @param tag      the tag to filter by
   * @param pageable pagination information
   * @return a Page of published posts with the tag
   */
  Page<Post> findByTagsContainingAndPublishedTrue(Tag tag, Pageable pageable);

  /**
   * Search for published posts by title or content
   *
   * @param query    the search query
   * @param pageable pagination information
   * @return a Page of matching published posts
   */
  @Query("SELECT p FROM Post p WHERE p.published = true AND " +
      "(LOWER(p.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
      "p.content LIKE CONCAT('%', :query, '%'))")
  Page<Post> searchPublishedPosts(@Param("query") String query, Pageable pageable);

  /**
   * Find all published posts created after a specific date
   *
   * @param date     the date to filter by
   * @param pageable pagination information
   * @return a Page of published posts created after the date
   */
  Page<Post> findByPublishedTrueAndCreatedAtAfter(LocalDateTime date, Pageable pageable);

  /**
   * Find recently published posts
   *
   * @param pageable pagination information
   * @return a List of the most recently published posts
   */
  @Query("SELECT p FROM Post p WHERE p.published = true ORDER BY p.publishedAt DESC")
  List<Post> findRecentPublishedPosts(Pageable pageable);

  /**
   * Check if a slug is already used
   *
   * @param slug the slug to check
   * @return true if the slug exists, false otherwise
   */
  boolean existsBySlug(String slug);

  /**
   * Count the number of published posts by a specific author
   *
   * @param author the author to count posts for
   * @return the number of published posts by the author
   */
  long countByAuthorAndPublishedTrue(User author);

  /**
   * Count the number of published posts in a specific category
   *
   * @param category the category to count posts for
   * @return the number of published posts in the category
   */
  long countByCategoryAndPublishedTrue(Category category);
}