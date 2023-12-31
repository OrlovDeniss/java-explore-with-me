package ru.practicum.stats.service.finder;

import ru.practicum.dto.ViewStats;
import ru.practicum.stats.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

public class FindAllViewStatsByTimeRange implements FinderStrategy {
    private final LocalDateTime start;
    private final LocalDateTime end;
    private final List<String> uris;
    private final StatsRepository statsRepository;

    public FindAllViewStatsByTimeRange(LocalDateTime start,
                                       LocalDateTime end,
                                       List<String> uris,
                                       StatsRepository statsRepository) {
        this.start = start;
        this.end = end;
        this.uris = uris;
        this.statsRepository = statsRepository;
    }

    @Override
    public List<ViewStats> findAllViewStats() {
        return statsRepository.findALlViewStatsByTimeRange(start, end, uris);
    }

}
