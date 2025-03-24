package kr.null0xff.blog.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Size(min = 3, max = 50)
  @Column(unique = true)
  private String username;

  @NotBlank
  @Size(max = 100)
  @Email
  @Column(unique = true)
  private String email;

  @NotBlank
  @Size(max = 120)
  private String password;

  @Size(max = 255)
  private String bio;

  @Size(max = 255)
  private String avatarUrl;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  // Relationships
  @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Post> posts = new ArrayList<>();

  @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Comment> comments = new ArrayList<>();

  // Convenience methods for bidirectional relationships
  public void addPost(Post post) {
    posts.add(post);
    post.setAuthor(this);
  }

  public void removePost(Post post) {
    posts.remove(post);
    post.setAuthor(null);
  }

  public void addComment(Comment comment) {
    comments.add(comment);
    comment.setAuthor(this);
  }

  public void removeComment(Comment comment) {
    comments.remove(comment);
    comment.setAuthor(null);
  }
}