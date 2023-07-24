package ru.practicum.compilation.controller.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.service.CompilationService;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/compilations")
public class CompilationPublicControllerImpl implements CompilationPublicController {
    private final CompilationService compilationService;

    @Override
    public ResponseEntity<List<CompilationDto>> getCompilationsByPublic(boolean pinned, Integer from, Integer size) {
        log.info("GET /compilations pinned: {}, from: {}, size: {}.", pinned, from, size);
        return ResponseEntity.ok().body(compilationService.getCompilationsByPublic(pinned, from, size));
    }

    @Override
    public ResponseEntity<CompilationDto> getCompilationByPublic(Integer compId) {
        log.info("GET /compilations/{}.", compId);
        return ResponseEntity.ok().body(compilationService.getCompilationByPublic(compId));
    }
}
