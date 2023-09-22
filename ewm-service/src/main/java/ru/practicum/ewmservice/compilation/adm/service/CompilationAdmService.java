package ru.practicum.ewmservice.compilation.adm.service;

import ru.practicum.ewmservice.compilation.dto.CompilationDto;
import ru.practicum.ewmservice.compilation.dto.NewCompilationDto;
import ru.practicum.ewmservice.compilation.dto.UpdateCompilationRequest;

public interface CompilationAdmService {
    CompilationDto saveCompilation(NewCompilationDto newCompilationDto);

    CompilationDto update(UpdateCompilationRequest updateCompilationRequest, Integer compId);

    void deleteById(Integer compId);
}
