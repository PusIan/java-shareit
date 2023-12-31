package ru.practicum.shareit.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class PermissionViolationException extends RuntimeException {
    public PermissionViolationException(String message) {
        super(message);
    }
}
