package ru.practicum.stats.service.finder;

import ru.practicum.dto.ViewStats;

import java.util.List;

public interface FinderStrategy {

    List<ViewStats> findAllViewStatsProjections();

}