package ru.practicum.stats.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.dto.EndpointHit;
import ru.practicum.dto.ViewStats;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

public interface StatsController {

    @PostMapping("/hit")
    ResponseEntity<EndpointHit> post(@Valid @RequestBody EndpointHit endpointHit);

    @GetMapping("/stats")
    ResponseEntity<List<ViewStats>> get(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                        @RequestParam(required = false, defaultValue = "") List<String> uris,
                                        @RequestParam(required = false, defaultValue = "false") boolean unique);

}