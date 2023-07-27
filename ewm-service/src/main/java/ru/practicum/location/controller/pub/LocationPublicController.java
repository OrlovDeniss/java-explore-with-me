package ru.practicum.location.controller.pub;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.location.dto.LocationDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

public interface LocationPublicController {
    String FROM = "0";
    String SIZE = "10";

    @GetMapping
    ResponseEntity<List<LocationDto>> getLocationsByPublic(
            @RequestParam(required = false, defaultValue = FROM) @PositiveOrZero Integer from,
            @RequestParam(required = false, defaultValue = SIZE) @Positive Integer size);

    @GetMapping("{locId}")
    ResponseEntity<LocationDto> getLocationByPublic(@PathVariable @Positive Integer locId);
}
