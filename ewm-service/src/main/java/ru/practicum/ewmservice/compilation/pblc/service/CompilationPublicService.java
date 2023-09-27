package ru.practicum.ewmservice.compilation.pblc.service;

import ru.practicum.ewmservice.compilation.dto.CompilationDto;

import java.util.List;

public interface CompilationPublicService {
    List<CompilationDto> findCompilation(Boolean pinned, Integer from, Integer size);

    CompilationDto findById(Integer compId);
}
