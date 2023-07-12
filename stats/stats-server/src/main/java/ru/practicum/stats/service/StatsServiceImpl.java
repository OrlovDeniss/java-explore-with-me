package ru.practicum.stats.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EndpointHit;
import ru.practicum.dto.ViewStats;
import ru.practicum.stats.mapper.StatsMapper;
import ru.practicum.stats.model.Stats;
import ru.practicum.stats.repository.StatsRepository;
import ru.practicum.stats.service.finder.FindAllUniqueViewStatsByTimeRange;
import ru.practicum.stats.service.finder.FindAllViewStatsByTimeRange;
import ru.practicum.stats.service.finder.FinderStrategy;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;

    public EndpointHit saveHit(EndpointHit endpointHit) {
        return toDto(statsRepository.save(toEntity(endpointHit)));
    }

    public List<ViewStats> getViewStats(LocalDateTime start,
                                        LocalDateTime end,
                                        List<String> uris,
                                        boolean unique) {
        FinderStrategy finderStrategy;
        if (unique) {
            finderStrategy = new FindAllUniqueViewStatsByTimeRange(start, end, uris, statsRepository);
        } else {
            finderStrategy = new FindAllViewStatsByTimeRange(start, end, uris, statsRepository);
        }
        return finderStrategy.findAllViewStatsProjections();
    }

    private Stats toEntity(EndpointHit endpointHit) {
        return StatsMapper.INSTANCE.toEntity(endpointHit);
    }

    private EndpointHit toDto(Stats stats) {
        return StatsMapper.INSTANCE.toDto(stats);
    }

}