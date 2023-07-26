package ru.practicum.request.controller.privat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.event.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdate;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.service.RequestService;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/")
public class UserRequestPrivateControllerImpl implements UserRequestPrivateController {
    private final RequestService requestService;

    @Override
    public ResponseEntity<List<ParticipationRequestDto>> getRequestsByPrivate(Integer userId) {
        log.info("GET /users/{}/requests.", userId);
        return ResponseEntity.ok().body(requestService.getEventRequestsByPrivate(userId));
    }

    @Override
    public ResponseEntity<ParticipationRequestDto> saveNewRequestByPrivate(Integer userId, Integer eventId) {
        log.info("POST /users/{}/requests eventId: {}.", userId, eventId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(requestService.saveNewRequestByPrivate(userId, eventId));
    }

    @Override
    public ResponseEntity<ParticipationRequestDto> cancelRequestByPrivate(Integer userId, Integer requestId) {
        log.info("DELETE /users/{}/requests/{}/cancel", userId, requestId);
        return ResponseEntity.ok().body(requestService.cancelRequestByPrivate(userId, requestId));
    }

    @Override
    public ResponseEntity<List<ParticipationRequestDto>> getEventRequestsByPrivate(Integer userId,
                                                                                   Integer eventId) {
        log.info("GET users/{}/events/{}/requests", userId, eventId);
        return ResponseEntity.ok().body(requestService.getEventRequestsByPrivate(userId, eventId));
    }

    @Override
    public ResponseEntity<EventRequestStatusUpdate> updateRequestStatusByPrivate(Integer userId,
                                                                                 Integer eventId,
                                                                                 EventRequestStatusUpdateRequest dto) {
        log.info("GET users/{}/events/{}/requests", userId, eventId);
        return ResponseEntity.ok().body(requestService.updateEventRequestsStatusByPrivate(userId, eventId, dto));
    }

}
