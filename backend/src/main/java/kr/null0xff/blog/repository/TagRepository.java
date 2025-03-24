package kr.null0xff.blog.repository;

import java.util.List;
import java.util.Optional;
import kr.null0xff.blog.entity.Tag;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

  /**
   * Find a tag by its name
   *
   * @param name the name to search for
   * @return an Optional containing the tag if found
   */
  Optional<Tag> findByName(String name);

  /**
   * Find a tag by its slug
   *
   * @param slug the slug to search for
   * @return an Optional containing the tag if found
   */
  Optional<Tag> findBySlug(String slug);

  /**
   * Check if a tag name already exists
   *
   * @param name the name to check
   * @return true if the name exists, false otherwise
   */
  boolean existsByName(String name);

  /**
   * Check if a tag slug already exists
   *
   * @param slug the slug to check
   * @return true if the slug exists, false otherwise
   */
  boolean existsBySlug(String slug);

  /**
   * Find tags that contain a specific string in their name Useful for tag autocompletion
   *
   * @param partialName the partial name to search for
   * @return a list of matching tags
   */
  List<Tag> findByNameContainingIgnoreCase(String partialName);

  /**
   * Get all tags with their post counts Useful for tag clouds
   *
   * @return a list of tags with post counts
   */
  @Query("SELECT t, COUNT(p) FROM Tag t JOIN t.posts p WHERE p.published = true GROUP BY t")
  List<Object[]> findAllWithPostCount(Sort sort);

  /**
   * Find popular tags (tags with many posts)
   *
   * @param limit the maximum number of tags to return
   * @return a list of popular tags with post counts
   */
  @Query("SELECT t, COUNT(p) FROM Tag t JOIN t.posts p WHERE p.published = true GROUP BY t ORDER BY COUNT(p) DESC")
  List<Object[]> findPopularTags(int limit);

  /**
   * Find tags that have at least one published post
   *
   * @return a list of non-empty tags
   */
  @Query("SELECT DISTINCT t FROM Tag t JOIN t.posts p WHERE p.published = true")
  List<Tag> findNonEmptyTags(Sort sort);
}