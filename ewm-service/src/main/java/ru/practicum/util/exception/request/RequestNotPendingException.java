package ru.practicum.util.exception.request;

public class RequestNotPendingException extends RuntimeException {
    public RequestNotPendingException(String message) {
        super(message);
    }
}
