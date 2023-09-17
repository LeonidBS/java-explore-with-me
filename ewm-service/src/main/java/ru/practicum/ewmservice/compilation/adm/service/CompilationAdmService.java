package ru.practicum.ewmservice.compilation.adm.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmservice.compilation.dto.CompilationDto;
import ru.practicum.ewmservice.compilation.dto.NewCompilationDto;
import ru.practicum.ewmservice.compilation.dto.UpdateCompilationRequest;

@Transactional(readOnly = true)
public interface CompilationAdmService {
    CompilationDto create(NewCompilationDto newCompilationDto);

    CompilationDto update(UpdateCompilationRequest updateCompilationRequest, Integer compId);

    void deleteById(Integer compId);
}
