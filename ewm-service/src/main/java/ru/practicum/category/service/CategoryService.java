package ru.practicum.category.service;

import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto saveNewCategoryByAdmin(NewCategoryDto newCategoryDto);

    CategoryDto updateCategoryByAdmin(Integer catId, CategoryDto categoryDto);

    List<CategoryDto> getCategoriesByPublic(Integer from, Integer size);

    CategoryDto getCategoryByPublic(Integer catId);

    void deleteCategoryByAdmin(Integer catId);

}