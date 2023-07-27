package ru.practicum.util.exception.location;

public class LocationCoordinatesAlreadyExistException extends RuntimeException {
    public LocationCoordinatesAlreadyExistException(String message) {
        super(message);
    }
}
