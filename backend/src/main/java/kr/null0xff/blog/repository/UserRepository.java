package kr.null0xff.blog.repository;

import java.util.Optional;
import kr.null0xff.blog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  /**
   * Find a user by their username
   *
   * @param username the username to search for
   * @return an Optional containing the user if found
   */
  Optional<User> findByUsername(String username);

  /**
   * Find a user by their email address
   *
   * @param email the email address to search for
   * @return an Optional containing the user if found
   */
  Optional<User> findByEmail(String email);

  /**
   * Check if a username is already taken
   *
   * @param username the username to check
   * @return true if the username exists, false otherwise
   */
  boolean existsByUsername(String username);

  /**
   * Check if an email is already registered
   *
   * @param email the email to check
   * @return true if the email exists, false otherwise
   */
  boolean existsByEmail(String email);
}