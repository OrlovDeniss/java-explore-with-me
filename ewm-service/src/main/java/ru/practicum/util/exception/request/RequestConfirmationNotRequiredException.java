package ru.practicum.util.exception.request;

public class RequestConfirmationNotRequiredException extends RuntimeException {
    public RequestConfirmationNotRequiredException(String message) {
        super(message);
    }
}
