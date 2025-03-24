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
@Tag(name = "Category Management", description = "APIs for managing blog categories")
public class CategoryController {

  private final CategoryService categoryService;

  /**
   * Get all categories
   *
   * @return ResponseEntity with a list of all categories
   */
  @Operation(summary = "Get all categories", description = "Retrieves a list of all blog categories")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved categories",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = CategoryResponse.class)))
  })
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
  @Operation(summary = "Get all categories with post counts",
      description = "Retrieves all categories with the count of posts in each category")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved categories with post counts",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = CategoryWithPostCountResponse.class)))
  })
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
  @Operation(summary = "Get non-empty categories",
      description = "Retrieves categories that have at least one published post")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved non-empty categories",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = CategoryResponse.class)))
  })
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
  @Operation(summary = "Get category by ID",
      description = "Retrieves a specific category by its ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved the category",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = CategoryResponse.class))),
      @ApiResponse(responseCode = "404", description = "Category not found",
          content = @Content)
  })
  @GetMapping("/{id}")
  public ResponseEntity<CategoryResponse> getCategoryById(
      @Parameter(description = "Category ID", required = true)
      @PathVariable Long id) {
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
  @Operation(summary = "Get category by slug",
      description = "Retrieves a specific category by its slug")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved the category",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = CategoryResponse.class))),
      @ApiResponse(responseCode = "404", description = "Category not found",
          content = @Content)
  })
  @GetMapping("/by-slug/{slug}")
  public ResponseEntity<CategoryResponse> getCategoryBySlug(
      @Parameter(description = "Category slug", required = true)
      @PathVariable String slug) {
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
  @Operation(summary = "Create a new category",
      description = "Creates a new category with the provided details")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Category successfully created",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = CategoryResponse.class))),
      @ApiResponse(responseCode = "400", description = "Invalid input",
          content = @Content)
  })
  @PostMapping
  public ResponseEntity<CategoryResponse> createCategory(
      @Parameter(description = "Category details", required = true)
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
  @Operation(summary = "Update an existing category",
      description = "Updates a category with the specified ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Category successfully updated",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = CategoryResponse.class))),
      @ApiResponse(responseCode = "400", description = "Invalid input",
          content = @Content),
      @ApiResponse(responseCode = "404", description = "Category not found",
          content = @Content)
  })
  @PutMapping("/{id}")
  public ResponseEntity<CategoryResponse> updateCategory(
      @Parameter(description = "Category ID", required = true)
      @PathVariable Long id,
      @Parameter(description = "Updated category details", required = true)
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
  @Operation(summary = "Delete a category",
      description = "Deletes the category with the specified ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Category successfully deleted",
          content = @Content),
      @ApiResponse(responseCode = "404", description = "Category not found",
          content = @Content)
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCategory(
      @Parameter(description = "Category ID", required = true)
      @PathVariable Long id) {
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
  @Operation(summary = "Check if slug is in use",
      description = "Checks if a category slug is already in use")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Check completed",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = Boolean.class)))
  })
  @GetMapping("/check-slug")
  public ResponseEntity<Boolean> isSlugInUse(
      @Parameter(description = "Slug to check", required = true)
      @RequestParam String slug) {
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
  @Operation(summary = "Count posts in category",
      description = "Counts the number of published posts in a specific category")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Count retrieved successfully",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = Long.class))),
      @ApiResponse(responseCode = "404", description = "Category not found",
          content = @Content)
  })
  @GetMapping("/{id}/post-count")
  public ResponseEntity<Long> countPostsInCategory(
      @Parameter(description = "Category ID", required = true)
      @PathVariable Long id) {
    log.info("Counting posts in category with ID: {}", id);

    long count = categoryService.countPostsInCategory(id);

    return ResponseEntity.ok(count);
  }
}