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
 * DTO for User update requests
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "UserUpdateRequest", description = "Request model for updating user profile information")
public class UserUpdateRequest {

  @Schema(description = "Updated username (must be unique)", example = "john_doe",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @NotBlank(message = "Username is required")
  @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
  private String username;

  @Schema(description = "Updated email address (must be unique)", example = "john.doe@example.com",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @NotBlank(message = "Email is required")
  @Size(max = 100, message = "Email cannot exceed 100 characters")
  @Email(message = "Email must be valid")
  private String email;

  @Schema(description = "Updated short biography or description",
      example = "Software developer with 5 years of experience in Java and Spring Boot. Now focused on cloud-native development.",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @Size(max = 255, message = "Bio cannot exceed 255 characters")
  private String bio;

  @Schema(description = "Updated URL to the user's avatar image",
      example = "https://example.com/avatars/john_doe_2023.jpg",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @Size(max = 255, message = "Avatar URL cannot exceed 255 characters")
  private String avatarUrl;
}