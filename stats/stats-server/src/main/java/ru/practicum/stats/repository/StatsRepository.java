package ru.practicum.stats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.dto.ViewStats;
import ru.practicum.stats.model.Stats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<Stats, Integer> {

    @Query("SELECT new ru.practicum.dto.ViewStats(s.app, s.uri, count(s)) " +
            "FROM Stats s " +
            "WHERE s.timestamp BETWEEN :start AND :end " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY count(s) DESC ")
    List<ViewStats> findALlViewStatsByTimeRange(LocalDateTime start,
                                                LocalDateTime end);

    @Query("SELECT new ru.practicum.dto.ViewStats(s.app, s.uri, count(s)) " +
            "FROM Stats s " +
            "WHERE s.timestamp BETWEEN :start AND :end " +
            "AND s.uri IN (:uris) " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY count(s) DESC ")
    List<ViewStats> findALlViewStatsByTimeRange(LocalDateTime start,
                                                LocalDateTime end,
                                                List<String> uris);

    @Query("SELECT new ru.practicum.dto.ViewStats(s.app, s.uri, count(DISTINCT s.ip)) " +
            "FROM Stats s " +
            "WHERE s.timestamp BETWEEN :start AND :end " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY count(DISTINCT s.ip) DESC ")
    List<ViewStats> findALlUniqueViewStatsByTimeRange(LocalDateTime start,
                                                      LocalDateTime end);

    @Query("SELECT new ru.practicum.dto.ViewStats(s.app, s.uri, count(DISTINCT s.ip)) " +
            "FROM Stats s " +
            "WHERE s.timestamp BETWEEN :start AND :end " +
            "AND s.uri IN (:uris) " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY count(DISTINCT s.ip) DESC ")
    List<ViewStats> findALlUniqueViewStatsByTimeRange(LocalDateTime start,
                                                      LocalDateTime end,
                                                      List<String> uris);
}