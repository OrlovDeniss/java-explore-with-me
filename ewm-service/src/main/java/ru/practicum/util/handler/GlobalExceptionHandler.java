package ru.practicum.util.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.util.exception.category.CategoryNotFoundException;
import ru.practicum.util.exception.event.*;
import ru.practicum.util.exception.request.AutorRequestException;
import ru.practicum.util.exception.request.RequestAlreadyExistException;
import ru.practicum.util.exception.request.RequestConfirmationNotRequiredException;
import ru.practicum.util.exception.user.UserNotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final String INCORRECT_REQUEST = "Incorrectly made request";

    @ExceptionHandler
    public ResponseEntity<ApiError> handleModelFieldsConstraint(HttpServletRequest request,
                                                                final MethodArgumentNotValidException e) {
        StringBuilder errors = new StringBuilder();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            errors.append("Field: ")
                    .append(error.getField())
                    .append(": ")
                    .append("Error: ")
                    .append(error.getDefaultMessage())
                    .append(", ");
        }
        errors.deleteCharAt(errors.length() - 2);
        return buildApiError(request, errors.toString(), INCORRECT_REQUEST,
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    public ResponseEntity<ApiError> handleBadRequest(HttpServletRequest request,
                                                     final Exception e) {
        return buildApiError(request, e.getMessage(), INCORRECT_REQUEST,
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            EventPublicQueryException.class,
            RequestConfirmationNotRequiredException.class
    })
    public ResponseEntity<ApiError> handleBadRequest(HttpServletRequest request,
                                                     final RuntimeException e) {
        return buildApiError(request, e.getMessage(), INCORRECT_REQUEST,
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            CategoryNotFoundException.class,
            UserNotFoundException.class,
            EventNotFoundException.class
    })
    public ResponseEntity<ApiError> handleNotFound(HttpServletRequest request,
                                                   final RuntimeException e) {
        return buildApiError(request, e.getMessage(), "The required object was not found",
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
            DataIntegrityViolationException.class,
            EventCreateUpdateRulesException.class,
            UpdateEventException.class,
            EventUpdateStateException.class,
            RequestAlreadyExistException.class,
            AutorRequestException.class,
            EventNotPublishedException.class,
            EventParticipationLimitException.class
    })
    public ResponseEntity<ApiError> handleConflict(HttpServletRequest request,
                                                   final RuntimeException e) {
        return buildApiError(request, e.getMessage(), "Integrity constraint has been violated",
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> handleThrowable(HttpServletRequest request,
                                                    final Throwable e) {
        return buildApiError(request, e.getMessage(), "An unexpected error occurred while processing the request",
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ApiError> buildApiError(HttpServletRequest request,
                                                   String message,
                                                   String reason,
                                                   HttpStatus httpStatus) {
        ApiError apiError = ApiError.builder()
                .status(httpStatus)
                .reason(reason)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
        log.error("{} {} {}", request.getMethod(), request.getRequestURI(), apiError);
        return ResponseEntity.status(httpStatus).body(apiError);
    }

}
