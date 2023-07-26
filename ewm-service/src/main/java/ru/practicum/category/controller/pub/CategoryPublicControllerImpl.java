package ru.practicum.category.controller.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.service.CategoryService;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryPublicControllerImpl implements CategoryPublicController {
    private final CategoryService categoryService;

    @Override
    public ResponseEntity<List<CategoryDto>> getCategoriesByPublic(Integer from, Integer size) {
        log.info("POST categories from: {}, size: {}.", from, size);
        return ResponseEntity.ok().body(categoryService.getCategoriesByPublic(from, size));
    }

    @Override
    public ResponseEntity<CategoryDto> getCategoryByPublic(Integer catId) {
        log.info("POST categories/{}", catId);
        return ResponseEntity.ok().body(categoryService.getCategoryByPublic(catId));
    }
}
