package com.ratelimiter.rate_limiter.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ratelimiter.rate_limiter.dto.response.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleUnreadable(
        HttpMessageNotReadableException ex,
        HttpServletRequest request ) {
            ErrorResponse response = ErrorResponse.of(
                "INVALID_INPUT", "Validation Failed",
                request.getAttribute("requestId").toString());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
        MethodArgumentNotValidException ex,
        HttpServletRequest request) {
            String message = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .findFirst()
            .orElse("Validation Failed");

            ErrorResponse response = ErrorResponse.of(
                "INVALID_INPUT",
                message,
                request.getAttribute("requestId").toString()
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(
        ResourceNotFoundException ex,
        HttpServletRequest request) {
        String message = ex.getMessage();

        ErrorResponse response = ErrorResponse.of(
            "NOT_FOUND", message, request.getAttribute("requestId").toString());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(
        UnauthorizedException ex,
        HttpServletRequest request) {
        ErrorResponse response = ErrorResponse.of(
            "UNAUTHORIZED", ex.getMessage(), request.getAttribute("requestId").toString());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleServerException(
        Exception ex,
        HttpServletRequest request
    ) {
        ErrorResponse response = ErrorResponse.of(
            "INTERNAL_ERROR", "An unexpected error occured.",
            request.getAttribute("requestId").toString());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
