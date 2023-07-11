package ru.practicum.stats.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.dto.EndpointHit;
import ru.practicum.dto.ViewStats;
import ru.practicum.stats.model.Stats;
import ru.practicum.stats.model.ViewStatsProjection;

import java.util.List;

@Mapper
public interface StatsMapper {

    StatsMapper INSTANCE = Mappers.getMapper(StatsMapper.class);

    Stats toEntity(EndpointHit endpointHit);

    List<ViewStats> toDto(List<ViewStatsProjection> viewStatsProjections);

    EndpointHit toDto(Stats stats);

}