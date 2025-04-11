package com.emotion_apiserver.controller.advice;


import com.emotion_apiserver.config.util.CustomJWTException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseEntity.badRequest().body(Map.of(
                "error", "입력값이 올바르지 않습니다.",
                "fieldErrors", fieldErrors
        ));
    }

    @ExceptionHandler(CustomJWTException.class)
    public ResponseEntity<?> handleCustomJWTException(CustomJWTException ex) {
        String msg = ex.getMessage();
        return ResponseEntity.ok().body(Map.of("error", msg));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(
                Map.of(
                        "success", false,
                        "error", ex.getMessage()
                )
        );
    }
}
