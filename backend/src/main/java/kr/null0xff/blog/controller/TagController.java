package kr.null0xff.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@io.swagger.v3.oas.annotations.tags.Tag(name = "Tag Management", description = "APIs for managing blog tags")
public class TagController {

  private final TagService tagService;

  /**
   * Get all tags
   *
   * @return ResponseEntity with a list of all tags
   */
  @Operation(summary = "Get all tags",
      description = "Retrieves a list of all available tags")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved tags",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = TagResponse.class)))
  })
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
  @Operation(summary = "Get all tags with post counts",
      description = "Retrieves all tags with the count of posts using each tag")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved tags with post counts",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = TagWithPostCountResponse.class)))
  })
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
  @Operation(summary = "Get popular tags",
      description = "Retrieves the most popular tags based on post count")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved popular tags",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = TagWithPostCountResponse.class)))
  })
  @GetMapping("/popular")
  public ResponseEntity<List<TagWithPostCountResponse>> getPopularTags(
      @Parameter(description = "Maximum number of tags to return")
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
  @Operation(summary = "Get non-empty tags",
      description = "Retrieves tags that have at least one published post")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved non-empty tags",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = TagResponse.class)))
  })
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
  @Operation(summary = "Get tag by ID",
      description = "Retrieves a specific tag by its ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved the tag",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = TagResponse.class))),
      @ApiResponse(responseCode = "404", description = "Tag not found",
          content = @Content)
  })
  @GetMapping("/{id}")
  public ResponseEntity<TagResponse> getTagById(
      @Parameter(description = "Tag ID", required = true)
      @PathVariable Long id) {
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
  @Operation(summary = "Get tag by slug",
      description = "Retrieves a specific tag by its slug")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved the tag",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = TagResponse.class))),
      @ApiResponse(responseCode = "404", description = "Tag not found",
          content = @Content)
  })
  @GetMapping("/by-slug/{slug}")
  public ResponseEntity<TagResponse> getTagBySlug(
      @Parameter(description = "Tag slug", required = true)
      @PathVariable String slug) {
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
  @Operation(summary = "Search tags by partial name",
      description = "Finds tags that match a partial name (useful for autocomplete)")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved matching tags",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = TagResponse.class)))
  })
  @GetMapping("/search")
  public ResponseEntity<List<TagResponse>> findTagsByPartialName(
      @Parameter(description = "Partial tag name for search", required = true)
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
  @Operation(summary = "Create a new tag",
      description = "Creates a new tag with the provided details")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Tag successfully created",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = TagResponse.class))),
      @ApiResponse(responseCode = "400", description = "Invalid input or tag already exists",
          content = @Content)
  })
  @PostMapping
  public ResponseEntity<TagResponse> createTag(
      @Parameter(description = "Tag details", required = true)
      @Valid @RequestBody TagRequest request) {
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
  @Operation(summary = "Update an existing tag",
      description = "Updates a tag with the specified ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Tag successfully updated",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = TagResponse.class))),
      @ApiResponse(responseCode = "400", description = "Invalid input or name/slug conflict",
          content = @Content),
      @ApiResponse(responseCode = "404", description = "Tag not found",
          content = @Content)
  })
  @PutMapping("/{id}")
  public ResponseEntity<TagResponse> updateTag(
      @Parameter(description = "Tag ID", required = true)
      @PathVariable Long id,
      @Parameter(description = "Updated tag details", required = true)
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
  @Operation(summary = "Delete a tag",
      description = "Deletes the tag with the specified ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Tag successfully deleted",
          content = @Content),
      @ApiResponse(responseCode = "404", description = "Tag not found",
          content = @Content)
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTag(
      @Parameter(description = "Tag ID", required = true)
      @PathVariable Long id) {
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
  @Operation(summary = "Check if slug is in use",
      description = "Checks if a tag slug is already in use")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Check completed",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = Boolean.class)))
  })
  @GetMapping("/check-slug")
  public ResponseEntity<Boolean> isSlugInUse(
      @Parameter(description = "Slug to check", required = true)
      @RequestParam String slug) {
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
  @Operation(summary = "Get or create tag by name",
      description = "Retrieves an existing tag with the given name or creates a new one if it doesn't exist")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Tag retrieved or created successfully",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = TagResponse.class)))
  })
  @PostMapping("/get-or-create")
  public ResponseEntity<TagResponse> getOrCreateTag(
      @Parameter(description = "Tag name", required = true)
      @RequestParam String name) {
    log.info("Getting or creating tag: {}", name);

    Tag tag = tagService.getOrCreateTag(name);
    TagResponse responseBody = TagResponse.fromEntity(tag);

    return ResponseEntity.ok(responseBody);
  }
}