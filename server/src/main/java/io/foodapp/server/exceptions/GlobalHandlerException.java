package io.foodapp.server.exceptions;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
// import org.springframework.security.authentication.BadCredentialsException;
// import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import io.foodapp.server.dtos.responses.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalHandlerException {
    @ExceptionHandler({ MethodArgumentNotValidException.class, HttpMessageNotReadableException.class })
    public ResponseEntity<ErrorResponse> handleValidationException(Exception ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();

        if (ex instanceof MethodArgumentNotValidException) {
            for (FieldError fieldError : ((MethodArgumentNotValidException) ex).getBindingResult().getFieldErrors()) {
                errors.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
        } else if (ex instanceof HttpMessageNotReadableException) {
            Throwable mostSpecificCause = ((HttpMessageNotReadableException) ex).getMostSpecificCause();
            if (mostSpecificCause instanceof IllegalArgumentException) {
                errors.put("role", mostSpecificCause.getMessage()); // Chỉ xử lý lỗi Enum
            } else {
                errors.put("request", "Invalid request format");
            }
        }

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .path(request.getDescription(false).substring(4))
                .message("Validation failed")
                .errors(errors)
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    // @ExceptionHandler(BadCredentialsException.class)
    // public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {
    //     ErrorResponse errorResponse = ErrorResponse.builder()
    //             .timestamp(LocalDateTime.now())
    //             .status(HttpStatus.UNAUTHORIZED.value())
    //             .path(request.getDescription(false).substring(4))
    //             .message(ex.getMessage())
    //             .build();

    //     return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    // }

    // @ExceptionHandler(IllegalArgumentException.class)
    // public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
    //     ErrorResponse errorResponse = ErrorResponse.builder()
    //             .timestamp(LocalDateTime.now())
    //             .status(HttpStatus.UNAUTHORIZED.value())
    //             .path(request.getDescription(false).substring(4))
    //             .message(ex.getMessage())
    //             .build();

    //     return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    // }

    // Xử lý lỗi chung
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .path(request.getDescription(false).substring(4))
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
