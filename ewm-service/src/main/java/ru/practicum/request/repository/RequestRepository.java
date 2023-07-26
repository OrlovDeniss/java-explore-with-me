package ru.practicum.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.request.model.Request;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Integer> {

    boolean existsByRequesterIdAndEventId(Integer requesterId, Integer eventId);

    List<Request> findAllByRequesterId(Integer requesterId);

    @Query("SELECT r FROM Request r " +
            "JOIN FETCH r.event " +
            "WHERE r.id = :requestId " +
            "  AND r.requester.id = :requesterId ")
    Optional<Request> findByIdAndRequesterIdWithEvent(Integer requestId, Integer requesterId);

    List<Request> findAllByEventId(Integer eventId);

}
