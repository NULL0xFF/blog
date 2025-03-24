package kr.null0xff.blog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for password update requests
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "PasswordUpdateRequest", description = "Request model for updating a user's password")
public class PasswordUpdateRequest {

  @Schema(description = "User's current password for verification", example = "OldP@ssw0rd",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @NotBlank(message = "Current password is required")
  private String currentPassword;

  @Schema(description = "New password to set for the account", example = "NewSecureP@ssw0rd",
      requiredMode = Schema.RequiredMode.REQUIRED, minLength = 6, maxLength = 120)
  @NotBlank(message = "New password is required")
  @Size(min = 6, max = 120, message = "New password must be between 6 and 120 characters")
  private String newPassword;
}