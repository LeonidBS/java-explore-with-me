package ru.practicum.ewmservice.compilation.adm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.compilation.dto.CompilationDto;
import ru.practicum.ewmservice.compilation.dto.NewCompilationDto;
import ru.practicum.ewmservice.compilation.dto.UpdateCompilationRequest;
import ru.practicum.ewmservice.compilation.adm.service.CompilationAdmService;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@RestController
@RequestMapping(path = "/admin/compilations")
@RequiredArgsConstructor
@Validated
public class CompilationAdmController {
    private final CompilationAdmService compilationAdmService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto create(@Valid @RequestBody NewCompilationDto newCompilationDto) {

        return compilationAdmService.saveCompilation(newCompilationDto);
    }

    @PatchMapping("/{compId}")
    public CompilationDto update(@Valid @RequestBody(required = false) UpdateCompilationRequest updateCompilationRequest,
                                 @PositiveOrZero @PathVariable Integer compId) {

        return compilationAdmService.update(updateCompilationRequest, compId);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer compId) {

        compilationAdmService.deleteById(compId);
    }
}
