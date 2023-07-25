package ru.practicum.category.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.service.CategoryService;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("admin/categories")
public class CategoryAdminControllerImpl implements CategoryAdminController {
    private final CategoryService categoryService;

    @Override
    public ResponseEntity<CategoryDto> saveNewCategoryByAdmin(NewCategoryDto newCategoryDto) {
        log.info("POST admin/categories dto: {}.", newCategoryDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(categoryService.saveNewCategoryByAdmin(newCategoryDto));
    }

    @Override
    public ResponseEntity<CategoryDto> updateCategoryByAdmin(Integer catId, CategoryDto categoryDto) {
        log.info("PATCH admin/categories/{} dto: {}.", catId, categoryDto);
        return ResponseEntity.ok().body(categoryService.updateCategoryByAdmin(catId, categoryDto));
    }

    @Override
    public ResponseEntity<Void> deleteCategoryByAdmin(Integer catId) {
        log.info("DELETE admin/categories/{}.", catId);
        categoryService.deleteCategoryByAdmin(catId);
        return ResponseEntity.noContent().build();
    }
}
