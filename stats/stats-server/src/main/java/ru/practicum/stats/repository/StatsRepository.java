package ru.practicum.stats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.stats.model.Stats;
import ru.practicum.stats.model.ViewStatsProjection;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<Stats, Integer> {

    @Query(nativeQuery = true,
            value = "SELECT s.app, s.uri, count(*) as hits FROM stats s " +
                    "WHERE s.timestamp BETWEEN :start AND :end " +
                    "GROUP BY s.app, s.uri " +
                    "ORDER BY hits DESC ")
    List<ViewStatsProjection> findALlViewStatsByTimeRange(LocalDateTime start,
                                                          LocalDateTime end);

    @Query(nativeQuery = true,
            value = "SELECT s.app, s.uri, count(*) as hits FROM stats s " +
                    "WHERE s.timestamp BETWEEN :start AND :end " +
                    "AND uri IN (:uris) " +
                    "GROUP BY s.app, s.uri " +
                    "ORDER BY hits DESC ")
    List<ViewStatsProjection> findALlViewStatsByTimeRange(LocalDateTime start,
                                                          LocalDateTime end,
                                                          List<String> uris);

    @Query(nativeQuery = true,
            value = "SELECT s.app, s.uri, count(DISTINCT s.ip) as hits FROM stats s " +
                    "WHERE s.timestamp BETWEEN :start AND :end " +
                    "GROUP BY s.app, s.uri " +
                    "ORDER BY hits DESC ")
    List<ViewStatsProjection> findALlUniqueViewStatsByTimeRange(LocalDateTime start,
                                                                LocalDateTime end);

    @Query(nativeQuery = true,
            value = "SELECT s.app, s.uri, count(DISTINCT s.ip) as hits FROM stats s " +
                    "WHERE s.timestamp BETWEEN :start AND :end " +
                    "AND uri IN (:uris) " +
                    "GROUP BY s.app, s.uri " +
                    "ORDER BY hits DESC ")
    List<ViewStatsProjection> findALlUniqueViewStatsByTimeRange(LocalDateTime start,
                                                                LocalDateTime end,
                                                                List<String> uris);
}