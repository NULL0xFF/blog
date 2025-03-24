package kr.null0xff.blog.service;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import kr.null0xff.blog.entity.Category;
import kr.null0xff.blog.entity.Post;
import kr.null0xff.blog.entity.Tag;
import kr.null0xff.blog.entity.User;
import kr.null0xff.blog.repository.CategoryRepository;
import kr.null0xff.blog.repository.PostRepository;
import kr.null0xff.blog.repository.TagRepository;
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
public class PostService {

  private final PostRepository postRepository;
  private final UserRepository userRepository;
  private final CategoryRepository categoryRepository;
  private final TagRepository tagRepository;

  /**
   * Get all published posts with pagination
   */
  @Transactional(readOnly = true)
  public Page<Post> getAllPublishedPosts(Pageable pageable) {
    log.debug("Finding all published posts with pagination");
    return postRepository.findByPublishedTrue(pageable);
  }

  /**
   * Get all posts (including drafts) with pagination This would typically be used in admin areas
   */
  @Transactional(readOnly = true)
  public Page<Post> getAllPosts(Pageable pageable) {
    log.debug("Finding all posts with pagination");
    return postRepository.findAll(pageable);
  }

  /**
   * Get a single post by its ID
   */
  @Transactional(readOnly = true)
  public Post getPostById(Long id) {
    log.debug("Finding post by ID: {}", id);
    return postRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Post not found with ID: " + id));
  }

  /**
   * Get a single published post by its slug
   */
  @Transactional(readOnly = true)
  public Post getPublishedPostBySlug(String slug) {
    log.debug("Finding published post by slug: {}", slug);
    Post post = postRepository.findBySlug(slug)
        .orElseThrow(() -> new EntityNotFoundException("Post not found with slug: " + slug));

    if (!post.isPublished()) {
      throw new EntityNotFoundException("Post not found with slug: " + slug);
    }

    return post;
  }

  /**
   * Get a single post by its slug (including drafts) This would typically be used in admin areas
   */
  @Transactional(readOnly = true)
  public Post getPostBySlug(String slug) {
    log.debug("Finding post by slug: {}", slug);
    return postRepository.findBySlug(slug)
        .orElseThrow(() -> new EntityNotFoundException("Post not found with slug: " + slug));
  }

  /**
   * Search for published posts containing the given query in title or content
   */
  @Transactional(readOnly = true)
  public Page<Post> searchPublishedPosts(String query, Pageable pageable) {
    log.debug("Searching published posts for: {}", query);
    return postRepository.searchPublishedPosts(query, pageable);
  }

  /**
   * Get published posts by category
   */
  @Transactional(readOnly = true)
  public Page<Post> getPostsByCategory(String categorySlug, Pageable pageable) {
    log.debug("Finding posts by category slug: {}", categorySlug);
    Category category = categoryRepository.findBySlug(categorySlug)
        .orElseThrow(
            () -> new EntityNotFoundException("Category not found with slug: " + categorySlug));

    return postRepository.findByCategoryAndPublishedTrue(category, pageable);
  }

  /**
   * Get published posts by tag
   */
  @Transactional(readOnly = true)
  public Page<Post> getPostsByTag(String tagSlug, Pageable pageable) {
    log.debug("Finding posts by tag slug: {}", tagSlug);
    Tag tag = tagRepository.findBySlug(tagSlug)
        .orElseThrow(() -> new EntityNotFoundException("Tag not found with slug: " + tagSlug));

    return postRepository.findByTagsContainingAndPublishedTrue(tag, pageable);
  }

  /**
   * Get published posts by author
   */
  @Transactional(readOnly = true)
  public Page<Post> getPostsByAuthor(String username, Pageable pageable) {
    log.debug("Finding posts by author username: {}", username);
    User author = userRepository.findByUsername(username)
        .orElseThrow(
            () -> new EntityNotFoundException("User not found with username: " + username));

    return postRepository.findByAuthorAndPublishedTrue(author, pageable);
  }

  /**
   * Get recent published posts (for featured sections, sidebars, etc.)
   */
  @Transactional(readOnly = true)
  public List<Post> getRecentPublishedPosts(int limit) {
    log.debug("Finding {} recent published posts", limit);
    return postRepository.findRecentPublishedPosts(Pageable.ofSize(limit));
  }

  /**
   * Create a new post
   */
  @Transactional
  public Post createPost(Post post, Long authorId, Long categoryId, Set<String> tagNames) {
    log.debug("Creating new post: {}", post.getTitle());

    // Set the author
    User author = userRepository.findById(authorId)
        .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + authorId));
    post.setAuthor(author);

    // Set the category if provided
    if (categoryId != null) {
      Category category = categoryRepository.findById(categoryId)
          .orElseThrow(
              () -> new EntityNotFoundException("Category not found with ID: " + categoryId));
      post.setCategory(category);
    }

    // Process tags
    if (tagNames != null && !tagNames.isEmpty()) {
      Set<Tag> tags = tagNames.stream()
          .map(tagName -> {
            // Find or create tag
            return tagRepository.findByName(tagName)
                .orElseGet(() -> {
                  Tag newTag = new Tag();
                  newTag.setName(tagName);
                  // Generate a slug from the tag name
                  newTag.setSlug(tagName.toLowerCase().replace(' ', '-'));
                  return tagRepository.save(newTag);
                });
          })
          .collect(Collectors.toSet());

      // Add all tags to the post
      tags.forEach(post::addTag);
    }

    return postRepository.save(post);
  }

  /**
   * Update an existing post
   */
  @Transactional
  public Post updatePost(Long postId, Post updatedPost, Long categoryId, Set<String> tagNames) {
    log.debug("Updating post with ID: {}", postId);

    Post existingPost = postRepository.findById(postId)
        .orElseThrow(() -> new EntityNotFoundException("Post not found with ID: " + postId));

    // Update basic fields
    existingPost.setTitle(updatedPost.getTitle());
    existingPost.setSlug(updatedPost.getSlug());
    existingPost.setDescription(updatedPost.getDescription());
    existingPost.setContent(updatedPost.getContent());
    existingPost.setImageUrl(updatedPost.getImageUrl());

    // Update published status
    if (updatedPost.isPublished() && !existingPost.isPublished()) {
      // If we're publishing for the first time, set publishedAt
      existingPost.publish();
    } else {
      existingPost.setPublished(updatedPost.isPublished());
    }

    // Update category if provided
    if (categoryId != null) {
      Category category = categoryRepository.findById(categoryId)
          .orElseThrow(
              () -> new EntityNotFoundException("Category not found with ID: " + categoryId));
      existingPost.setCategory(category);
    } else {
      existingPost.setCategory(null);
    }

    // Update tags if provided
    if (tagNames != null) {
      // Remove all existing tags
      existingPost.getTags().clear();

      // Add new tags
      Set<Tag> tags = tagNames.stream()
          .map(tagName -> {
            // Find or create tag
            return tagRepository.findByName(tagName)
                .orElseGet(() -> {
                  Tag newTag = new Tag();
                  newTag.setName(tagName);
                  // Generate a slug from the tag name
                  newTag.setSlug(tagName.toLowerCase().replace(' ', '-'));
                  return tagRepository.save(newTag);
                });
          })
          .collect(Collectors.toSet());

      // Add all tags to the post
      tags.forEach(existingPost::addTag);
    }

    return postRepository.save(existingPost);
  }

  /**
   * Publish a post
   */
  @Transactional
  public Post publishPost(Long postId) {
    log.debug("Publishing post with ID: {}", postId);

    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new EntityNotFoundException("Post not found with ID: " + postId));

    post.publish();
    return postRepository.save(post);
  }

  /**
   * Delete a post
   */
  @Transactional
  public void deletePost(Long postId) {
    log.debug("Deleting post with ID: {}", postId);

    if (!postRepository.existsById(postId)) {
      throw new EntityNotFoundException("Post not found with ID: " + postId);
    }

    postRepository.deleteById(postId);
  }

  /**
   * Check if a slug is already in use
   */
  @Transactional(readOnly = true)
  public boolean isSlugInUse(String slug) {
    return postRepository.existsBySlug(slug);
  }

  /**
   * Generate a unique slug from a title
   */
  @Transactional(readOnly = true)
  public String generateUniqueSlug(String title) {
    // Convert title to lowercase and replace spaces with hyphens
    String baseSlug = title.toLowerCase()
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
}