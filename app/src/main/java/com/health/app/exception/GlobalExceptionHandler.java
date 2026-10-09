package com.health.app.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicateEmail(
            DuplicateEmailException exception,
            HttpServletRequest request){
        return buildResponse(HttpStatus.CONFLICT,
                "DUPLICATE_EMAIL",
                exception.getMessage(),
                List.of());
    }

    @ExceptionHandler(EmailAlreadyRegisteredException.class)
    public ResponseEntity<ApiErrorResponse> handleEmailAlreadyRegistered(
            EmailAlreadyRegisteredException exception,
            HttpServletRequest request) {
        return buildResponse(HttpStatus.CONFLICT,
                "DUPLICATE_EMAIL",
                exception.getMessage(),
                List.of());
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handlePasswordMismatch(
            PasswordMismatchException exception,
            HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST,
                "PASSWORD_MISMATCH",
                exception.getMessage(),
                List.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(
            MethodArgumentNotValidException exception,
            HttpServletRequest request){
        List<String> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ":"+error.getDefaultMessage())
                .toList();
        return buildResponse(HttpStatus.BAD_REQUEST,
                "VALIDATION_ERROR",
                "Request validation failed",
                errors);
    }

    private ResponseEntity<ApiErrorResponse> buildResponse(
            HttpStatus status,
            String code,
            String message,
            List<String> errors){
    ApiErrorResponse response = new ApiErrorResponse(
            Instant.now(),
            status.value(),
            code,
            message,
            errors
    );
    return ResponseEntity.status(status).body(response);
    }
}
