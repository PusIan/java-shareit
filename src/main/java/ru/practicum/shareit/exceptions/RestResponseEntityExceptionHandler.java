package ru.practicum.shareit.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
@Slf4j
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodMismatch(MethodArgumentTypeMismatchException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        Throwable cause = ex.getCause() // First cause is a ConversionException
                .getCause(); // Second Cause is your custom exception or some other exception e.g. NullPointerException
        if (Objects.equals(cause.getClass(), UnsupportedBookingStatusFilterException.class)) {
            body.put("error", cause.getMessage());
        } else {
            body.put("error", ex.getMessage());
        }
        return ResponseEntity.badRequest().body(body);
    }
}
