package ru.practicum.location.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.location.dto.LocationDto;
import ru.practicum.location.dto.NewLocationDto;
import ru.practicum.location.dto.UpdateLocationDto;
import ru.practicum.location.service.LocationService;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("admin/locations")
public class LocationAdminControllerImpl implements LocationAdminController {
    private final LocationService locationService;

    @Override
    public ResponseEntity<List<LocationDto>> getLocationsByAdmin(Integer from, Integer size) {
        log.info("GET admin/locations from: {}, size: {}.", from, size);
        return ResponseEntity.ok().body(locationService.getLocations(from, size));
    }

    @Override
    public ResponseEntity<LocationDto> getLocationByAdmin(Integer locId) {
        log.info("GET admin/locations/{}", locId);
        return ResponseEntity.ok().body(locationService.getLocation(locId));
    }

    @Override
    public ResponseEntity<LocationDto> saveNewLocationByAdmin(NewLocationDto newLocationDto) {
        log.info("POST admin/locations dto: {}.", newLocationDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(locationService.saveNewLocationByAdmin(newLocationDto));
    }

    @Override
    public ResponseEntity<LocationDto> updateLocationByAdmin(Integer locId, UpdateLocationDto updateLocationDto) {
        log.info("PUT admin/locations/{} dto: {}", locId, updateLocationDto);
        return ResponseEntity.ok().body(locationService.updateLocationByAdmin(locId, updateLocationDto));
    }

    @Override
    public ResponseEntity<Void> deleteLocationByAdmin(Integer locId) {
        log.info("DELETE admin/locations/{}", locId);
        locationService.deleteLocationByAdmin(locId);
        return ResponseEntity.noContent().build();
    }
}
