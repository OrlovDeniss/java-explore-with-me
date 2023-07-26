package ru.practicum.event.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.UpdateEventRequest;
import ru.practicum.event.dto.query.EventAdminQuery;
import ru.practicum.event.service.EventService;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/events")
public class EventAdminControllerImpl implements EventAdminController {
    private final EventService eventService;

    @Override
    public ResponseEntity<List<EventFullDto>> getEventsByAdmin(EventAdminQuery query,
                                                               Integer from,
                                                               Integer size) {
        log.info("GET /admin/events from: {}, size: {}.", from, size);
        return ResponseEntity.ok().body(eventService.getEventsByAdmin(query, from, size));
    }

    @Override
    public ResponseEntity<EventFullDto> updateEventByAdmin(Integer eventId,
                                                           UpdateEventRequest updateEventRequest) {
        log.info("PATCH /admin/events/{} dto: {}.", eventId, updateEventRequest);
        return ResponseEntity.ok().body(eventService.updateEventByAdmin(eventId, updateEventRequest));
    }
}
