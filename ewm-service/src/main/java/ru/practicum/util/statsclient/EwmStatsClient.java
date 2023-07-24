package ru.practicum.util.statsclient;

import ru.practicum.dto.ViewStats;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EwmStatsClient {

    void saveEndpointHit(HttpServletRequest request);

    List<ViewStats> getViesStats(LocalDateTime start,
                                 LocalDateTime end,
                                 List<String> uris,
                                 boolean unique);
}
