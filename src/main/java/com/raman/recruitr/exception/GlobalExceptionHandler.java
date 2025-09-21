package com.raman.recruitr.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.nio.file.AccessDeniedException;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        ApiErrorResponse error = ApiErrorResponse.of(HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(MethodArgumentTypeMismatchException ex) {
        ApiErrorResponse error = ApiErrorResponse.of(HttpStatus.BAD_REQUEST, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));

        ApiErrorResponse error = ApiErrorResponse.of(HttpStatus.BAD_REQUEST, message);
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationExceptions(HttpMessageNotReadableException ex) {
        ApiErrorResponse error = ApiErrorResponse.of(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage());
        return ResponseEntity.badRequest().body(error);
    }

    // Handle ConstraintViolations (e.g. @Validated on path params)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
        String message = ex.getConstraintViolations().stream()
                .map(cv -> cv.getPropertyPath() + ": " + cv.getMessage())
                .collect(Collectors.joining("; "));
        ApiErrorResponse error = ApiErrorResponse.of(HttpStatus.BAD_REQUEST, message);
        return ResponseEntity.badRequest().body(error);
    }

    // Handle Access Denied (403)
    @ExceptionHandler({AccessDeniedException.class, AuthorizationDeniedException.class})
    public ResponseEntity<ApiErrorResponse> handleAccessDenied(AuthorizationDeniedException ex) {
        ApiErrorResponse error = ApiErrorResponse.of(HttpStatus.FORBIDDEN,
                ex.getMessage() != null ? ex.getMessage() : "You do not have permission to access this resource.");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ApiErrorResponse> handleForbiddenException(ForbiddenException ex) {
        ApiErrorResponse error = ApiErrorResponse.of(HttpStatus.FORBIDDEN,
                ex.getMessage() != null ? ex.getMessage() : "An unexpected error occurred");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    // Handle generic exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(Exception ex) {
        ApiErrorResponse error = ApiErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage() != null ? ex.getMessage() : "An unexpected error occurred");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
