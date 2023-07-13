package ru.practicum.util.exception;

public class StartAfterEndException extends RuntimeException {
    public StartAfterEndException() {
        super("Start must be before end.");
    }
}
