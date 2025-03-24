package kr.null0xff.blog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "UserCreateRequest", description = "Request model for creating a new user account")
public class UserCreateRequest {

  @Schema(description = "Username for the account (must be unique)", example = "johndoe", requiredMode = Schema.RequiredMode.REQUIRED)
  @NotBlank(message = "Username is required")
  @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
  private String username;

  @Schema(description = "Email address (must be unique)", example = "john.doe@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
  @NotBlank(message = "Email is required")
  @Size(max = 100, message = "Email cannot exceed 100 characters")
  @Email(message = "Email must be valid")
  private String email;

  @Schema(description = "Account password", example = "SecureP@ssw0rd", requiredMode = Schema.RequiredMode.REQUIRED,
      minLength = 6, maxLength = 120)
  @NotBlank(message = "Password is required")
  @Size(min = 6, max = 120, message = "Password must be between 6 and 120 characters")
  private String password;

  @Schema(description = "Short biography or description", example = "Software developer with 5 years of experience in Java and Spring Boot.",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @Size(max = 255, message = "Bio cannot exceed 255 characters")
  private String bio;

  @Schema(description = "URL to the user's avatar image", example = "https://example.com/avatars/johndoe.jpg",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @Size(max = 255, message = "Avatar URL cannot exceed 255 characters")
  private String avatarUrl;
}