package ru.practicum.event.controller.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.UpdateEventRequest;
import ru.practicum.event.dto.query.EventAdminQuery;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

public interface EventAdminController {
    String FROM = "0";
    String SIZE = "10";

    @GetMapping
    ResponseEntity<List<EventFullDto>> getEventsByAdmin(
            EventAdminQuery query,
            @RequestParam(required = false, defaultValue = FROM) @PositiveOrZero Integer from,
            @RequestParam(required = false, defaultValue = SIZE) @Positive Integer size);

    @PatchMapping("{eventId}")
    ResponseEntity<EventFullDto> updateEventByAdmin(
            @PathVariable @Positive Integer eventId,
            @Valid @RequestBody UpdateEventRequest updateEventRequest);

}