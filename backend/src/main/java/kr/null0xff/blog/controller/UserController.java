package kr.null0xff.blog.controller;

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
public class UserController {

  private final UserService userService;

  /**
   * Get all users
   *
   * @return ResponseEntity with a list of all users
   */
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
  @GetMapping("/{id}")
  public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
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
  @GetMapping("/by-username/{username}")
  public ResponseEntity<UserResponse> getUserByUsername(@PathVariable String username) {
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
  @GetMapping("/check-username")
  public ResponseEntity<Boolean> isUsernameTaken(@RequestParam String username) {
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
  @GetMapping("/check-email")
  public ResponseEntity<Boolean> isEmailRegistered(@RequestParam String email) {
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
  @PostMapping
  public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserCreateRequest request) {
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
  @PutMapping("/{id}")
  public ResponseEntity<UserResponse> updateUser(
      @PathVariable Long id,
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
  @PutMapping("/{id}/password")
  public ResponseEntity<UserResponse> updatePassword(
      @PathVariable Long id,
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
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
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
  @GetMapping("/{id}/post-count")
  public ResponseEntity<Long> countPublishedPostsByUser(@PathVariable Long id) {
    log.info("Counting published posts by user with ID: {}", id);

    long count = userService.countPublishedPostsByUser(id);

    return ResponseEntity.ok(count);
  }

  /**
   * Get authors (users who have published at least one post)
   *
   * @return ResponseEntity with a list of authors
   */
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