package ru.practicum.compilation.service;

import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationsDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {

    List<CompilationDto> getCompilationsByPublic(boolean pinned, Integer from, Integer size);

    CompilationDto getCompilationByPublic(Integer compId);

    CompilationDto saveNewCompilationByAdmin(NewCompilationsDto newCompilationsDto);

    CompilationDto updateCompilationByAdmin(Integer compId, UpdateCompilationRequest updateCompilationRequest);

    void deleteCompilationByAdmin(Integer compId);
}
