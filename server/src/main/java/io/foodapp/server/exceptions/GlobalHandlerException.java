package io.foodapp.server.exceptions;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalHandlerException {
    @ExceptionHandler({ MethodArgumentNotValidException.class, HttpMessageNotReadableException.class })
    public ResponseEntity<Map<String, Object>> handleValidationException(Exception ex, WebRequest request) {
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


        return ResponseEntity.badRequest().body(new HashMap<String, Object>() {
            {
                put("timestamp", LocalDateTime.now());
                put("path", request.getDescription(false).substring(4));
                put("message", "Validation error");
                put("errors", errors);
            }
        });
    }

    // Xử lý lỗi chung
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGlobalException(Exception ex, WebRequest request) {

        return ResponseEntity.badRequest().body(new HashMap<String, Object>() {
            {
                put("timestamp", LocalDateTime.now());
                put("path", request.getDescription(false).substring(4));
                put("message", ex.getMessage());
            }
        });
    }
}
