package kr.null0xff.blog.service;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import kr.null0xff.blog.entity.User;
import kr.null0xff.blog.repository.PostRepository;
import kr.null0xff.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PostRepository postRepository;

  /**
   * Get all users
   */
  @Transactional(readOnly = true)
  public List<User> getAllUsers() {
    log.debug("Finding all users");
    return userRepository.findAll();
  }

  /**
   * Get a user by ID
   */
  @Transactional(readOnly = true)
  public User getUserById(Long id) {
    log.debug("Finding user with ID: {}", id);
    return userRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));
  }

  /**
   * Get a user by username
   */
  @Transactional(readOnly = true)
  public User getUserByUsername(String username) {
    log.debug("Finding user with username: {}", username);
    return userRepository.findByUsername(username)
        .orElseThrow(
            () -> new EntityNotFoundException("User not found with username: " + username));
  }

  /**
   * Find a user by email
   */
  @Transactional(readOnly = true)
  public Optional<User> findUserByEmail(String email) {
    log.debug("Finding user with email: {}", email);
    return userRepository.findByEmail(email);
  }

  /**
   * Check if a username is already taken
   */
  @Transactional(readOnly = true)
  public boolean isUsernameTaken(String username) {
    return userRepository.existsByUsername(username);
  }

  /**
   * Check if an email is already registered
   */
  @Transactional(readOnly = true)
  public boolean isEmailRegistered(String email) {
    return userRepository.existsByEmail(email);
  }

  /**
   * Create a new user Note: In a real application, password handling would be more sophisticated
   * with encoding
   */
  @Transactional
  public User createUser(User user) {
    log.debug("Creating new user: {}", user.getUsername());

    // Check if username or email already exists
    if (userRepository.existsByUsername(user.getUsername())) {
      throw new IllegalArgumentException("Username is already taken");
    }

    if (userRepository.existsByEmail(user.getEmail())) {
      throw new IllegalArgumentException("Email is already registered");
    }

    // In a real application, you would encode the password here
    // user.setPassword(passwordEncoder.encode(user.getPassword()));

    return userRepository.save(user);
  }

  /**
   * Update user information This doesn't update the password, which should be handled separately
   * for security
   */
  @Transactional
  public User updateUser(Long userId, User updatedUser) {
    log.debug("Updating user with ID: {}", userId);

    User existingUser = userRepository.findById(userId)
        .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

    // Check if the new username is taken by another user
    if (!existingUser.getUsername().equals(updatedUser.getUsername()) &&
        userRepository.existsByUsername(updatedUser.getUsername())) {
      throw new IllegalArgumentException("Username is already taken");
    }

    // Check if the new email is used by another user
    if (!existingUser.getEmail().equals(updatedUser.getEmail()) &&
        userRepository.existsByEmail(updatedUser.getEmail())) {
      throw new IllegalArgumentException("Email is already registered");
    }

    // Update fields
    existingUser.setUsername(updatedUser.getUsername());
    existingUser.setEmail(updatedUser.getEmail());
    existingUser.setBio(updatedUser.getBio());
    existingUser.setAvatarUrl(updatedUser.getAvatarUrl());

    return userRepository.save(existingUser);
  }

  /**
   * Update password Note: In a real application, password handling would be more sophisticated
   */
  @Transactional
  public User updatePassword(Long userId, String currentPassword, String newPassword) {
    log.debug("Updating password for user with ID: {}", userId);

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

    // In a real application, you would verify the current password and encode the new one
    // if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
    //     throw new IllegalArgumentException("Current password is incorrect");
    // }
    //
    // user.setPassword(passwordEncoder.encode(newPassword));

    // For now, just update the password directly (not secure)
    if (!user.getPassword().equals(currentPassword)) {
      throw new IllegalArgumentException("Current password is incorrect");
    }

    user.setPassword(newPassword);

    return userRepository.save(user);
  }

  /**
   * Delete a user This will also delete all the user's posts and comments due to cascading
   */
  @Transactional
  public void deleteUser(Long userId) {
    log.debug("Deleting user with ID: {}", userId);

    if (!userRepository.existsById(userId)) {
      throw new EntityNotFoundException("User not found with ID: " + userId);
    }

    userRepository.deleteById(userId);
  }

  /**
   * Count the number of published posts by a user
   */
  @Transactional(readOnly = true)
  public long countPublishedPostsByUser(Long userId) {
    log.debug("Counting published posts by user with ID: {}", userId);

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

    return postRepository.countByAuthorAndPublishedTrue(user);
  }

  /**
   * Get authors (users who have published at least one post)
   */
  @Transactional(readOnly = true)
  public List<User> getAuthors() {
    log.debug("Finding all authors");

    // In a real application, you would use a custom query to find users who have published posts
    // For now, we'll return all users as a simplification
    return userRepository.findAll();
  }
}