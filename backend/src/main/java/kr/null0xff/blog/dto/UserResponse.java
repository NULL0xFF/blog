package kr.null0xff.blog.dto;

import java.time.LocalDateTime;
import kr.null0xff.blog.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for User responses
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

  private Long id;
  private String username;
  private String email;
  private String bio;
  private String avatarUrl;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  /**
   * Convert a User entity to a UserResponse DTO
   */
  public static UserResponse fromEntity(User user) {
    if (user == null) {
      return null;
    }

    return UserResponse.builder()
        .id(user.getId())
        .username(user.getUsername())
        .email(user.getEmail())
        .bio(user.getBio())
        .avatarUrl(user.getAvatarUrl())
        .createdAt(user.getCreatedAt())
        .updatedAt(user.getUpdatedAt())
        .build();
  }
}