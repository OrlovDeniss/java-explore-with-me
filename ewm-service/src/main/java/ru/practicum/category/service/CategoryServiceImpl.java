package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.util.exception.category.CategoryNotFoundException;
import ru.practicum.util.pagerequest.PageRequester;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDto saveNewCategoryByAdmin(NewCategoryDto newCategoryDto) {
        return toDto(categoryRepository.save(toEntity(newCategoryDto)));
    }

    @Override
    public CategoryDto updateCategoryByAdmin(Integer catId, CategoryDto categoryDto) {
        throwWhenCategoryNotFound(catId);
        categoryDto.setId(catId);
        return toDto(categoryRepository.save(toEntity(categoryDto)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> getCategoriesByPublic(Integer from, Integer size) {
        return toDto(categoryRepository.findAll(new PageRequester(from, size)).toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto getCategoryByPublic(Integer catId) {
        return toDto(categoryRepository.findById(catId)
                .orElseThrow(() -> new CategoryNotFoundException(
                        String.format("Category with id=%d was not found", catId))));
    }

    @Override
    public void deleteCategoryByAdmin(Integer catId) {
        throwWhenCategoryNotFound(catId);
        categoryRepository.deleteById(catId);
    }

    private void throwWhenCategoryNotFound(Integer catId) {
        if (!categoryRepository.existsById(catId)) {
            throw new CategoryNotFoundException(
                    String.format("Category with id=%d was not found", catId));
        }
    }

    private Category toEntity(NewCategoryDto newCategoryDto) {
        return CategoryMapper.INSTANCE.toEntity(newCategoryDto);
    }

    private Category toEntity(CategoryDto categoryDto) {
        return CategoryMapper.INSTANCE.toEntity(categoryDto);
    }

    private CategoryDto toDto(Category category) {
        return CategoryMapper.INSTANCE.toDto(category);
    }

    private List<CategoryDto> toDto(List<Category> category) {
        return CategoryMapper.INSTANCE.toDto(category);
    }

}
