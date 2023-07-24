package ru.practicum.category.controller.pub;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.category.dto.CategoryDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

public interface CategoryPublicController {
    String FROM = "0";
    String SIZE = "10";

    @GetMapping
    ResponseEntity<List<CategoryDto>> getCategoriesByPublic(
            @RequestParam(required = false, defaultValue = FROM) @PositiveOrZero Integer from,
            @RequestParam(required = false, defaultValue = SIZE) @Positive Integer size);

    @GetMapping("{catId}")
    ResponseEntity<CategoryDto> getCategoryByPublic(@PathVariable @Positive Integer catId);

}