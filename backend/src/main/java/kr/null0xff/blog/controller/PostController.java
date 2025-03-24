package kr.null0xff.blog.controller;

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
public class PostController {

  private final PostService postService;

  /**
   * Get all published posts with pagination
   *
   * @param page Page number (0-based)
   * @param size Number of items per page
   * @return ResponseEntity with a page of published posts
   */
  @GetMapping
  public ResponseEntity<Page<PostResponse>> getAllPublishedPosts(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "publishedAt") String sortBy,
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
  @GetMapping("/admin")
  public ResponseEntity<Page<PostResponse>> getAllPosts(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "createdAt") String sortBy,
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
  @GetMapping("/{id}")
  public ResponseEntity<PostResponse> getPostById(@PathVariable Long id) {
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
  @GetMapping("/by-slug/{slug}")
  public ResponseEntity<PostResponse> getPublishedPostBySlug(@PathVariable String slug) {
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
  @GetMapping("/search")
  public ResponseEntity<Page<PostResponse>> searchPublishedPosts(
      @RequestParam String query,
      @RequestParam(defaultValue = "0") int page,
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
  @GetMapping("/by-category/{categorySlug}")
  public ResponseEntity<Page<PostResponse>> getPostsByCategory(
      @PathVariable String categorySlug,
      @RequestParam(defaultValue = "0") int page,
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
  @GetMapping("/by-tag/{tagSlug}")
  public ResponseEntity<Page<PostResponse>> getPostsByTag(
      @PathVariable String tagSlug,
      @RequestParam(defaultValue = "0") int page,
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
  @GetMapping("/by-author/{username}")
  public ResponseEntity<Page<PostResponse>> getPostsByAuthor(
      @PathVariable String username,
      @RequestParam(defaultValue = "0") int page,
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
  @GetMapping("/recent")
  public ResponseEntity<List<PostResponse>> getRecentPublishedPosts(
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
  @PostMapping
  public ResponseEntity<PostResponse> createPost(@Valid @RequestBody PostCreateRequest request) {
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
  @PutMapping("/{id}")
  public ResponseEntity<PostResponse> updatePost(
      @PathVariable Long id,
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
  @PutMapping("/{id}/publish")
  public ResponseEntity<PostResponse> publishPost(@PathVariable Long id) {
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
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletePost(@PathVariable Long id) {
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
  @GetMapping("/check-slug")
  public ResponseEntity<Boolean> isSlugInUse(@RequestParam String slug) {
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
  @GetMapping("/generate-slug")
  public ResponseEntity<String> generateUniqueSlug(@RequestParam String title) {
    log.info("Generating slug from title: {}", title);

    String slug = postService.generateUniqueSlug(title);

    return ResponseEntity.ok(slug);
  }
}