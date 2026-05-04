package org.vsu.authservice.utils.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.vsu.authservice.auth.dto.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .responseCode(409)
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(UserServiceClientException.class)
    public ResponseEntity<ErrorResponse> handleUserServiceClientException(UserServiceClientException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .responseCode(503)
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse);
    }

    @ExceptionHandler(KeycloakRegisterException.class)
    public ResponseEntity<ErrorResponse> handleKeycloakRegisterException(KeycloakRegisterException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .responseCode(500)
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(KeycloakLoginException.class)
    public ResponseEntity<ErrorResponse> handleKeycloakLoginException(KeycloakLoginException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .responseCode(400)
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
