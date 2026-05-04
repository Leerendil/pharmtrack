package org.vsu.catalogservice.utils.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.vsu.catalogservice.dto.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ApplicationMalfunctionException.class)
    public ResponseEntity<ErrorResponse> handleApplicationMalfunctionException(ApplicationMalfunctionException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .responseCode(500)
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(KafkaMalfunctionExecution.class)
    public ResponseEntity<ErrorResponse> handleKafkaMalfunctionExecution(KafkaMalfunctionExecution ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .responseCode(503)
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse);
    }

    @ExceptionHandler({
            MedicineAlreadyExistsException.class,
            ManufacturerAlreadyExistsException.class,
            CategoryAlreadyExistsException.class
    })
    public ResponseEntity<ErrorResponse> handleConflictException(RuntimeException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .responseCode(409)
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler({
            MedicineNotFoundException.class,
            ManufacturerNotFoundException.class,
            CategoryNotFoundException.class,
            ApplicationNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFoundException(RuntimeException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .responseCode(404)
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
}
