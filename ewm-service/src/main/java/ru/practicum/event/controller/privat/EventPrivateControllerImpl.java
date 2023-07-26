package ru.practicum.event.controller.privat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventRequest;
import ru.practicum.event.service.EventService;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class EventPrivateControllerImpl implements EventPrivateController {
    private final EventService eventService;

    @Override
    public ResponseEntity<List<EventShortDto>> getShortEventsByUser(Integer userId,
                                                                    Integer from,
                                                                    Integer size) {
        log.info("GET users/{}/events from: {}, size: {}.", userId, from, size);
        return ResponseEntity.ok().body(eventService.getShortEventsByUser(userId, from, size));
    }

    @Override
    public ResponseEntity<EventFullDto> saveNewEventByUser(Integer userId,
                                                           NewEventDto newEventDto) {
        log.info("POST users/{}/events dto: {}.", userId, newEventDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(eventService.saveNewEventByUser(userId, newEventDto));
    }

    @Override
    public ResponseEntity<EventFullDto> getFullEventByUser(Integer userId,
                                                           Integer eventId) {
        log.info("GET users/{}/events/{}.", userId, eventId);
        return ResponseEntity.ok().body(eventService.getFullEventByUser(userId, eventId));
    }

    @Override
    public ResponseEntity<EventFullDto> updateEventByUser(Integer userId,
                                                          Integer eventId,
                                                          UpdateEventRequest updateEventRequest) {
        log.info("PATCH users/{}/events/{} dto: {}.", userId, eventId, updateEventRequest);
        return ResponseEntity.ok().body(eventService.updateEventByUser(userId, eventId, updateEventRequest));
    }

}
