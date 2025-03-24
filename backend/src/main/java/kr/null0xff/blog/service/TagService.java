package kr.null0xff.blog.service;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kr.null0xff.blog.entity.Tag;
import kr.null0xff.blog.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TagService {

  private final TagRepository tagRepository;

  /**
   * Get all tags
   */
  @Transactional(readOnly = true)
  public List<Tag> getAllTags() {
    log.debug("Finding all tags");
    return tagRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
  }

  /**
   * Get all tags with post counts (for tag clouds)
   */
  @Transactional(readOnly = true)
  public Map<Tag, Long> getTagsWithPostCount() {
    log.debug("Finding all tags with post counts");

    List<Object[]> results = tagRepository.findAllWithPostCount(
        Sort.by(Sort.Direction.ASC, "name"));

    return results.stream()
        .collect(Collectors.toMap(
            row -> (Tag) row[0],
            row -> (Long) row[1]
        ));
  }

  /**
   * Get popular tags (for featured tag sections)
   */
  @Transactional(readOnly = true)
  public Map<Tag, Long> getPopularTags(int limit) {
    log.debug("Finding {} popular tags", limit);

    List<Object[]> results = tagRepository.findPopularTags(limit);

    return results.stream()
        .collect(Collectors.toMap(
            row -> (Tag) row[0],
            row -> (Long) row[1]
        ));
  }

  /**
   * Get non-empty tags (tags with at least one published post)
   */
  @Transactional(readOnly = true)
  public List<Tag> getNonEmptyTags() {
    log.debug("Finding non-empty tags");
    return tagRepository.findNonEmptyTags(
        Sort.by(Sort.Direction.ASC, "name"));
  }

  /**
   * Get a tag by its ID
   */
  @Transactional(readOnly = true)
  public Tag getTagById(Long id) {
    log.debug("Finding tag with ID: {}", id);
    return tagRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Tag not found with ID: " + id));
  }

  /**
   * Get a tag by its slug
   */
  @Transactional(readOnly = true)
  public Tag getTagBySlug(String slug) {
    log.debug("Finding tag with slug: {}", slug);
    return tagRepository.findBySlug(slug)
        .orElseThrow(() -> new EntityNotFoundException("Tag not found with slug: " + slug));
  }

  /**
   * Find tags matching a partial name (for autocomplete)
   */
  @Transactional(readOnly = true)
  public List<Tag> findTagsByPartialName(String partialName) {
    log.debug("Finding tags matching partial name: {}", partialName);
    return tagRepository.findByNameContainingIgnoreCase(partialName);
  }

  /**
   * Create a new tag
   */
  @Transactional
  public Tag createTag(Tag tag) {
    log.debug("Creating new tag: {}", tag.getName());

    // Check if a tag with the same name or slug already exists
    if (tagRepository.existsByName(tag.getName())) {
      throw new IllegalArgumentException("A tag with this name already exists");
    }

    if (tag.getSlug() == null || tag.getSlug().isEmpty()) {
      // Generate a slug from the name if not provided
      tag.setSlug(generateSlugFromName(tag.getName()));
    } else if (tagRepository.existsBySlug(tag.getSlug())) {
      throw new IllegalArgumentException("A tag with this slug already exists");
    }

    return tagRepository.save(tag);
  }

  /**
   * Update an existing tag
   */
  @Transactional
  public Tag updateTag(Long tagId, Tag updatedTag) {
    log.debug("Updating tag with ID: {}", tagId);

    Tag existingTag = tagRepository.findById(tagId)
        .orElseThrow(() -> new EntityNotFoundException("Tag not found with ID: " + tagId));

    // Check if the updated name or slug would conflict with another tag
    if (!existingTag.getName().equals(updatedTag.getName()) &&
        tagRepository.existsByName(updatedTag.getName())) {
      throw new IllegalArgumentException("A tag with this name already exists");
    }

    String newSlug = updatedTag.getSlug();
    if (newSlug == null || newSlug.isEmpty()) {
      // Generate a slug from the name if not provided
      newSlug = generateSlugFromName(updatedTag.getName());
    } else if (!existingTag.getSlug().equals(newSlug) &&
        tagRepository.existsBySlug(newSlug)) {
      throw new IllegalArgumentException("A tag with this slug already exists");
    }

    // Update fields
    existingTag.setName(updatedTag.getName());
    existingTag.setSlug(newSlug);
    existingTag.setDescription(updatedTag.getDescription());

    return tagRepository.save(existingTag);
  }

  /**
   * Delete a tag This will remove the tag from all associated posts
   */
  @Transactional
  public void deleteTag(Long tagId) {
    log.debug("Deleting tag with ID: {}", tagId);

    Tag tag = tagRepository.findById(tagId)
        .orElseThrow(() -> new EntityNotFoundException("Tag not found with ID: " + tagId));

    // Remove this tag from all posts
    tag.getPosts().forEach(post -> post.removeTag(tag));

    // Now we can safely delete the tag
    tagRepository.delete(tag);
  }

  /**
   * Check if a tag slug is already in use
   */
  @Transactional(readOnly = true)
  public boolean isSlugInUse(String slug) {
    return tagRepository.existsBySlug(slug);
  }

  /**
   * Generate a slug from a tag name
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
   * Get or create a tag by name Useful when adding tags to posts
   */
  @Transactional
  public Tag getOrCreateTag(String tagName) {
    log.debug("Getting or creating tag: {}", tagName);

    return tagRepository.findByName(tagName)
        .orElseGet(() -> {
          Tag newTag = new Tag();
          newTag.setName(tagName);
          newTag.setSlug(generateSlugFromName(tagName));
          return tagRepository.save(newTag);
        });
  }
}