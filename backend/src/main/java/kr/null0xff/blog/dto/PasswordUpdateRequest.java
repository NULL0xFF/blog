package kr.null0xff.blog.dto;

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
public class PasswordUpdateRequest {

  @NotBlank(message = "Current password is required")
  private String currentPassword;

  @NotBlank(message = "New password is required")
  @Size(min = 6, max = 120, message = "New password must be between 6 and 120 characters")
  private String newPassword;
}
