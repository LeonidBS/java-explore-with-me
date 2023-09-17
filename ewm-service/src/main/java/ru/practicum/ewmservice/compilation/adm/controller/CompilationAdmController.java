package ru.practicum.ewmservice.compilation.adm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.compilation.adm.repository.CompilationAdmRepository;
import ru.practicum.ewmservice.compilation.adm.service.CompilationAdmService;
import ru.practicum.ewmservice.compilation.dto.CompilationDto;
import ru.practicum.ewmservice.compilation.dto.NewCompilationDto;
import ru.practicum.ewmservice.compilation.dto.UpdateCompilationRequest;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(path = "/admin/compilations")
@RequiredArgsConstructor
@Validated
public class CompilationAdmController {
    CompilationAdmService compilationAdmService;

    @PostMapping
    public CompilationDto create(@RequestBody @Valid NewCompilationDto newCompilationDto) {

        return compilationAdmService.create(newCompilationDto);
    }

    @PatchMapping("/{compId}")
    public CompilationDto update(@RequestBody @Valid UpdateCompilationRequest updateCompilationRequest,
                                 @PathVariable Integer compId) {

        return compilationAdmService.update(updateCompilationRequest, compId);
    }

    @DeleteMapping("/{compId}")
    public void delete(@PathVariable Integer compId) {

        compilationAdmService.deleteById(compId);
    }
}
