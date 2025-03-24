package kr.null0xff.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import kr.null0xff.blog.dto.PasswordUpdateRequest;
import kr.null0xff.blog.dto.UserCreateRequest;
import kr.null0xff.blog.dto.UserResponse;
import kr.null0xff.blog.dto.UserUpdateRequest;
import kr.null0xff.blog.entity.User;
import kr.null0xff.blog.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User Management", description = "APIs for managing blog users and authors")
public class UserController {

  private final UserService userService;

  /**
   * Get all users
   *
   * @return ResponseEntity with a list of all users
   */
  @Operation(summary = "Get all users",
      description = "Retrieves a list of all registered users")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved users",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = UserResponse.class)))
  })
  @GetMapping
  public ResponseEntity<List<UserResponse>> getAllUsers() {
    log.info("Fetching all users");

    List<User> users = userService.getAllUsers();

    // Convert entities to DTOs
    List<UserResponse> responseBody = users.stream()
        .map(UserResponse::fromEntity)
        .collect(Collectors.toList());

    return ResponseEntity.ok(responseBody);
  }

  /**
   * Get a user by ID
   *
   * @param id User ID
   * @return ResponseEntity with the user
   */
  @Operation(summary = "Get user by ID",
      description = "Retrieves a specific user by their ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved the user",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = UserResponse.class))),
      @ApiResponse(responseCode = "404", description = "User not found",
          content = @Content)
  })
  @GetMapping("/{id}")
  public ResponseEntity<UserResponse> getUserById(
      @Parameter(description = "User ID", required = true)
      @PathVariable Long id) {
    log.info("Fetching user with ID: {}", id);

    User user = userService.getUserById(id);
    UserResponse responseBody = UserResponse.fromEntity(user);

    return ResponseEntity.ok(responseBody);
  }

  /**
   * Get a user by username
   *
   * @param username Username
   * @return ResponseEntity with the user
   */
  @Operation(summary = "Get user by username",
      description = "Retrieves a specific user by their username")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved the user",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = UserResponse.class))),
      @ApiResponse(responseCode = "404", description = "User not found",
          content = @Content)
  })
  @GetMapping("/by-username/{username}")
  public ResponseEntity<UserResponse> getUserByUsername(
      @Parameter(description = "Username", required = true)
      @PathVariable String username) {
    log.info("Fetching user with username: {}", username);

    User user = userService.getUserByUsername(username);
    UserResponse responseBody = UserResponse.fromEntity(user);

    return ResponseEntity.ok(responseBody);
  }

  /**
   * Check if a username is already taken
   *
   * @param username Username to check
   * @return ResponseEntity with boolean result
   */
  @Operation(summary = "Check if username is taken",
      description = "Checks if a username is already registered")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Check completed",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = Boolean.class)))
  })
  @GetMapping("/check-username")
  public ResponseEntity<Boolean> isUsernameTaken(
      @Parameter(description = "Username to check", required = true)
      @RequestParam String username) {
    log.info("Checking if username is taken: {}", username);

    boolean isTaken = userService.isUsernameTaken(username);

    return ResponseEntity.ok(isTaken);
  }

  /**
   * Check if an email is already registered
   *
   * @param email Email to check
   * @return ResponseEntity with boolean result
   */
  @Operation(summary = "Check if email is registered",
      description = "Checks if an email address is already registered")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Check completed",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = Boolean.class)))
  })
  @GetMapping("/check-email")
  public ResponseEntity<Boolean> isEmailRegistered(
      @Parameter(description = "Email to check", required = true)
      @RequestParam String email) {
    log.info("Checking if email is registered: {}", email);

    boolean isRegistered = userService.isEmailRegistered(email);

    return ResponseEntity.ok(isRegistered);
  }

  /**
   * Create a new user
   *
   * @param request User creation request
   * @return ResponseEntity with the created user
   */
  @Operation(summary = "Create a new user",
      description = "Registers a new user with the provided details")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "User successfully created",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = UserResponse.class))),
      @ApiResponse(responseCode = "400", description = "Invalid input or username/email already in use",
          content = @Content)
  })
  @PostMapping
  public ResponseEntity<UserResponse> createUser(
      @Parameter(description = "User details", required = true)
      @Valid @RequestBody UserCreateRequest request) {
    log.info("Creating new user: {}", request.getUsername());

    // Create a User entity from the request
    User user = new User();
    user.setUsername(request.getUsername());
    user.setEmail(request.getEmail());
    user.setPassword(request.getPassword());
    user.setBio(request.getBio());
    user.setAvatarUrl(request.getAvatarUrl());

    // Create the user
    User createdUser = userService.createUser(user);

    // Convert entity to DTO
    UserResponse responseBody = UserResponse.fromEntity(createdUser);

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(responseBody);
  }

  /**
   * Update user information
   *
   * @param id      User ID
   * @param request User update request
   * @return ResponseEntity with the updated user
   */
  @Operation(summary = "Update user information",
      description = "Updates a user's profile information")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "User successfully updated",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = UserResponse.class))),
      @ApiResponse(responseCode = "400", description = "Invalid input or username/email conflict",
          content = @Content),
      @ApiResponse(responseCode = "404", description = "User not found",
          content = @Content)
  })
  @PutMapping("/{id}")
  public ResponseEntity<UserResponse> updateUser(
      @Parameter(description = "User ID", required = true)
      @PathVariable Long id,
      @Parameter(description = "Updated user details", required = true)
      @Valid @RequestBody UserUpdateRequest request) {

    log.info("Updating user with ID: {}", id);

    // Create a User entity from the request
    User user = new User();
    user.setUsername(request.getUsername());
    user.setEmail(request.getEmail());
    user.setBio(request.getBio());
    user.setAvatarUrl(request.getAvatarUrl());

    // Update the user
    User updatedUser = userService.updateUser(id, user);

    // Convert entity to DTO
    UserResponse responseBody = UserResponse.fromEntity(updatedUser);

    return ResponseEntity.ok(responseBody);
  }

  /**
   * Update user password
   *
   * @param id      User ID
   * @param request Password update request
   * @return ResponseEntity with the updated user
   */
  @Operation(summary = "Update user password",
      description = "Updates a user's password after verifying the current password")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Password successfully updated",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = UserResponse.class))),
      @ApiResponse(responseCode = "400", description = "Invalid input or incorrect current password",
          content = @Content),
      @ApiResponse(responseCode = "404", description = "User not found",
          content = @Content)
  })
  @PutMapping("/{id}/password")
  public ResponseEntity<UserResponse> updatePassword(
      @Parameter(description = "User ID", required = true)
      @PathVariable Long id,
      @Parameter(description = "Password update details", required = true)
      @Valid @RequestBody PasswordUpdateRequest request) {

    log.info("Updating password for user with ID: {}", id);

    User updatedUser = userService.updatePassword(id, request.getCurrentPassword(),
        request.getNewPassword());

    // Convert entity to DTO
    UserResponse responseBody = UserResponse.fromEntity(updatedUser);

    return ResponseEntity.ok(responseBody);
  }

  /**
   * Delete a user
   *
   * @param id User ID
   * @return ResponseEntity with no content
   */
  @Operation(summary = "Delete a user",
      description = "Deletes the user with the specified ID and all their content")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "User successfully deleted",
          content = @Content),
      @ApiResponse(responseCode = "404", description = "User not found",
          content = @Content)
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(
      @Parameter(description = "User ID", required = true)
      @PathVariable Long id) {
    log.info("Deleting user with ID: {}", id);

    userService.deleteUser(id);

    return ResponseEntity.noContent().build();
  }

  /**
   * Count the number of published posts by a user
   *
   * @param id User ID
   * @return ResponseEntity with the count
   */
  @Operation(summary = "Count published posts by user",
      description = "Counts the number of published posts by a specific user")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Count retrieved successfully",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = Long.class))),
      @ApiResponse(responseCode = "404", description = "User not found",
          content = @Content)
  })
  @GetMapping("/{id}/post-count")
  public ResponseEntity<Long> countPublishedPostsByUser(
      @Parameter(description = "User ID", required = true)
      @PathVariable Long id) {
    log.info("Counting published posts by user with ID: {}", id);

    long count = userService.countPublishedPostsByUser(id);

    return ResponseEntity.ok(count);
  }

  /**
   * Get authors (users who have published at least one post)
   *
   * @return ResponseEntity with a list of authors
   */
  @Operation(summary = "Get all authors",
      description = "Retrieves users who have published at least one post")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved authors",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = UserResponse.class)))
  })
  @GetMapping("/authors")
  public ResponseEntity<List<UserResponse>> getAuthors() {
    log.info("Fetching all authors");

    List<User> authors = userService.getAuthors();

    // Convert entities to DTOs
    List<UserResponse> responseBody = authors.stream()
        .map(UserResponse::fromEntity)
        .collect(Collectors.toList());

    return ResponseEntity.ok(responseBody);
  }
}