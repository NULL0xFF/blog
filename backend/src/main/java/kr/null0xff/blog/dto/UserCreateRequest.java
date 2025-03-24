package kr.null0xff.blog.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for User creation requests
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequest {

  @NotBlank(message = "Username is required")
  @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
  private String username;

  @NotBlank(message = "Email is required")
  @Size(max = 100, message = "Email cannot exceed 100 characters")
  @Email(message = "Email must be valid")
  private String email;

  @NotBlank(message = "Password is required")
  @Size(min = 6, max = 120, message = "Password must be between 6 and 120 characters")
  private String password;

  @Size(max = 255, message = "Bio cannot exceed 255 characters")
  private String bio;

  @Size(max = 255, message = "Avatar URL cannot exceed 255 characters")
  private String avatarUrl;
}
