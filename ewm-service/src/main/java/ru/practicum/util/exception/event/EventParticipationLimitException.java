package ru.practicum.util.exception.event;

public class EventParticipationLimitException extends RuntimeException {
    public EventParticipationLimitException(String message) {
        super(message);
    }
}
