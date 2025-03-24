package kr.null0xff.blog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "UserResponse", description = "Response model for user account data")
public class UserResponse {

  @Schema(description = "Unique identifier of the user", example = "1")
  private Long id;

  @Schema(description = "Username for the account", example = "johndoe")
  private String username;

  @Schema(description = "Email address", example = "john.doe@example.com")
  private String email;

  @Schema(description = "Short biography or description", example = "Software developer with 5 years of experience in Java and Spring Boot.")
  private String bio;

  @Schema(description = "URL to the user's avatar image", example = "https://example.com/avatars/johndoe.jpg")
  private String avatarUrl;

  @Schema(description = "Date and time when the user account was created", example = "2023-01-10T08:30:00")
  private LocalDateTime createdAt;

  @Schema(description = "Date and time when the user account was last updated", example = "2023-02-15T16:45:30")
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