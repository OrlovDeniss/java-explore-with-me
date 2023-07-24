package ru.practicum.event.controller.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.query.EventPublicQuery;
import ru.practicum.event.service.EventService;
import ru.practicum.util.exception.event.EventPublicQueryException;
import ru.practicum.util.statsclient.EwmStatsClient;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventPublicControllerImpl implements EventPublicController {
    private final EventService eventService;
    private final EwmStatsClient ewmStatsClient;

    @Override
    public ResponseEntity<List<EventShortDto>> getShortEventsByPublic(EventPublicQuery query,
                                                                      Integer from,
                                                                      Integer size,
                                                                      HttpServletRequest request) {
        throwWhenStartAfterEnd(query.getRangeStart(), query.getRangeEnd());
        log.info("GET events query: {}, from: {}, size: {}.", query, from, size);
        ewmStatsClient.saveEndpointHit(request);
        return ResponseEntity.ok().body(eventService.getShortEventsByPublic(query, from, size));
    }

    @Override
    public ResponseEntity<EventFullDto> getEventByPublic(Integer eventId, HttpServletRequest request) {
        log.info("GET events/{}.", eventId);
        ewmStatsClient.saveEndpointHit(request);
        return ResponseEntity.ok().body(eventService.getEventByPublic(eventId));
    }

    private void throwWhenStartAfterEnd(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end)) {
            throw new EventPublicQueryException("rangeStart must be before rangeEnd");
        }
    }
}
