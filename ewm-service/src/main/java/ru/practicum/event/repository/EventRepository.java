package ru.practicum.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.event.enums.State;
import ru.practicum.event.model.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Integer> {

    Page<Event> findAllByInitiatorId(int userId, Pageable pageable);

    Optional<Event> findByIdAndInitiator_Id(int eventId, int userId);

    boolean existsByIdAndInitiator_Id(int eventId, int userId);

    boolean existsByIdAndEventDateAfter(Integer id, LocalDateTime eventDate);

    @Query("SELECT e " +
            "FROM Event e " +
            "WHERE (e.state = 'PUBLISHED') " +
            "  AND (:text IS NULL " +
            "    OR lower(e.description) like lower(concat('%', :text, '%')) " +
            "    OR lower(e.annotation) like lower(concat('%', :text, '%'))) " +
            "  AND ((:categoryIds) IS NULL OR e.category.id IN (:categoryIds)) " +
            "  AND (:paid IS NULL OR e.paid = :paid) " +
            "  AND ((:onlyAvailable IS TRUE AND (e.participantLimit > e.confirmedRequests OR e.participantLimit = 0)) " +
            "    OR :onlyAvailable IS FALSE) " +
            "  AND e.eventDate BETWEEN :start AND :end " +
            "  AND ((:locIds) IS NULL " +
            "    OR EXISTS(select loc.id from Location loc" +
            "      where loc.id IN (:locIds) " +
            "        AND function('distance', e.location.lon, e.location.lat, loc.lon, loc.lat) <= loc.radius)) ")
    Page<Event> findEvents(String text,
                           Set<Integer> categoryIds,
                           Set<Integer> locIds,
                           Boolean paid,
                           boolean onlyAvailable,
                           LocalDateTime start,
                           LocalDateTime end,
                           Pageable pageable);

    @Query("SELECT e " +
            "FROM Event e " +
            "WHERE ((:users) IS NULL OR e.initiator.id IN (:users)) " +
            "  AND ((:states) IS NULL OR e.state IN (:states)) " +
            "  AND ((:categories) IS NULL OR e.category.id IN (:categories)) " +
            "  AND e.eventDate BETWEEN :start AND :end " +
            "  AND ((:locIds) IS NULL OR " +
            "    EXISTS(select loc.id from Location loc " +
            "      where loc.id IN (:locIds) " +
            "        AND function('distance', e.location.lon, e.location.lat, loc.lon, loc.lat) <= loc.radius)) ")
    Page<Event> findEvents(Set<Integer> users,
                           Set<State> states,
                           Set<Integer> categories,
                           Set<Integer> locIds,
                           LocalDateTime start,
                           LocalDateTime end,
                           Pageable pageable);

    Optional<Event> findByIdAndInitiator_IdAndStateNot(Integer eventId,
                                                       Integer initiatorId,
                                                       State state);

    List<Event> findEventsByIdIn(List<Integer> ids);

    Optional<Event> findByIdAndState(Integer eventId, State state);

    boolean existsByIdAndState(Integer eventId, State state);

    @Query("SELECT count(e) > 0 " +
            "FROM Event e " +
            "WHERE e.id = :eventId " +
            "  AND (e.participantLimit = 0 OR e.participantLimit > e.confirmedRequests) ")
    boolean existsByIdAndParticipantLimitGreaterThenConfirmedRequestsOrZero(Integer eventId);

    @Query("SELECT count(e) > 0 " +
            "FROM Event e " +
            "WHERE e.id = :eventId " +
            "  AND (e.participantLimit = 0 OR e.requestModeration = false) ")
    boolean existsByIdAndParticipantLimitIsZeroOrRequestModerationFalse(Integer eventId);

}
