package ru.practicum.location.service;

import ru.practicum.location.dto.LocationDto;
import ru.practicum.location.dto.NewLocationDto;
import ru.practicum.location.dto.UpdateLocationDto;

import java.util.List;

public interface LocationService {

    List<LocationDto> getLocations(Integer from, Integer size);

    LocationDto getLocation(Integer locId);

    LocationDto saveNewLocationByAdmin(NewLocationDto newLocationDto);

    LocationDto updateLocationByAdmin(Integer locId, UpdateLocationDto updateLocationDto);

    void deleteLocationByAdmin(Integer locId);
}
