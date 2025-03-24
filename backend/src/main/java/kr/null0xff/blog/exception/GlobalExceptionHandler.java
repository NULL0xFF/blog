package kr.null0xff.blog.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

/**
 * Global exception handler for REST API
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  /**
   * Handle EntityNotFoundException
   */
  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleEntityNotFoundException(
      EntityNotFoundException ex, WebRequest request) {

    log.error("Entity not found: {}", ex.getMessage());

    ErrorResponse errorResponse = ErrorResponse.builder()
        .timestamp(LocalDateTime.now())
        .status(HttpStatus.NOT_FOUND.value())
        .error("Not Found")
        .message(ex.getMessage())
        .path(request.getDescription(false).replace("uri=", ""))
        .build();

    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
  }

  /**
   * Handle IllegalArgumentException
   */
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
      IllegalArgumentException ex, WebRequest request) {

    log.error("Illegal argument: {}", ex.getMessage());

    ErrorResponse errorResponse = ErrorResponse.builder()
        .timestamp(LocalDateTime.now())
        .status(HttpStatus.BAD_REQUEST.value())
        .error("Bad Request")
        .message(ex.getMessage())
        .path(request.getDescription(false).replace("uri=", ""))
        .build();

    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  /**
   * Handle validation errors from @Valid annotations
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ValidationErrorResponse> handleValidationExceptions(
      MethodArgumentNotValidException ex, WebRequest request) {

    log.error("Validation error: {}", ex.getMessage());

    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach(error -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });

    ValidationErrorResponse errorResponse = ValidationErrorResponse.builder()
        .timestamp(LocalDateTime.now())
        .status(HttpStatus.BAD_REQUEST.value())
        .error("Validation Failed")
        .message("Validation error: " + ex.getBindingResult().getErrorCount() + " errors")
        .path(request.getDescription(false).replace("uri=", ""))
        .errors(errors)
        .build();

    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  /**
   * Handle constraint violations
   */
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ValidationErrorResponse> handleConstraintViolation(
      ConstraintViolationException ex, WebRequest request) {

    log.error("Constraint violation: {}", ex.getMessage());

    Map<String, String> errors = new HashMap<>();
    ex.getConstraintViolations().forEach(violation -> {
      String propertyPath = violation.getPropertyPath().toString();
      String message = violation.getMessage();
      errors.put(propertyPath, message);
    });

    ValidationErrorResponse errorResponse = ValidationErrorResponse.builder()
        .timestamp(LocalDateTime.now())
        .status(HttpStatus.BAD_REQUEST.value())
        .error("Validation Failed")
        .message("Constraint violation: " + errors.size() + " errors")
        .path(request.getDescription(false).replace("uri=", ""))
        .errors(errors)
        .build();

    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  /**
   * Handle all other exceptions
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex, WebRequest request) {
    log.error("Unexpected error", ex);

    ErrorResponse errorResponse = ErrorResponse.builder()
        .timestamp(LocalDateTime.now())
        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
        .error("Internal Server Error")
        .message("An unexpected error occurred: " + ex.getMessage())
        .path(request.getDescription(false).replace("uri=", ""))
        .build();

    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}

/**
 * Standard error response DTO
 */
@lombok.Data
@lombok.Builder
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
class ErrorResponse {

  private LocalDateTime timestamp;
  private int status;
  private String error;
  private String message;
  private String path;
}

/**
 * Validation error response DTO with field errors
 */
@lombok.Data
@lombok.Builder
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
class ValidationErrorResponse {

  private LocalDateTime timestamp;
  private int status;
  private String error;
  private String message;
  private String path;
  private Map<String, String> errors;
}