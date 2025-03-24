package kr.null0xff.blog.dto;

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
public class UserSummaryResponse {

  private Long id;
  private String username;
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
