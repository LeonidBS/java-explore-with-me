package ru.practicum.ewmservice.category.adm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.category.adm.service.CategoryAdmService;
import ru.practicum.ewmservice.category.dto.CategoryDto;
import ru.practicum.ewmservice.category.dto.NewCategoryDto;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(path = "/admin/categories")
@RequiredArgsConstructor
@Validated
public class CategoryAdmController {

    private final CategoryAdmService categoryAdmService;

    @PostMapping
    public CategoryDto create(@RequestBody @Valid NewCategoryDto newCategoryDto) {

        return categoryAdmService.create(newCategoryDto);
    }

    @PatchMapping("/{catId}")
    public CategoryDto update(@RequestBody @Valid CategoryDto categoryDto, @PathVariable Integer catId) {

        return categoryAdmService.update(categoryDto, catId);
    }

    @DeleteMapping("/{catId}")
    public void delete(@PathVariable Integer catId) {

        categoryAdmService.deleteById(catId);
    }
}
