package ru.practicum.stats.service;

import ru.practicum.dto.EndpointHit;
import ru.practicum.dto.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {

    EndpointHit saveHit(EndpointHit endpointHit);

    List<ViewStats> getViewStats(LocalDateTime start,
                                 LocalDateTime end,
                                 List<String> uris,
                                 boolean unique);

}
