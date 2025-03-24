package kr.null0xff.blog.repository;

import java.util.List;
import java.util.Optional;
import kr.null0xff.blog.entity.Category;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

  /**
   * Find a category by its name
   *
   * @param name the name to search for
   * @return an Optional containing the category if found
   */
  Optional<Category> findByName(String name);

  /**
   * Find a category by its slug
   *
   * @param slug the slug to search for
   * @return an Optional containing the category if found
   */
  Optional<Category> findBySlug(String slug);

  /**
   * Check if a category name already exists
   *
   * @param name the name to check
   * @return true if the name exists, false otherwise
   */
  boolean existsByName(String name);

  /**
   * Check if a category slug already exists
   *
   * @param slug the slug to check
   * @return true if the slug exists, false otherwise
   */
  boolean existsBySlug(String slug);

  /**
   * Get all categories with their post counts Useful for category sidebars and navigation
   *
   * @return a list of categories with post counts
   */
  @Query("SELECT c, COUNT(p) FROM Category c LEFT JOIN c.posts p WHERE p.published = true GROUP BY c")
  List<Object[]> findAllWithPostCount(Sort sort);

  /**
   * Find categories that have at least one published post
   *
   * @return a list of non-empty categories
   */
  @Query("SELECT DISTINCT c FROM Category c JOIN c.posts p WHERE p.published = true")
  List<Category> findNonEmptyCategories(Sort sort);
}