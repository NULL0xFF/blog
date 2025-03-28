package kr.null0xff.blog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.null0xff.blog.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Summary DTO for User entities (used in nested responses)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "UserSummaryResponse", description = "Simplified response model for user data when embedded in other responses")
public class UserSummaryResponse {

  @Schema(description = "Unique identifier of the user", example = "1")
  private Long id;

  @Schema(description = "Username of the user", example = "johndoe")
  private String username;

  @Schema(description = "URL to the user's avatar image", example = "https://example.com/avatars/johndoe.jpg")
  private String avatarUrl;

  /**
   * Convert a User entity to a UserSummaryResponse DTO
   */
  public static UserSummaryResponse fromEntity(User user) {
    if (user == null) {
      return null;
    }

    return UserSummaryResponse.builder()
        .id(user.getId())
        .username(user.getUsername())
        .avatarUrl(user.getAvatarUrl())
        .build();
  }
}