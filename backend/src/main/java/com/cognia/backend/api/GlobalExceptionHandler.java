package com.cognia.backend.api;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorEnvelope> handleApiException(ApiException ex) {
        return ResponseEntity.status(ex.status())
                .body(new ErrorEnvelope(new ErrorBody(ex.code(), ex.getMessage(), List.of())));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorEnvelope> handleValidation(MethodArgumentNotValidException ex) {
        List<ValidationIssue> issues = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new ValidationIssue(error.getField(), error.getDefaultMessage()))
                .toList();
        return ResponseEntity.badRequest()
                .body(new ErrorEnvelope(new ErrorBody(
                        "VALIDATION_ERROR", "Request validation failed.", issues)));
    }

    public record ErrorEnvelope(ErrorBody error) {

    }

    public record ErrorBody(String code, String message, List<ValidationIssue> details) {

    }

    public record ValidationIssue(String field, String message) {

    }
}
