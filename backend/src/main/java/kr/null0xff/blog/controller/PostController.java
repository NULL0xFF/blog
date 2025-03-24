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
import kr.null0xff.blog.dto.PostCreateRequest;
import kr.null0xff.blog.dto.PostResponse;
import kr.null0xff.blog.dto.PostUpdateRequest;
import kr.null0xff.blog.entity.Post;
import kr.null0xff.blog.service.PostService;
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
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Post Management", description = "APIs for managing blog posts and their content")
public class PostController {

  private final PostService postService;

  /**
   * Get all published posts with pagination
   *
   * @param page Page number (0-based)
   * @param size Number of items per page
   * @return ResponseEntity with a page of published posts
   */
  @Operation(summary = "Get all published posts",
      description = "Retrieves all published posts with pagination and sorting options")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved posts",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = PostResponse.class)))
  })
  @GetMapping
  public ResponseEntity<Page<PostResponse>> getAllPublishedPosts(
      @Parameter(description = "Page number (0-based)")
      @RequestParam(defaultValue = "0") int page,
      @Parameter(description = "Number of items per page")
      @RequestParam(defaultValue = "10") int size,
      @Parameter(description = "Field to sort by")
      @RequestParam(defaultValue = "publishedAt") String sortBy,
      @Parameter(description = "Sort direction (asc or desc)")
      @RequestParam(defaultValue = "desc") String direction) {

    log.info("Fetching published posts - page: {}, size: {}", page, size);

    Sort sort = direction.equalsIgnoreCase("asc") ?
        Sort.by(sortBy).ascending() :
        Sort.by(sortBy).descending();

    Pageable pageable = PageRequest.of(page, size, sort);
    Page<Post> postsPage = postService.getAllPublishedPosts(pageable);

    // Convert entity to DTO
    Page<PostResponse> responseBody = postsPage.map(PostResponse::fromEntity);

    return ResponseEntity.ok(responseBody);
  }

  /**
   * Get all posts (including drafts) with pagination
   *
   * @param page Page number (0-based)
   * @param size Number of items per page
   * @return ResponseEntity with a page of all posts
   */
  @Operation(summary = "Get all posts (including drafts)",
      description = "Retrieves all posts including drafts with pagination and sorting options")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved all posts",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = PostResponse.class)))
  })
  @GetMapping("/admin")
  public ResponseEntity<Page<PostResponse>> getAllPosts(
      @Parameter(description = "Page number (0-based)")
      @RequestParam(defaultValue = "0") int page,
      @Parameter(description = "Number of items per page")
      @RequestParam(defaultValue = "10") int size,
      @Parameter(description = "Field to sort by")
      @RequestParam(defaultValue = "createdAt") String sortBy,
      @Parameter(description = "Sort direction (asc or desc)")
      @RequestParam(defaultValue = "desc") String direction) {

    log.info("Fetching all posts - page: {}, size: {}", page, size);

    Sort sort = direction.equalsIgnoreCase("asc") ?
        Sort.by(sortBy).ascending() :
        Sort.by(sortBy).descending();

    Pageable pageable = PageRequest.of(page, size, sort);
    Page<Post> postsPage = postService.getAllPosts(pageable);

    // Convert entity to DTO
    Page<PostResponse> responseBody = postsPage.map(PostResponse::fromEntity);

    return ResponseEntity.ok(responseBody);
  }

  /**
   * Get a post by ID
   *
   * @param id Post ID
   * @return ResponseEntity with the post
   */
  @Operation(summary = "Get post by ID",
      description = "Retrieves a specific post by its ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved the post",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = PostResponse.class))),
      @ApiResponse(responseCode = "404", description = "Post not found",
          content = @Content)
  })
  @GetMapping("/{id}")
  public ResponseEntity<PostResponse> getPostById(
      @Parameter(description = "Post ID", required = true)
      @PathVariable Long id) {
    log.info("Fetching post with ID: {}", id);

    Post post = postService.getPostById(id);
    PostResponse responseBody = PostResponse.fromEntity(post);

    return ResponseEntity.ok(responseBody);
  }

  /**
   * Get a published post by slug
   *
   * @param slug Post slug
   * @return ResponseEntity with the post
   */
  @Operation(summary = "Get published post by slug",
      description = "Retrieves a specific published post by its slug")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved the post",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = PostResponse.class))),
      @ApiResponse(responseCode = "404", description = "Post not found or not published",
          content = @Content)
  })
  @GetMapping("/by-slug/{slug}")
  public ResponseEntity<PostResponse> getPublishedPostBySlug(
      @Parameter(description = "Post slug", required = true)
      @PathVariable String slug) {
    log.info("Fetching published post with slug: {}", slug);

    Post post = postService.getPublishedPostBySlug(slug);
    PostResponse responseBody = PostResponse.fromEntity(post);

    return ResponseEntity.ok(responseBody);
  }

  /**
   * Search for published posts containing a query
   *
   * @param query Search query
   * @param page  Page number (0-based)
   * @param size  Number of items per page
   * @return ResponseEntity with a page of matching posts
   */
  @Operation(summary = "Search published posts",
      description = "Searches for published posts containing the query in title or content")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully performed search",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = PostResponse.class)))
  })
  @GetMapping("/search")
  public ResponseEntity<Page<PostResponse>> searchPublishedPosts(
      @Parameter(description = "Search query", required = true)
      @RequestParam String query,
      @Parameter(description = "Page number (0-based)")
      @RequestParam(defaultValue = "0") int page,
      @Parameter(description = "Number of items per page")
      @RequestParam(defaultValue = "10") int size) {

    log.info("Searching for posts with query: {} - page: {}, size: {}", query, page, size);

    Pageable pageable = PageRequest.of(page, size);
    Page<Post> postsPage = postService.searchPublishedPosts(query, pageable);

    // Convert entity to DTO
    Page<PostResponse> responseBody = postsPage.map(PostResponse::fromEntity);

    return ResponseEntity.ok(responseBody);
  }

  /**
   * Get published posts by category
   *
   * @param categorySlug Category slug
   * @param page         Page number (0-based)
   * @param size         Number of items per page
   * @return ResponseEntity with a page of posts in the category
   */
  @Operation(summary = "Get posts by category",
      description = "Retrieves published posts belonging to a specific category")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved posts",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = PostResponse.class))),
      @ApiResponse(responseCode = "404", description = "Category not found",
          content = @Content)
  })
  @GetMapping("/by-category/{categorySlug}")
  public ResponseEntity<Page<PostResponse>> getPostsByCategory(
      @Parameter(description = "Category slug", required = true)
      @PathVariable String categorySlug,
      @Parameter(description = "Page number (0-based)")
      @RequestParam(defaultValue = "0") int page,
      @Parameter(description = "Number of items per page")
      @RequestParam(defaultValue = "10") int size) {

    log.info("Fetching posts for category: {} - page: {}, size: {}", categorySlug, page, size);

    Pageable pageable = PageRequest.of(page, size);
    Page<Post> postsPage = postService.getPostsByCategory(categorySlug, pageable);

    // Convert entity to DTO
    Page<PostResponse> responseBody = postsPage.map(PostResponse::fromEntity);

    return ResponseEntity.ok(responseBody);
  }

  /**
   * Get published posts by tag
   *
   * @param tagSlug Tag slug
   * @param page    Page number (0-based)
   * @param size    Number of items per page
   * @return ResponseEntity with a page of posts with the tag
   */
  @Operation(summary = "Get posts by tag",
      description = "Retrieves published posts tagged with a specific tag")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved posts",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = PostResponse.class))),
      @ApiResponse(responseCode = "404", description = "Tag not found",
          content = @Content)
  })
  @GetMapping("/by-tag/{tagSlug}")
  public ResponseEntity<Page<PostResponse>> getPostsByTag(
      @Parameter(description = "Tag slug", required = true)
      @PathVariable String tagSlug,
      @Parameter(description = "Page number (0-based)")
      @RequestParam(defaultValue = "0") int page,
      @Parameter(description = "Number of items per page")
      @RequestParam(defaultValue = "10") int size) {

    log.info("Fetching posts for tag: {} - page: {}, size: {}", tagSlug, page, size);

    Pageable pageable = PageRequest.of(page, size);
    Page<Post> postsPage = postService.getPostsByTag(tagSlug, pageable);

    // Convert entity to DTO
    Page<PostResponse> responseBody = postsPage.map(PostResponse::fromEntity);

    return ResponseEntity.ok(responseBody);
  }

  /**
   * Get published posts by author
   *
   * @param username Author's username
   * @param page     Page number (0-based)
   * @param size     Number of items per page
   * @return ResponseEntity with a page of posts by the author
   */
  @Operation(summary = "Get posts by author",
      description = "Retrieves published posts written by a specific author")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved posts",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = PostResponse.class))),
      @ApiResponse(responseCode = "404", description = "Author not found",
          content = @Content)
  })
  @GetMapping("/by-author/{username}")
  public ResponseEntity<Page<PostResponse>> getPostsByAuthor(
      @Parameter(description = "Author's username", required = true)
      @PathVariable String username,
      @Parameter(description = "Page number (0-based)")
      @RequestParam(defaultValue = "0") int page,
      @Parameter(description = "Number of items per page")
      @RequestParam(defaultValue = "10") int size) {

    log.info("Fetching posts by author: {} - page: {}, size: {}", username, page, size);

    Pageable pageable = PageRequest.of(page, size);
    Page<Post> postsPage = postService.getPostsByAuthor(username, pageable);

    // Convert entity to DTO
    Page<PostResponse> responseBody = postsPage.map(PostResponse::fromEntity);

    return ResponseEntity.ok(responseBody);
  }

  /**
   * Get recent published posts
   *
   * @param limit Maximum number of posts to return
   * @return ResponseEntity with a list of recent posts
   */
  @Operation(summary = "Get recent published posts",
      description = "Retrieves the most recently published posts up to the specified limit")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved recent posts",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = PostResponse.class)))
  })
  @GetMapping("/recent")
  public ResponseEntity<List<PostResponse>> getRecentPublishedPosts(
      @Parameter(description = "Maximum number of posts to return")
      @RequestParam(defaultValue = "5") int limit) {

    log.info("Fetching {} recent published posts", limit);

    List<Post> posts = postService.getRecentPublishedPosts(limit);

    // Convert entities to DTOs
    List<PostResponse> responseBody = posts.stream()
        .map(PostResponse::fromEntity)
        .collect(Collectors.toList());

    return ResponseEntity.ok(responseBody);
  }

  /**
   * Create a new post
   *
   * @param request Post creation request
   * @return ResponseEntity with the created post
   */
  @Operation(summary = "Create a new post",
      description = "Creates a new blog post with the provided details")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Post successfully created",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = PostResponse.class))),
      @ApiResponse(responseCode = "400", description = "Invalid input",
          content = @Content),
      @ApiResponse(responseCode = "404", description = "Author, category, or tag not found",
          content = @Content)
  })
  @PostMapping
  public ResponseEntity<PostResponse> createPost(
      @Parameter(description = "Post details", required = true)
      @Valid @RequestBody PostCreateRequest request) {
    log.info("Creating new post: {}", request.getTitle());

    // Create a Post entity from the request
    Post post = new Post();
    post.setTitle(request.getTitle());
    post.setSlug(request.getSlug());
    post.setDescription(request.getDescription());
    post.setContent(request.getContent());
    post.setImageUrl(request.getImageUrl());
    post.setPublished(request.isPublished());

    // Create the post
    Post createdPost = postService.createPost(
        post,
        request.getAuthorId(),
        request.getCategoryId(),
        request.getTags());

    // Convert entity to DTO
    PostResponse responseBody = PostResponse.fromEntity(createdPost);

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(responseBody);
  }

  /**
   * Update an existing post
   *
   * @param id      Post ID
   * @param request Post update request
   * @return ResponseEntity with the updated post
   */
  @Operation(summary = "Update an existing post",
      description = "Updates a post with the specified ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Post successfully updated",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = PostResponse.class))),
      @ApiResponse(responseCode = "400", description = "Invalid input",
          content = @Content),
      @ApiResponse(responseCode = "404", description = "Post, category, or tag not found",
          content = @Content)
  })
  @PutMapping("/{id}")
  public ResponseEntity<PostResponse> updatePost(
      @Parameter(description = "Post ID", required = true)
      @PathVariable Long id,
      @Parameter(description = "Updated post details", required = true)
      @Valid @RequestBody PostUpdateRequest request) {

    log.info("Updating post with ID: {}", id);

    // Create a Post entity from the request
    Post post = new Post();
    post.setTitle(request.getTitle());
    post.setSlug(request.getSlug());
    post.setDescription(request.getDescription());
    post.setContent(request.getContent());
    post.setImageUrl(request.getImageUrl());
    post.setPublished(request.isPublished());

    // Update the post
    Post updatedPost = postService.updatePost(
        id,
        post,
        request.getCategoryId(),
        request.getTags());

    // Convert entity to DTO
    PostResponse responseBody = PostResponse.fromEntity(updatedPost);

    return ResponseEntity.ok(responseBody);
  }

  /**
   * Publish a post
   *
   * @param id Post ID
   * @return ResponseEntity with the published post
   */
  @Operation(summary = "Publish a post",
      description = "Changes a post's status to published and sets the published date")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Post successfully published",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = PostResponse.class))),
      @ApiResponse(responseCode = "404", description = "Post not found",
          content = @Content)
  })
  @PutMapping("/{id}/publish")
  public ResponseEntity<PostResponse> publishPost(
      @Parameter(description = "Post ID", required = true)
      @PathVariable Long id) {
    log.info("Publishing post with ID: {}", id);

    Post publishedPost = postService.publishPost(id);

    // Convert entity to DTO
    PostResponse responseBody = PostResponse.fromEntity(publishedPost);

    return ResponseEntity.ok(responseBody);
  }

  /**
   * Delete a post
   *
   * @param id Post ID
   * @return ResponseEntity with no content
   */
  @Operation(summary = "Delete a post",
      description = "Deletes the post with the specified ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Post successfully deleted",
          content = @Content),
      @ApiResponse(responseCode = "404", description = "Post not found",
          content = @Content)
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletePost(
      @Parameter(description = "Post ID", required = true)
      @PathVariable Long id) {
    log.info("Deleting post with ID: {}", id);

    postService.deletePost(id);

    return ResponseEntity.noContent().build();
  }

  /**
   * Check if a slug is already in use
   *
   * @param slug Slug to check
   * @return ResponseEntity with boolean result
   */
  @Operation(summary = "Check if slug is in use",
      description = "Checks if a post slug is already in use")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Check completed",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = Boolean.class)))
  })
  @GetMapping("/check-slug")
  public ResponseEntity<Boolean> isSlugInUse(
      @Parameter(description = "Slug to check", required = true)
      @RequestParam String slug) {
    log.info("Checking if slug is in use: {}", slug);

    boolean isInUse = postService.isSlugInUse(slug);

    return ResponseEntity.ok(isInUse);
  }

  /**
   * Generate a unique slug from a title
   *
   * @param title Title to generate slug from
   * @return ResponseEntity with the generated slug
   */
  @Operation(summary = "Generate unique slug from title",
      description = "Generates a unique URL-friendly slug from a post title")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Slug successfully generated",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = String.class)))
  })
  @GetMapping("/generate-slug")
  public ResponseEntity<String> generateUniqueSlug(
      @Parameter(description = "Title to generate slug from", required = true)
      @RequestParam String title) {
    log.info("Generating slug from title: {}", title);

    String slug = postService.generateUniqueSlug(title);

    return ResponseEntity.ok(slug);
  }
}