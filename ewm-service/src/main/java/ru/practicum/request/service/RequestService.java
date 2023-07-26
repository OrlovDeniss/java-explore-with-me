package ru.practicum.request.service;

import ru.practicum.event.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdate;
import ru.practicum.request.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {

    List<ParticipationRequestDto> getEventRequestsByPrivate(Integer userId);

    ParticipationRequestDto saveNewRequestByPrivate(Integer userId, Integer eventId);

    ParticipationRequestDto cancelRequestByPrivate(Integer userId, Integer requestId);

    List<ParticipationRequestDto> getEventRequestsByPrivate(Integer userId, Integer eventId);

    EventRequestStatusUpdate updateEventRequestsStatusByPrivate(Integer userId,
                                                                Integer eventId,
                                                                EventRequestStatusUpdateRequest updateRequest);

}