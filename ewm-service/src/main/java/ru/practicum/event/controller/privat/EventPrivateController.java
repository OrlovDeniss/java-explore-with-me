package ru.practicum.event.controller.privat;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventRequest;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

public interface EventPrivateController {

    String FROM = "0";
    String SIZE = "10";

    @GetMapping("{userId}/events")
    ResponseEntity<List<EventShortDto>> getShortEventsByUser(
            @PathVariable @Positive Integer userId,
            @RequestParam(required = false, defaultValue = FROM) @PositiveOrZero Integer from,
            @RequestParam(required = false, defaultValue = SIZE) @Positive Integer size);

    @PostMapping("{userId}/events")
    ResponseEntity<EventFullDto> saveNewEventByUser(
            @PathVariable @Positive Integer userId,
            @Valid @RequestBody NewEventDto newEventDto);

    @GetMapping("{userId}/events/{eventId}")
    ResponseEntity<EventFullDto> getFullEventByUser(
            @PathVariable @Positive Integer userId,
            @PathVariable @Positive Integer eventId);

    @PatchMapping("{userId}/events/{eventId}")
    ResponseEntity<EventFullDto> updateEventByUser(
            @PathVariable @Positive Integer userId,
            @PathVariable @Positive Integer eventId,
            @Valid @RequestBody UpdateEventRequest updateEventRequest);

}
