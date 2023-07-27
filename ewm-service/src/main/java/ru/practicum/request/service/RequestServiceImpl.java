package ru.practicum.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.dto.EventRequestStatusUpdateRequest;
import ru.practicum.event.enums.State;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.request.dto.EventRequestStatusUpdate;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.enums.Status;
import ru.practicum.request.mapper.RequestMapper;
import ru.practicum.request.model.Request;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;
import ru.practicum.util.exception.event.EventNotFoundException;
import ru.practicum.util.exception.event.EventNotPublishedException;
import ru.practicum.util.exception.event.EventParticipationLimitException;
import ru.practicum.util.exception.request.*;
import ru.practicum.util.exception.user.UserNotAuthorException;
import ru.practicum.util.exception.user.UserNotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static ru.practicum.request.enums.Status.CONFIRMED;

@Service
@Transactional
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getEventRequestsByPrivate(Integer userId) {
        throwWhenUserNotFound(userId);
        return toDto(requestRepository.findAllByRequesterId(userId));
    }

    @Override
    public ParticipationRequestDto saveNewRequestByPrivate(Integer userId, Integer eventId) {
        throwWhenUserNotFound(userId);
        throwWhenEventNotFound(eventId);
        throwWhenRequestAlreadyExists(userId, eventId);
        throwWhenAuthorCreateRequest(userId, eventId);
        throwWhenEventNotPublished(eventId);
        throwWhenEventParticipationLimitReached(eventId);
        Event event = findEventOrThrow(eventId);
        Status requestStatus;
        if (event.isRequestModeration() && event.getParticipantLimit() != 0) {
            requestStatus = Status.PENDING;
        } else {
            requestStatus = CONFIRMED;
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        }
        return toDto(requestRepository.save(
                Request.builder()
                        .created(LocalDateTime.now())
                        .requester(findRequesterOrThrow(userId))
                        .event(event)
                        .status(requestStatus)
                        .build()));
    }

    @Override
    public ParticipationRequestDto cancelRequestByPrivate(Integer userId, Integer requestId) {
        var request = findRequestWithEventOrThrow(userId, requestId);
        if (request.getStatus() == CONFIRMED) {
            var event = request.getEvent();
            event.setConfirmedRequests(event.getConfirmedRequests() - 1);
        }
        request.setStatus(Status.CANCELED);
        return toDto(requestRepository.save(request));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getEventRequestsByPrivate(Integer userId, Integer eventId) {
        throwWhenNotAuthorEvent(userId, eventId);
        return toDto(requestRepository.findAllByEventId(eventId));
    }

    @Override
    public EventRequestStatusUpdate updateEventRequestsStatusByPrivate(Integer userId,
                                                                       Integer eventId,
                                                                       EventRequestStatusUpdateRequest dto) {
        throwWhenEventNotFound(eventId);
        throwWhenNotAuthorEvent(userId, eventId);
        throwWhenConfirmationNotRequired(eventId);
        throwWhenEventParticipationLimitReached(eventId);
        var event = findEventOrThrow(eventId);
        List<Request> requests = requestRepository.findAllById(dto.getRequestIds());
        List<Request> confirmed = new ArrayList<>();
        List<Request> rejected = new ArrayList<>();
        for (Request request : requests) {
            if (isParticipationLimitFree(event) && dto.getStatus() == CONFIRMED) {
                confirmedWhenRequestStatusPendingOrThrow(request, event);
                confirmed.add(request);
            } else {
                request.setStatus(Status.REJECTED);
                rejected.add(request);
            }
        }
        eventRepository.save(event);
        requestRepository.saveAll(requests);
        return EventRequestStatusUpdate.builder()
                .confirmedRequests(toDto(confirmed))
                .rejectedRequests(toDto(rejected))
                .build();
    }

    private void confirmedWhenRequestStatusPendingOrThrow(Request request, Event event) {
        if (request.getStatus() == Status.PENDING) {
            request.setStatus(CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        } else {
            throw new RequestNotPendingException(
                    String.format("Request with id=%d status not pending", request.getId())
            );
        }
    }

    private boolean isParticipationLimitFree(Event event) {
        if (event.getParticipantLimit() == 0) {
            return true;
        } else {
            return event.getParticipantLimit() - event.getConfirmedRequests() > 0;
        }
    }

    private void throwWhenConfirmationNotRequired(Integer eventId) {
        if (eventRepository.existsByIdAndParticipantLimitIsZeroOrRequestModerationFalse(eventId)) {
            throw new RequestConfirmationNotRequiredException(
                    String.format("For event id=%d confirmation is not required", eventId)
            );
        }
    }

    private void throwWhenNotAuthorEvent(Integer userId, Integer eventId) {
        if (!eventRepository.existsByIdAndInitiator_Id(eventId, userId)) {
            throw new UserNotAuthorException(
                    String.format("The user with id=%d is not the author event with id=%d", userId, eventId)
            );
        }
    }

    private Request findRequestWithEventOrThrow(Integer userId, Integer requestId) {
        return requestRepository.findByIdAndRequesterId(requestId, userId)
                .orElseThrow(() -> {
                    throw new RequestNotFoundException(
                            String.format("Request with id=%d was not found", requestId));
                });
    }

    private User findRequesterOrThrow(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    throw new RequestNotFoundException(
                            String.format("User with id=%d was not found", userId));
                });
    }

    private Event findEventOrThrow(Integer eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> {
                    throw new EventNotFoundException(
                            String.format("Event id=%d was not found", eventId));
                });
    }

    private void throwWhenUserNotFound(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(
                    String.format("User with id=%d was not found", userId));
        }
    }

    private void throwWhenEventParticipationLimitReached(Integer eventId) {
        if (!eventRepository.existsByIdAndParticipantLimitGreaterThenConfirmedRequestsOrZero(eventId)) {
            throw new EventParticipationLimitException(
                    String.format("Event with id=%d participation limit reached", eventId));
        }
    }

    private void throwWhenEventNotPublished(Integer eventId) {
        if (!eventRepository.existsByIdAndState(eventId, State.PUBLISHED)) {
            throw new EventNotPublishedException(
                    String.format("Event with id=%d is not published", eventId));
        }
    }

    private void throwWhenRequestAlreadyExists(Integer userId, Integer eventId) {
        if (requestRepository.existsByRequesterIdAndEventId(userId, eventId)) {
            throw new RequestAlreadyExistException(
                    String.format("The request by userId=%d & eventId=%d already exist", userId, eventId));
        }
    }

    private void throwWhenEventNotFound(Integer eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new EventNotFoundException(
                    String.format("Event id=%d was not found", eventId));
        }
    }

    private void throwWhenAuthorCreateRequest(Integer userId, Integer eventId) {
        if (eventRepository.existsByIdAndInitiator_Id(eventId, userId)) {
            throw new AutorRequestException("Author cannot request own event");
        }
    }

    private ParticipationRequestDto toDto(Request request) {
        return RequestMapper.INSTANCE.toDto(request);
    }

    private List<ParticipationRequestDto> toDto(List<Request> request) {
        return RequestMapper.INSTANCE.toDto(request);
    }

}
