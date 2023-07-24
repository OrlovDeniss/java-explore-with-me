package ru.practicum.event.controller.pub;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.query.EventPublicQuery;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

public interface EventPublicController {

    String FROM = "0";
    String SIZE = "10";

    @GetMapping
    ResponseEntity<List<EventShortDto>> getShortEventsByPublic(
            @Valid EventPublicQuery eventPublicQuery,
            @RequestParam(required = false, defaultValue = FROM) Integer from,
            @RequestParam(required = false, defaultValue = SIZE) Integer size,
            HttpServletRequest request);

    @GetMapping("{eventId}")
    ResponseEntity<EventFullDto> getEventByPublic(@PathVariable @Positive Integer eventId,
                                                  HttpServletRequest request);
}
