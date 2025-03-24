package kr.null0xff.blog.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "posts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Size(max = 200)
  private String title;

  @NotBlank
  @Size(max = 200)
  @Column(unique = true)
  private String slug;

  @NotBlank
  @Size(max = 500)
  private String description;

  @NotBlank
  @Lob
  @Column(columnDefinition = "TEXT")
  private String content;

  @Size(max = 255)
  private String imageUrl;

  @Builder.Default
  private boolean published = false;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @Column(name = "published_at")
  private LocalDateTime publishedAt;

  // Relationships
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "author_id", nullable = false)
  private User author;

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Comment> comments = new ArrayList<>();

  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinTable(
      name = "post_tags",
      joinColumns = @JoinColumn(name = "post_id"),
      inverseJoinColumns = @JoinColumn(name = "tag_id")
  )
  private Set<Tag> tags = new HashSet<>();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id")
  private Category category;

  // Convenience methods for bidirectional relationships
  public void addComment(Comment comment) {
    comments.add(comment);
    comment.setPost(this);
  }

  public void removeComment(Comment comment) {
    comments.remove(comment);
    comment.setPost(null);
  }

  public void addTag(Tag tag) {
    tags.add(tag);
    tag.getPosts().add(this);
  }

  public void removeTag(Tag tag) {
    tags.remove(tag);
    tag.getPosts().remove(this);
  }

  // Method to set publishing status
  public void publish() {
    this.published = true;
    this.publishedAt = LocalDateTime.now();
  }
}