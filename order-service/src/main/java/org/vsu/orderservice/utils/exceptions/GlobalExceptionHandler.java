package org.vsu.orderservice.utils.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import org.vsu.orderservice.dto.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ForbiddenActionException.class)
    public ResponseEntity<ErrorResponse> handleForbiddenActionException(ForbiddenActionException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .responseCode(403)
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleOrderNotFoundException(OrderNotFoundException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .responseCode(404)
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }


    @ExceptionHandler({
            FailedToAddItemException.class,
            OutboxMalfunctionException.class
    })
    public ResponseEntity<ErrorResponse> handleFailedToAddItemException(RuntimeException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .responseCode(500)
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(KafkaMalfunctionException.class)
    public ResponseEntity<ErrorResponse> handleKafkaMalfunctionException(KafkaMalfunctionException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .responseCode(503)
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse);
    }
}
