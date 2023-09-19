package ru.practicum.ewmservice.category.pblc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.category.dto.CategoryDto;
import ru.practicum.ewmservice.category.pblc.service.CategoryPublicService;

import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/categories")
@RequiredArgsConstructor
@Validated
public class CategoryPublicController {
    private final CategoryPublicService categoryPublicService;

    @GetMapping
    public List<CategoryDto> getAll(@RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                    @RequestParam(defaultValue = "10") @PositiveOrZero Integer size) {

        return categoryPublicService.findAll(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryDto getById(@PositiveOrZero @PathVariable Integer catId) {

        return categoryPublicService.findById(catId);
    }
}
