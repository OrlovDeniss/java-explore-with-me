package ru.practicum.location.controller.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.location.dto.LocationDto;
import ru.practicum.location.service.LocationService;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/locations")
public class LocationPublicControllerImpl implements LocationPublicController {
    private final LocationService locationService;

    @Override
    public ResponseEntity<List<LocationDto>> getLocationsByPublic(Integer from, Integer size) {
        log.info("GET admin/locations from: {}, size: {}.", from, size);
        return ResponseEntity.ok().body(locationService.getLocations(from, size));
    }

    @Override
    public ResponseEntity<LocationDto> getLocationByPublic(Integer locId) {
        log.info("GET admin/locations/{}", locId);
        return ResponseEntity.ok().body(locationService.getLocation(locId));
    }
}
