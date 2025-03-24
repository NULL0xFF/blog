package kr.null0xff.blog.service;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kr.null0xff.blog.entity.Category;
import kr.null0xff.blog.repository.CategoryRepository;
import kr.null0xff.blog.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryService {

  private final CategoryRepository categoryRepository;
  private final PostRepository postRepository;

  /**
   * Get all categories
   */
  @Transactional(readOnly = true)
  public List<Category> getAllCategories() {
    log.debug("Finding all categories");
    return categoryRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
  }

  /**
   * Get all categories with post counts Useful for sidebars and navigation
   */
  @Transactional(readOnly = true)
  public Map<Category, Long> getCategoriesWithPostCount() {
    log.debug("Finding all categories with post counts");

    List<Object[]> results = categoryRepository.findAllWithPostCount(
        Sort.by(Sort.Direction.ASC, "name"));

    return results.stream()
        .collect(Collectors.toMap(
            row -> (Category) row[0],
            row -> (Long) row[1]
        ));
  }

  /**
   * Get all non-empty categories (categories with at least one published post)
   */
  @Transactional(readOnly = true)
  public List<Category> getNonEmptyCategories() {
    log.debug("Finding non-empty categories");
    return categoryRepository.findNonEmptyCategories(
        Sort.by(Sort.Direction.ASC, "name"));
  }

  /**
   * Get a category by its ID
   */
  @Transactional(readOnly = true)
  public Category getCategoryById(Long id) {
    log.debug("Finding category with ID: {}", id);
    return categoryRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Category not found with ID: " + id));
  }

  /**
   * Get a category by its slug
   */
  @Transactional(readOnly = true)
  public Category getCategoryBySlug(String slug) {
    log.debug("Finding category with slug: {}", slug);
    return categoryRepository.findBySlug(slug)
        .orElseThrow(() -> new EntityNotFoundException("Category not found with slug: " + slug));
  }

  /**
   * Create a new category
   */
  @Transactional
  public Category createCategory(Category category) {
    log.debug("Creating new category: {}", category.getName());

    // Check if a category with the same name or slug already exists
    if (categoryRepository.existsByName(category.getName())) {
      throw new IllegalArgumentException("A category with this name already exists");
    }

    if (category.getSlug() == null || category.getSlug().isEmpty()) {
      // Generate a slug from the name if not provided
      category.setSlug(generateSlugFromName(category.getName()));
    } else if (categoryRepository.existsBySlug(category.getSlug())) {
      throw new IllegalArgumentException("A category with this slug already exists");
    }

    return categoryRepository.save(category);
  }

  /**
   * Update an existing category
   */
  @Transactional
  public Category updateCategory(Long categoryId, Category updatedCategory) {
    log.debug("Updating category with ID: {}", categoryId);

    Category existingCategory = categoryRepository.findById(categoryId)
        .orElseThrow(
            () -> new EntityNotFoundException("Category not found with ID: " + categoryId));

    // Check if the updated name or slug would conflict with another category
    if (!existingCategory.getName().equals(updatedCategory.getName()) &&
        categoryRepository.existsByName(updatedCategory.getName())) {
      throw new IllegalArgumentException("A category with this name already exists");
    }

    String newSlug = updatedCategory.getSlug();
    if (newSlug == null || newSlug.isEmpty()) {
      // Generate a slug from the name if not provided
      newSlug = generateSlugFromName(updatedCategory.getName());
    } else if (!existingCategory.getSlug().equals(newSlug) &&
        categoryRepository.existsBySlug(newSlug)) {
      throw new IllegalArgumentException("A category with this slug already exists");
    }

    // Update fields
    existingCategory.setName(updatedCategory.getName());
    existingCategory.setSlug(newSlug);
    existingCategory.setDescription(updatedCategory.getDescription());
    existingCategory.setColor(updatedCategory.getColor());

    return categoryRepository.save(existingCategory);
  }

  /**
   * Delete a category This will remove the category from all associated posts
   */
  @Transactional
  public void deleteCategory(Long categoryId) {
    log.debug("Deleting category with ID: {}", categoryId);

    Category category = categoryRepository.findById(categoryId)
        .orElseThrow(
            () -> new EntityNotFoundException("Category not found with ID: " + categoryId));

    // Remove this category from all posts
    category.getPosts().forEach(post -> post.setCategory(null));

    // Now we can safely delete the category
    categoryRepository.delete(category);
  }

  /**
   * Check if a category slug is already in use
   */
  @Transactional(readOnly = true)
  public boolean isSlugInUse(String slug) {
    return categoryRepository.existsBySlug(slug);
  }

  /**
   * Generate a slug from a category name
   */
  private String generateSlugFromName(String name) {
    // Convert to lowercase and replace spaces with hyphens
    String baseSlug = name.toLowerCase()
        .replaceAll("[^a-z0-9\\s]", "") // Remove non-alphanumeric chars
        .replaceAll("\\s+", "-"); // Replace spaces with hyphens

    // Check if the base slug is already in use
    if (!isSlugInUse(baseSlug)) {
      return baseSlug;
    }

    // If the slug is in use, append a number until we find a unique slug
    int counter = 1;
    String newSlug;
    do {
      newSlug = baseSlug + "-" + counter;
      counter++;
    } while (isSlugInUse(newSlug));

    return newSlug;
  }

  /**
   * Count the number of posts in a category
   */
  @Transactional(readOnly = true)
  public long countPostsInCategory(Long categoryId) {
    log.debug("Counting posts in category with ID: {}", categoryId);

    Category category = categoryRepository.findById(categoryId)
        .orElseThrow(
            () -> new EntityNotFoundException("Category not found with ID: " + categoryId));

    return postRepository.countByCategoryAndPublishedTrue(category);
  }
}