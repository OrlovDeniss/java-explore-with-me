package ru.practicum.stats.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.dto.EndpointHit;
import ru.practicum.stats.model.Stats;

@Mapper
public interface StatsMapper {

    StatsMapper INSTANCE = Mappers.getMapper(StatsMapper.class);

    Stats toEntity(EndpointHit endpointHit);

    EndpointHit toDto(Stats stats);

}