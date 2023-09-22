package ru.practicum.ewmservice.category.adm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmservice.category.dto.CategoryDto;
import ru.practicum.ewmservice.category.dto.CategoryMapper;
import ru.practicum.ewmservice.category.dto.NewCategoryDto;
import ru.practicum.ewmservice.category.model.Category;
import ru.practicum.ewmservice.category.repository.CategoryRepository;
import ru.practicum.ewmservice.event.repository.EventRepository;
import ru.practicum.ewmservice.exception.AccessDeniedException;
import ru.practicum.ewmservice.exception.IdNotFoundException;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryAdmServiceImpl implements CategoryAdmService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public CategoryDto create(NewCategoryDto newCategoryDto) {

        Category category = Category.builder()
                .name(newCategoryDto.getName())
                .build();

        log.debug("Category has been created: {}", category);
        return categoryMapper.mapToDto(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public CategoryDto update(CategoryDto categoryDto, Integer catId) {

        Category existedCategory = categoryRepository.findById(catId)
                .orElseThrow(() -> {
                    log.error("Category with ID {} has not been found", catId);
                    return new IdNotFoundException("There is no Category with ID: " + catId);
                });

        Category category = Category.builder()
                .id(existedCategory.getId())
                .name(categoryDto.getName())
                .build();

        log.debug("Category has been updated: {}", category);
        return categoryMapper.mapToDto(categoryRepository.save(category));
    }


    @Override
    @Transactional
    public void deleteById(Integer catId) {

        if (categoryRepository.findById(catId).isEmpty()) {
            log.error("Category with ID {} has not been found", catId);
            throw new IdNotFoundException(String.format(
                    "Category with ID %d has not been found", catId));
        }

        if (!eventRepository.findByCategoryId(catId).isEmpty()) {
            log.error("Category with ID {} has used. It cannot be deleted", catId);
            throw new AccessDeniedException(String.format(
                    "Category with ID %s has used. It cannot be deleted", catId));
        }

        categoryRepository.deleteById(catId);
    }
}
