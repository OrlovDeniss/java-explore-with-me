package ru.practicum.category.controller.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

public interface CategoryAdminController {

    @PostMapping
    ResponseEntity<CategoryDto> saveNewCategoryByAdmin(@Valid @RequestBody NewCategoryDto newCategoryDto);

    @PatchMapping("{catId}")
    ResponseEntity<CategoryDto> updateCategoryByAdmin(@PathVariable @Positive Integer catId,
                                                      @Valid @RequestBody CategoryDto categoryDto);

    @DeleteMapping("{catId}")
    ResponseEntity<Void> deleteCategoryByAdmin(@PathVariable @Positive Integer catId);

}