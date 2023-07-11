package ru.practicum.stats.service.finder;

import ru.practicum.stats.model.ViewStatsProjection;

import java.util.List;

public interface FinderStrategy {

    List<ViewStatsProjection> findAllViewStatsProjections();

}