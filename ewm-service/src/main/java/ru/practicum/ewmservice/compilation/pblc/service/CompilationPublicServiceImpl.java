package ru.practicum.ewmservice.compilation.pblc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmservice.compilation.dto.CompilationDto;
import ru.practicum.ewmservice.compilation.dto.CompilationMapper;
import ru.practicum.ewmservice.compilation.model.Compilation;
import ru.practicum.ewmservice.compilation.repository.CompilationRepository;
import ru.practicum.ewmservice.exception.IdNotFoundException;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CompilationPublicServiceImpl implements CompilationPublicService {
    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;

    @Override
    public List<CompilationDto> findByPinned(Boolean pinned) {

        return compilationMapper.mapListToDto(compilationRepository.findByPinned(pinned));
    }

    @Override
    public CompilationDto findById(Integer compId) {

        Compilation existedCompilation = compilationRepository.findById(compId)
                .orElseThrow(() -> {
                    log.error("Compilation with id={} was not found", compId);
                    return new IdNotFoundException(String.format(
                            "Compilation with id=%d was not found", compId));
                });

        return compilationMapper.mapToDto(existedCompilation);
    }
}
