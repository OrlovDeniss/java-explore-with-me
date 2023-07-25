package ru.practicum.compilation.controller.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationsDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

public interface CompilationAdminController {

    @PostMapping
    ResponseEntity<CompilationDto> saveNewCompilationsByAdmin(@Valid @RequestBody NewCompilationsDto newCompilationsDto);

    @PatchMapping("{compId}")
    ResponseEntity<CompilationDto> updateCompilationByAdmin(@PathVariable @Positive Integer compId,
                                                            @Valid @RequestBody UpdateCompilationRequest dto);

    @DeleteMapping("{compId}")
    ResponseEntity<Void> deleteCompilationByAdmin(@PathVariable @Positive Integer compId);

}
