package ru.practicum.event.service;

import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventRequest;
import ru.practicum.event.dto.query.EventAdminQuery;
import ru.practicum.event.dto.query.EventPublicQuery;

import java.util.List;

public interface EventService {

    List<EventShortDto> getShortEventsByUser(Integer userId,
                                             Integer from,
                                             Integer size);

    EventFullDto saveNewEventByUser(Integer userId,
                                    NewEventDto newEventDto);

    EventFullDto getFullEventByUser(Integer userId,
                                    Integer eventId);

    EventFullDto updateEventByUser(Integer userId,
                                   Integer eventId,
                                   UpdateEventRequest updateEventRequest);

    EventFullDto updateEventByAdmin(Integer eventId,
                                    UpdateEventRequest updateEventRequest);

    List<EventShortDto> getShortEventsByPublic(EventPublicQuery eventPublicQuery,
                                               Integer from,
                                               Integer size);

    List<EventFullDto> getEventsByAdmin(EventAdminQuery eventAdminQuery,
                                        Integer from,
                                        Integer size);

    EventFullDto getEventByPublic(Integer eventId);

}
