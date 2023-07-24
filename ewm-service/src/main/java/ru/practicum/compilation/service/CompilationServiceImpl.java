package ru.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationsDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.compilation.mapper.CompilationMapper;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.reposiotry.CompilationRepository;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.util.exception.compilation.CompilationNotFoundException;
import ru.practicum.util.pagerequest.PageRequester;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    public List<CompilationDto> getCompilationsByPublic(boolean pinned, Integer from, Integer size) {
        return toDto(compilationRepository.findAllByPinned(pinned, new PageRequester(from, size)).toList());
    }

    @Override
    public CompilationDto getCompilationByPublic(Integer compId) {
        return toDto(findCompilationByIdOrThrow(compId));
    }

    @Override
    public CompilationDto saveNewCompilationByAdmin(NewCompilationsDto dto) {
        return toDto(compilationRepository.save(Compilation.builder()
                .events(eventRepository.findEventsByIdIn(dto.getEvents()))
                .pinned(dto.isPinned())
                .title(dto.getTitle())
                .build()));
    }

    @Override
    public CompilationDto updateCompilationByAdmin(Integer compId, UpdateCompilationRequest dto) {
        var comp = findCompilationOrThrow(compId);
        if (Objects.nonNull(dto.getEventsIds())) {
            comp.setEvents(eventRepository.findAllById(dto.getEventsIds()));
        }
        update(dto, comp);
        return toDto(compilationRepository.save(comp));
    }

    @Override
    public void deleteCompilationByAdmin(Integer compId) {
        compilationRepository.deleteById(compId);
    }

    private Compilation findCompilationOrThrow(Integer compId) {
        return compilationRepository.findById(compId)
                .orElseThrow(() -> {
                    throw new CompilationNotFoundException(
                            String.format("Compilation id=%d was not found", compId));
                });
    }

    private Compilation findCompilationByIdOrThrow(Integer compId) {
        return findCompilationOrThrow(compId);
    }

    private void update(UpdateCompilationRequest dto, Compilation compilation) {
        CompilationMapper.INSTANCE.update(dto, compilation);
    }

    private CompilationDto toDto(Compilation compilation) {
        return CompilationMapper.INSTANCE.toDto(compilation);
    }

    private List<CompilationDto> toDto(List<Compilation> compilation) {
        return CompilationMapper.INSTANCE.toDto(compilation);
    }
}
