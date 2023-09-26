package ru.practicum.ewmservice.category.adm.service;

import ru.practicum.ewmservice.category.dto.CategoryDto;
import ru.practicum.ewmservice.category.dto.NewCategoryDto;

public interface CategoryAdmService {
    CategoryDto create(NewCategoryDto newCategoryDto);

    CategoryDto update(CategoryDto categoryDto, Integer catId);

    void deleteById(Integer catId);
}
