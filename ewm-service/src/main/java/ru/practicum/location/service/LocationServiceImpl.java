package ru.practicum.location.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.location.dto.LocationDto;
import ru.practicum.location.dto.NewLocationDto;
import ru.practicum.location.dto.UpdateLocationDto;
import ru.practicum.location.mapper.LocationMapper;
import ru.practicum.location.model.Location;
import ru.practicum.location.repository.LocationRepository;
import ru.practicum.util.exception.location.LocationCoordinatesAlreadyExistException;
import ru.practicum.util.exception.location.LocationNotFoundException;
import ru.practicum.util.pagerequest.PageRequester;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;

    @Override
    public List<LocationDto> getLocations(Integer from, Integer size) {
        return toDto(locationRepository.findAll(new PageRequester(from, size, Sort.by("id"))).toList());
    }

    @Override
    public LocationDto getLocation(Integer locId) {
        return toDto(findLocationOrThrow(locId));
    }

    @Override
    public LocationDto saveNewLocationByAdmin(NewLocationDto newLocationDto) {
        throwWhenCoordinatesAlreadyExist(newLocationDto.getLat(), newLocationDto.getLon());
        return toDto(locationRepository.save(toEntity(newLocationDto)));
    }

    @Override
    public LocationDto updateLocationByAdmin(Integer locId, UpdateLocationDto updateLocationDto) {
        var location = findLocationOrThrow(locId);
        update(updateLocationDto, location);
        return toDto(locationRepository.save(location));
    }

    @Override
    public void deleteLocationByAdmin(Integer locId) {
        locationRepository.deleteById(locId);
    }

    private void throwWhenCoordinatesAlreadyExist(Double lat, Double lon) {
        if (locationRepository.existsByLatAndLon(lat, lon)) {
            throw new LocationCoordinatesAlreadyExistException(
                    String.format("Location with lat=%2.6f, lon=%2.6f already exists", lat, lon)
            );
        }
    }

    private Location findLocationOrThrow(int locId) {
        return locationRepository.findById(locId).orElseThrow(() -> {
            throw new LocationNotFoundException(
                    String.format("Location with id=%d was not found", locId));
        });
    }

    private Location toEntity(NewLocationDto newLocationDto) {
        return LocationMapper.INSTANCE.toEntity(newLocationDto);
    }

    private void update(UpdateLocationDto updateLocationDto, Location location) {
        LocationMapper.INSTANCE.update(updateLocationDto, location);
    }

    private LocationDto toDto(Location location) {
        return LocationMapper.INSTANCE.toDto(location);
    }

    private List<LocationDto> toDto(List<Location> locations) {
        return LocationMapper.INSTANCE.toDto(locations);
    }
}
