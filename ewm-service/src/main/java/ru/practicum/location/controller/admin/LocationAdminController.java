package ru.practicum.location.controller.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.location.dto.LocationDto;
import ru.practicum.location.dto.NewLocationDto;
import ru.practicum.location.dto.UpdateLocationDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

public interface LocationAdminController {
    String FROM = "0";
    String SIZE = "10";

    @GetMapping
    ResponseEntity<List<LocationDto>> getLocationsByAdmin(
            @RequestParam(required = false, defaultValue = FROM) @PositiveOrZero Integer from,
            @RequestParam(required = false, defaultValue = SIZE) @Positive Integer size);

    @GetMapping("{locId}")
    ResponseEntity<LocationDto> getLocationByAdmin(@PathVariable @Positive Integer locId);

    @PostMapping
    ResponseEntity<LocationDto> saveNewLocationByAdmin(@Valid @RequestBody NewLocationDto newLocationDto);

    @PutMapping("{locId}")
    ResponseEntity<LocationDto> updateLocationByAdmin(@PathVariable @Positive Integer locId,
                                                      @Valid @RequestBody UpdateLocationDto updateLocationDto);

    @DeleteMapping("{locId}")
    ResponseEntity<Void> deleteLocationByAdmin(@PathVariable @Positive Integer locId);

}
