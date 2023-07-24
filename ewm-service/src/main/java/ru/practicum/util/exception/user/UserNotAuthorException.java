package ru.practicum.util.exception.user;

public class UserNotAuthorException extends RuntimeException {
    public UserNotAuthorException(String message) {
        super(message);
    }
}
