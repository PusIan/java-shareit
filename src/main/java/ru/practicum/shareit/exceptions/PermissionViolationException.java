package ru.practicum.shareit.exceptions;

public class PermissionViolationException extends RuntimeException {
    public PermissionViolationException(String message) {
        super(message);
    }
}
