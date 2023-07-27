package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.dto.ViewStats;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventRequest;
import ru.practicum.event.dto.query.EventAdminQuery;
import ru.practicum.event.dto.query.EventPublicQuery;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;
import ru.practicum.util.exception.category.CategoryNotFoundException;
import ru.practicum.util.exception.event.EventCreateUpdateRulesException;
import ru.practicum.util.exception.event.EventNotFoundException;
import ru.practicum.util.exception.event.EventUpdateStateException;
import ru.practicum.util.exception.user.UserNotFoundException;
import ru.practicum.util.pagerequest.PageRequester;
import ru.practicum.util.statsclient.EwmStatsClient;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.practicum.event.enums.Sort.VIEWS;
import static ru.practicum.event.enums.State.*;

@Service
@Transactional
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private static final int CREATE_BEFORE_START_HOURS = 2;
    private static final int UPDATE_BEFORE_START_HOURS_BY_ADMIN = 1;
    private static final int UPDATE_BEFORE_START_HOURS_BY_USER = 2;

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final EwmStatsClient ewmStatsClient;

    @Override
    public List<EventShortDto> getShortEventsByUser(Integer userId, Integer from, Integer size) {
        return toShortDto(eventRepository.findAllByInitiatorId(userId, new PageRequester(from, size)).toList());
    }

    @Override
    public EventFullDto saveNewEventByUser(Integer userId, NewEventDto newEventDto) {
        throwWhenEventStartInFewHours(newEventDto.getEventDate(), CREATE_BEFORE_START_HOURS);
        var initiator = findInitiatorOrThrow(userId);
        var category = findCategoryOrThrow(newEventDto.getCategoryId());
        var newEvent = toEntity(newEventDto);
        newEvent.setInitiator(initiator);
        newEvent.setCategory(category);
        newEvent.setState(PENDING);
        newEvent.setCreatedOn(LocalDateTime.now());
        return toDto(eventRepository.save(newEvent));
    }

    @Override
    public EventFullDto getFullEventByUser(Integer userId, Integer eventId) {
        return toDto(findEventOrThrow(userId, eventId));
    }

    @Override
    public EventFullDto updateEventByUser(Integer userId,
                                          Integer eventId,
                                          UpdateEventRequest updateEvent) {
        throwWhenUpdateEventDateNotNullAndStartInFewHours(updateEvent, UPDATE_BEFORE_START_HOURS_BY_USER);
        throwWhenUserEventNotFound(userId, eventId);
        var event = findUserEventOrThrowWhenStatusPublished(userId, eventId);
        setEventStateByUserStateAction(updateEvent, event);
        update(updateEvent, event);
        return toDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto updateEventByAdmin(Integer eventId, UpdateEventRequest updateEvent) {
        throwWhenEventNotFound(eventId);
        throwWhenEventStartInFewHours(eventId, UPDATE_BEFORE_START_HOURS_BY_ADMIN);
        var event = findEventOrThrow(eventId);
        setEventStateByAdminStateAction(updateEvent, event);
        update(updateEvent, event);
        return toDto(eventRepository.save(event));
    }

    @Override
    public List<EventShortDto> getShortEventsByPublic(EventPublicQuery query,
                                                      Integer from,
                                                      Integer size) {
        List<Event> events = eventRepository
                .findEvents(
                        query.getText(),
                        query.getCategories(),
                        query.getLocations(),
                        query.getPaid(),
                        query.isOnlyAvailable(),
                        query.getRangeStart(),
                        query.getRangeEnd(),
                        new PageRequester(from, size, Sort.by("eventDate")))
                .toList();
        return toShortDto(sortByQuery(setViews(events), query.getSort()));
    }

    @Override
    public List<EventFullDto> getEventsByAdmin(EventAdminQuery query,
                                               Integer from,
                                               Integer size) {
        List<Event> events = eventRepository
                .findEvents(
                        query.getUsers(),
                        query.getStates(),
                        query.getCategories(),
                        query.getLocations(),
                        query.getRangeStart(),
                        query.getRangeEnd(),
                        new PageRequester(from, size))
                .toList();
        return toDto(setViews(events));
    }

    @Override
    public EventFullDto getEventByPublic(Integer eventId) {
        return toDto(setViews(findPublishedEventOrThrow(eventId)));
    }

    private List<Event> sortByQuery(List<Event> events,
                                    ru.practicum.event.enums.Sort sort) {
        if (sort == VIEWS) {
            events.sort((e1, e2) -> (int) (e2.getViews() - e1.getViews()));
        }
        return events;
    }

    private Event setViews(Event event) {
        return setViews(List.of(event)).get(0);
    }

    private List<Event> setViews(List<Event> events) {
        Map<Integer, Long> eventIdAndViews = viewStatsToMap(findViewStatsFromStatsServer(getEventUrls(events)));
        if (!eventIdAndViews.isEmpty()) {
            events.forEach(e ->
                    e.setViews(eventIdAndViews.getOrDefault(e.getId(), 0L)));
        }
        return events;
    }

    private List<String> getEventUrls(List<Event> events) {
        List<String> eventsUrls = new ArrayList<>();
        events.forEach(event -> eventsUrls.add("/events/" + event.getId()));
        return eventsUrls;
    }

    private List<ViewStats> findViewStatsFromStatsServer(List<String> eventsUrls) {
        return ewmStatsClient.getViesStats(
                LocalDateTime.now().minusYears(100),
                LocalDateTime.now(),
                eventsUrls,
                true);
    }

    private Map<Integer, Long> viewStatsToMap(List<ViewStats> viewStats) {
        return viewStats.stream()
                .collect(Collectors.toMap(
                        vs -> Integer.parseInt(vs.getUri().replace("/events/", "")),
                        ViewStats::getHits));
    }

    private void setEventStateByAdminStateAction(UpdateEventRequest updateEvent, Event event) {
        if (Objects.nonNull(updateEvent.getStateAction())) {
            switch (updateEvent.getStateAction()) {
                case PUBLISH_EVENT:
                    if (event.getState() == PENDING) {
                        event.setState(PUBLISHED);
                        event.setPublishedOn(LocalDateTime.now());
                    } else {
                        throw new EventUpdateStateException("Event can be published in only PENDING state");
                    }
                    break;
                case REJECT_EVENT:
                    if (event.getState() != PUBLISHED) {
                        event.setState(CANCELED);
                    } else {
                        throw new EventUpdateStateException("Event can be rejected if isn't PUBLISHED");
                    }
                    break;
                default:
                    throw new EventUpdateStateException("StateAction must be PUBLISH_EVENT or REJECT_EVENT");
            }
        }
    }

    private void setEventStateByUserStateAction(UpdateEventRequest updateEvent, Event event) {
        if (Objects.nonNull(updateEvent.getStateAction())) {
            switch (updateEvent.getStateAction()) {
                case SEND_TO_REVIEW:
                    event.setState(PENDING);
                    break;
                case CANCEL_REVIEW:
                    event.setState(CANCELED);
                    break;
                default:
                    throw new EventUpdateStateException("StateAction must be SEND_TO_REVIEW or CANCEL_REVIEW");
            }
        }
    }

    private Event findUserEventOrThrowWhenStatusPublished(Integer userId, Integer eventId) {
        return eventRepository
                .findByIdAndInitiator_IdAndStateNot(eventId, userId, PUBLISHED)
                .orElseThrow(() -> {
                    throw new EventCreateUpdateRulesException("Event must be not published");
                });
    }

    private Event findPublishedEventOrThrow(Integer eventId) {
        return eventRepository.findByIdAndState(eventId, PUBLISHED)
                .orElseThrow(() -> {
                    throw new EventNotFoundException(
                            String.format("Event id=%d was not found", eventId));
                });
    }

    private Category findCategoryOrThrow(Integer categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    throw new CategoryNotFoundException(
                            String.format("Category with id=%d was not found", categoryId));
                });
    }

    private User findInitiatorOrThrow(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    throw new UserNotFoundException(
                            String.format("User with id=%d was not found", userId));
                });
    }

    private void throwWhenEventStartInFewHours(LocalDateTime eventDate, int hours) {
        if (!eventDate.isAfter(LocalDateTime.now().plusHours(hours))) {
            throw new EventCreateUpdateRulesException(
                    String.format("EventDate must be %d hours later than current", hours));
        }
    }

    private void throwWhenEventStartInFewHours(int eventId, int hours) {
        if (!eventRepository.existsByIdAndEventDateAfter(eventId, LocalDateTime.now().plusHours(hours))) {
            throw new EventCreateUpdateRulesException(
                    String.format("EventDate must be %d hours later than current", hours));
        }
    }

    private void throwWhenUpdateEventDateNotNullAndStartInFewHours(UpdateEventRequest updateEvent,
                                                                   int updateBeforeStartHoursByUser) {
        if (Objects.nonNull(updateEvent.getEventDate())) {
            throwWhenEventStartInFewHours(updateEvent.getEventDate(), updateBeforeStartHoursByUser);
        }
    }

    private void throwWhenEventNotFound(Integer eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new EventNotFoundException(
                    String.format("Event with id=%d was not found", eventId)
            );
        }
    }

    private Event findEventOrThrow(Integer eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> {
                    throw new EventNotFoundException(
                            String.format("Event id=%d was not found", eventId));
                });
    }

    private Event findEventOrThrow(Integer initiatorId, Integer eventId) {
        return eventRepository.findByIdAndInitiator_Id(eventId, initiatorId)
                .orElseThrow(() -> {
                    throw new EventNotFoundException(
                            String.format("Event id=%d with initiator id=%d was not found", eventId, initiatorId));
                });
    }

    private void throwWhenUserEventNotFound(Integer userId, Integer eventId) {
        if (!eventRepository.existsByIdAndInitiator_Id(eventId, userId)) {
            throw new EventNotFoundException(
                    String.format("Event id=%d with initiator id=%d was not found", eventId, userId));
        }
    }

    private void update(UpdateEventRequest dto, Event entity) {
        EventMapper.INSTANCE.update(dto, entity);
    }

    private Event toEntity(NewEventDto newEventDto) {
        return EventMapper.INSTANCE.toEntity(newEventDto);
    }

    private EventFullDto toDto(Event event) {
        return EventMapper.INSTANCE.toDto(event);
    }

    private List<EventFullDto> toDto(List<Event> event) {
        return EventMapper.INSTANCE.toDto(event);
    }

    private List<EventShortDto> toShortDto(List<Event> events) {
        return EventMapper.INSTANCE.toShortDto(events);
    }

}
