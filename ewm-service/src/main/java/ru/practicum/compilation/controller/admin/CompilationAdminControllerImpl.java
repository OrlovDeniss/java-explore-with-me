package ru.practicum.compilation.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationsDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.compilation.service.CompilationService;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/compilations")
public class CompilationAdminControllerImpl implements CompilationAdminController {
    private final CompilationService compilationService;

    @Override
    public ResponseEntity<CompilationDto> saveNewCompilationsByAdmin(NewCompilationsDto newCompilationsDto) {
        log.info("GET admin/compilations dto: {}.", newCompilationsDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(compilationService.saveNewCompilationByAdmin(newCompilationsDto));
    }

    @Override
    public ResponseEntity<CompilationDto> updateCompilationByAdmin(Integer compId,
                                                                   UpdateCompilationRequest dto) {
        log.info("PATCH admin/compilations dto: {}.", dto);
        return ResponseEntity.ok().body(compilationService.updateCompilationByAdmin(compId, dto));
    }

    @Override
    public ResponseEntity<Object> deleteCompilationByAdmin(Integer compId) {
        log.info("DELETE admin/compilations/{}.", compId);
        compilationService.deleteCompilationByAdmin(compId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
