package kr.null0xff.blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for Category requests (both creation and update)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest {

  @NotBlank(message = "Name is required")
  @Size(max = 50, message = "Name cannot exceed 50 characters")
  private String name;

  @Size(max = 200, message = "Description cannot exceed 200 characters")
  private String description;

  @Size(max = 200, message = "Slug cannot exceed 200 characters")
  private String slug;

  @Size(min = 7, max = 7, message = "Color must be a 7-character hex code (including #)")
  private String color;
}
