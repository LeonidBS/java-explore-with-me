package ru.practicum.ewmservice.category.pblc.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmservice.category.dto.CategoryDto;

import java.util.List;

@Transactional(readOnly = true)
public interface CategoryPublicService {
    List<CategoryDto> findAll(Integer from, Integer size);

    CategoryDto findById(Integer catId);
}
