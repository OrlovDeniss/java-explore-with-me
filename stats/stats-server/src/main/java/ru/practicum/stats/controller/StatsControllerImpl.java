package ru.practicum.stats.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.EndpointHit;
import ru.practicum.dto.ViewStats;
import ru.practicum.stats.service.StatsServiceImpl;
import ru.practicum.util.exception.StartAfterEndException;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class StatsControllerImpl implements StatsController {
    private final StatsServiceImpl statsService;

    @Override
    public ResponseEntity<EndpointHit> post(EndpointHit endpointHit) {
        log.info("POST /hit with dto: {}.", endpointHit);
        return new ResponseEntity<>(statsService.saveHit(endpointHit), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<ViewStats>> get(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        throwWhenStartAfterEnd(start, end);
        log.info("GET /stats with params: {}, {}, {}, {}", start, end, uris, unique);
        return new ResponseEntity<>(statsService.getViewStats(start, end, uris, unique), HttpStatus.OK);
    }

    private void throwWhenStartAfterEnd(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end)) {
            throw new StartAfterEndException();
        }
    }

}