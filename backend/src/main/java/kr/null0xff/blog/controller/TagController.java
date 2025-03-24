package kr.null0xff.blog.controller;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kr.null0xff.blog.dto.TagRequest;
import kr.null0xff.blog.dto.TagResponse;
import kr.null0xff.blog.dto.TagWithPostCountResponse;
import kr.null0xff.blog.entity.Tag;
import kr.null0xff.blog.service.TagService;
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
@RequestMapping("/api/tags")
@RequiredArgsConstructor
@Slf4j
public class TagController {

  private final TagService tagService;

  /**
   * Get all tags
   *
   * @return ResponseEntity with a list of all tags
   */
  @GetMapping
  public ResponseEntity<List<TagResponse>> getAllTags() {
    log.info("Fetching all tags");

    List<Tag> tags = tagService.getAllTags();

    // Convert entities to DTOs
    List<TagResponse> responseBody = tags.stream()
        .map(TagResponse::fromEntity)
        .collect(Collectors.toList());

    return ResponseEntity.ok(responseBody);
  }

  /**
   * Get all tags with post counts
   *
   * @return ResponseEntity with a list of tags with post counts
   */
  @GetMapping("/with-post-count")
  public ResponseEntity<List<TagWithPostCountResponse>> getTagsWithPostCount() {
    log.info("Fetching all tags with post counts");

    Map<Tag, Long> tagsWithCount = tagService.getTagsWithPostCount();

    // Convert map entries to DTOs
    List<TagWithPostCountResponse> responseBody = tagsWithCount.entrySet().stream()
        .map(entry -> TagWithPostCountResponse.fromEntityAndCount(entry.getKey(), entry.getValue()))
        .collect(Collectors.toList());

    return ResponseEntity.ok(responseBody);
  }

  /**
   * Get popular tags (for featured tag sections)
   *
   * @param limit Maximum number of tags to return
   * @return ResponseEntity with a list of popular tags with post counts
   */
  @GetMapping("/popular")
  public ResponseEntity<List<TagWithPostCountResponse>> getPopularTags(
      @RequestParam(defaultValue = "10") int limit) {

    log.info("Fetching {} popular tags", limit);

    Map<Tag, Long> tagsWithCount = tagService.getPopularTags(limit);

    // Convert map entries to DTOs
    List<TagWithPostCountResponse> responseBody = tagsWithCount.entrySet().stream()
        .map(entry -> TagWithPostCountResponse.fromEntityAndCount(entry.getKey(), entry.getValue()))
        .collect(Collectors.toList());

    return ResponseEntity.ok(responseBody);
  }

  /**
   * Get all non-empty tags (tags with at least one published post)
   *
   * @return ResponseEntity with a list of non-empty tags
   */
  @GetMapping("/non-empty")
  public ResponseEntity<List<TagResponse>> getNonEmptyTags() {
    log.info("Fetching non-empty tags");

    List<Tag> tags = tagService.getNonEmptyTags();

    // Convert entities to DTOs
    List<TagResponse> responseBody = tags.stream()
        .map(TagResponse::fromEntity)
        .collect(Collectors.toList());

    return ResponseEntity.ok(responseBody);
  }

  /**
   * Get a tag by its ID
   *
   * @param id Tag ID
   * @return ResponseEntity with the tag
   */
  @GetMapping("/{id}")
  public ResponseEntity<TagResponse> getTagById(@PathVariable Long id) {
    log.info("Fetching tag with ID: {}", id);

    Tag tag = tagService.getTagById(id);
    TagResponse responseBody = TagResponse.fromEntity(tag);

    return ResponseEntity.ok(responseBody);
  }

  /**
   * Get a tag by its slug
   *
   * @param slug Tag slug
   * @return ResponseEntity with the tag
   */
  @GetMapping("/by-slug/{slug}")
  public ResponseEntity<TagResponse> getTagBySlug(@PathVariable String slug) {
    log.info("Fetching tag with slug: {}", slug);

    Tag tag = tagService.getTagBySlug(slug);
    TagResponse responseBody = TagResponse.fromEntity(tag);

    return ResponseEntity.ok(responseBody);
  }

  /**
   * Find tags matching a partial name (for autocomplete)
   *
   * @param partialName Partial tag name for search
   * @return ResponseEntity with a list of matching tags
   */
  @GetMapping("/search")
  public ResponseEntity<List<TagResponse>> findTagsByPartialName(
      @RequestParam String partialName) {

    log.info("Finding tags matching partial name: {}", partialName);

    List<Tag> tags = tagService.findTagsByPartialName(partialName);

    // Convert entities to DTOs
    List<TagResponse> responseBody = tags.stream()
        .map(TagResponse::fromEntity)
        .collect(Collectors.toList());

    return ResponseEntity.ok(responseBody);
  }

  /**
   * Create a new tag
   *
   * @param request Tag creation request
   * @return ResponseEntity with the created tag
   */
  @PostMapping
  public ResponseEntity<TagResponse> createTag(@Valid @RequestBody TagRequest request) {
    log.info("Creating new tag: {}", request.getName());

    // Create a Tag entity from the request
    Tag tag = new Tag();
    tag.setName(request.getName());
    tag.setSlug(request.getSlug());
    tag.setDescription(request.getDescription());

    // Create the tag
    Tag createdTag = tagService.createTag(tag);

    // Convert entity to DTO
    TagResponse responseBody = TagResponse.fromEntity(createdTag);

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(responseBody);
  }

  /**
   * Update an existing tag
   *
   * @param id      Tag ID
   * @param request Tag update request
   * @return ResponseEntity with the updated tag
   */
  @PutMapping("/{id}")
  public ResponseEntity<TagResponse> updateTag(
      @PathVariable Long id,
      @Valid @RequestBody TagRequest request) {

    log.info("Updating tag with ID: {}", id);

    // Create a Tag entity from the request
    Tag tag = new Tag();
    tag.setName(request.getName());
    tag.setSlug(request.getSlug());
    tag.setDescription(request.getDescription());

    // Update the tag
    Tag updatedTag = tagService.updateTag(id, tag);

    // Convert entity to DTO
    TagResponse responseBody = TagResponse.fromEntity(updatedTag);

    return ResponseEntity.ok(responseBody);
  }

  /**
   * Delete a tag
   *
   * @param id Tag ID
   * @return ResponseEntity with no content
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
    log.info("Deleting tag with ID: {}", id);

    tagService.deleteTag(id);

    return ResponseEntity.noContent().build();
  }

  /**
   * Check if a tag slug is already in use
   *
   * @param slug Slug to check
   * @return ResponseEntity with boolean result
   */
  @GetMapping("/check-slug")
  public ResponseEntity<Boolean> isSlugInUse(@RequestParam String slug) {
    log.info("Checking if tag slug is in use: {}", slug);

    boolean isInUse = tagService.isSlugInUse(slug);

    return ResponseEntity.ok(isInUse);
  }

  /**
   * Get or create a tag by name
   *
   * @param name Tag name
   * @return ResponseEntity with the tag
   */
  @PostMapping("/get-or-create")
  public ResponseEntity<TagResponse> getOrCreateTag(@RequestParam String name) {
    log.info("Getting or creating tag: {}", name);

    Tag tag = tagService.getOrCreateTag(name);
    TagResponse responseBody = TagResponse.fromEntity(tag);

    return ResponseEntity.ok(responseBody);
  }
}