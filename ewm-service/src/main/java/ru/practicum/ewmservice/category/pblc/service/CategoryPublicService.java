package ru.practicum.ewmservice.category.pblc.service;

import ru.practicum.ewmservice.category.dto.CategoryDto;

import java.util.List;

public interface CategoryPublicService {
    List<CategoryDto> findAll(Integer from, Integer size);

    CategoryDto findById(Integer catId);
}
