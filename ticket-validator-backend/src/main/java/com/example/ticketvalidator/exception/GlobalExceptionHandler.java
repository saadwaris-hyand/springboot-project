package com.example.ticketvalidator.exception;


import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidFormat(InvalidFormatException ex) {
        String fieldName = ex.getPath().get(0).getFieldName();
        String invalidValue = Objects.toString(ex.getValue(), "null");

        String message = String.format("Invalid value '%s' for field '%s'. Expected one of: %s",
                invalidValue,
                fieldName,
                getEnumValues(ex)
        );

        return ResponseEntity.badRequest().body(Map.of(
                "status", "failed",
                "errors", List.of(message)
        ));
    }

    private List<String> getEnumValues(InvalidFormatException ex) {
        Class<?> targetType = ex.getTargetType();
        if (targetType.isEnum()) {
            Object[] constants = targetType.getEnumConstants();
            return Arrays.stream(constants).map(Object::toString).toList();
        }
        return List.of("Unknown");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.toList());

        return ResponseEntity.badRequest().body(Map.of(
                "status", "failed",
                "errors", errors
        ));
    }
}
