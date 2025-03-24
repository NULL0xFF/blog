package kr.null0xff.blog.controller;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kr.null0xff.blog.dto.CategoryRequest;
import kr.null0xff.blog.dto.CategoryResponse;
import kr.null0xff.blog.dto.CategoryWithPostCountResponse;
import kr.null0xff.blog.entity.Category;
import kr.null0xff.blog.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

  private final CategoryService categoryService;

  /**
   * Get all categories
   *
   * @return ResponseEntity with a list of all categories
   */
  @GetMapping
  public ResponseEntity<List<CategoryResponse>> getAllCategories() {
    log.info("Fetching all categories");

    List<Category> categories = categoryService.getAllCategories();

    // Convert entities to DTOs
    List<CategoryResponse> responseBody = categories.stream()
        .map(CategoryResponse::fromEntity)
        .collect(Collectors.toList());

    return ResponseEntity.ok(responseBody);
  }

  /**
   * Get all categories with post counts
   *
   * @return ResponseEntity with a list of categories with post counts
   */
  @GetMapping("/with-post-count")
  public ResponseEntity<List<CategoryWithPostCountResponse>> getCategoriesWithPostCount() {
    log.info("Fetching all categories with post counts");

    Map<Category, Long> categoriesWithCount = categoryService.getCategoriesWithPostCount();

    // Convert map entries to DTOs
    List<CategoryWithPostCountResponse> responseBody = categoriesWithCount.entrySet().stream()
        .map(entry -> CategoryWithPostCountResponse.fromEntityAndCount(entry.getKey(),
            entry.getValue()))
        .collect(Collectors.toList());

    return ResponseEntity.ok(responseBody);
  }

  /**
   * Get all non-empty categories (categories with at least one published post)
   *
   * @return ResponseEntity with a list of non-empty categories
   */
  @GetMapping("/non-empty")
  public ResponseEntity<List<CategoryResponse>> getNonEmptyCategories() {
    log.info("Fetching non-empty categories");

    List<Category> categories = categoryService.getNonEmptyCategories();

    // Convert entities to DTOs
    List<CategoryResponse> responseBody = categories.stream()
        .map(CategoryResponse::fromEntity)
        .collect(Collectors.toList());

    return ResponseEntity.ok(responseBody);
  }

  /**
   * Get a category by its ID
   *
   * @param id Category ID
   * @return ResponseEntity with the category
   */
  @GetMapping("/{id}")
  public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long id) {
    log.info("Fetching category with ID: {}", id);

    Category category = categoryService.getCategoryById(id);
    CategoryResponse responseBody = CategoryResponse.fromEntity(category);

    return ResponseEntity.ok(responseBody);
  }

  /**
   * Get a category by its slug
   *
   * @param slug Category slug
   * @return ResponseEntity with the category
   */
  @GetMapping("/by-slug/{slug}")
  public ResponseEntity<CategoryResponse> getCategoryBySlug(@PathVariable String slug) {
    log.info("Fetching category with slug: {}", slug);

    Category category = categoryService.getCategoryBySlug(slug);
    CategoryResponse responseBody = CategoryResponse.fromEntity(category);

    return ResponseEntity.ok(responseBody);
  }

  /**
   * Create a new category
   *
   * @param request Category creation request
   * @return ResponseEntity with the created category
   */
  @PostMapping
  public ResponseEntity<CategoryResponse> createCategory(
      @Valid @RequestBody CategoryRequest request) {
    log.info("Creating new category: {}", request.getName());

    // Create a Category entity from the request
    Category category = new Category();
    category.setName(request.getName());
    category.setSlug(request.getSlug());
    category.setDescription(request.getDescription());
    category.setColor(request.getColor());

    // Create the category
    Category createdCategory = categoryService.createCategory(category);

    // Convert entity to DTO
    CategoryResponse responseBody = CategoryResponse.fromEntity(createdCategory);

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(responseBody);
  }

  /**
   * Update an existing category
   *
   * @param id      Category ID
   * @param request Category update request
   * @return ResponseEntity with the updated category
   */
  @PutMapping("/{id}")
  public ResponseEntity<CategoryResponse> updateCategory(
      @PathVariable Long id,
      @Valid @RequestBody CategoryRequest request) {

    log.info("Updating category with ID: {}", id);

    // Create a Category entity from the request
    Category category = new Category();
    category.setName(request.getName());
    category.setSlug(request.getSlug());
    category.setDescription(request.getDescription());
    category.setColor(request.getColor());

    // Update the category
    Category updatedCategory = categoryService.updateCategory(id, category);

    // Convert entity to DTO
    CategoryResponse responseBody = CategoryResponse.fromEntity(updatedCategory);

    return ResponseEntity.ok(responseBody);
  }

  /**
   * Delete a category
   *
   * @param id Category ID
   * @return ResponseEntity with no content
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
    log.info("Deleting category with ID: {}", id);

    categoryService.deleteCategory(id);

    return ResponseEntity.noContent().build();
  }

  /**
   * Check if a category slug is already in use
   *
   * @param slug Slug to check
   * @return ResponseEntity with boolean result
   */
  @GetMapping("/check-slug")
  public ResponseEntity<Boolean> isSlugInUse(@RequestParam String slug) {
    log.info("Checking if category slug is in use: {}", slug);

    boolean isInUse = categoryService.isSlugInUse(slug);

    return ResponseEntity.ok(isInUse);
  }

  /**
   * Count the number of posts in a category
   *
   * @param id Category ID
   * @return ResponseEntity with the count
   */
  @GetMapping("/{id}/post-count")
  public ResponseEntity<Long> countPostsInCategory(@PathVariable Long id) {
    log.info("Counting posts in category with ID: {}", id);

    long count = categoryService.countPostsInCategory(id);

    return ResponseEntity.ok(count);
  }
}