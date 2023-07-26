package ru.practicum.compilation.controller.pub;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.compilation.dto.CompilationDto;

import javax.validation.constraints.Positive;
import java.util.List;

public interface CompilationPublicController {
    String FROM = "0";
    String SIZE = "10";

    @GetMapping
    ResponseEntity<List<CompilationDto>> getCompilationsByPublic(
            @RequestParam(required = false) boolean pinned,
            @RequestParam(required = false, defaultValue = FROM) Integer from,
            @RequestParam(required = false, defaultValue = SIZE) Integer size);

    @GetMapping("{compId}")
    ResponseEntity<CompilationDto> getCompilationByPublic(@PathVariable @Positive Integer compId);
}
