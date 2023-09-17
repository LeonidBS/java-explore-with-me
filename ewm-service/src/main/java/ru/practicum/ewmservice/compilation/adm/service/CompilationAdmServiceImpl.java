package ru.practicum.ewmservice.compilation.adm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmservice.compilation.adm.repository.CompilationAdmRepository;
import ru.practicum.ewmservice.compilation.dto.CompilationDto;
import ru.practicum.ewmservice.compilation.dto.CompilationMapper;
import ru.practicum.ewmservice.compilation.dto.NewCompilationDto;
import ru.practicum.ewmservice.compilation.dto.UpdateCompilationRequest;
import ru.practicum.ewmservice.compilation.model.Compilation;
import ru.practicum.ewmservice.event.adm.repository.EventAdmRepository;
import ru.practicum.ewmservice.exception.IdNotFoundException;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CompilationAdmServiceImpl implements CompilationAdmService {
    private final CompilationAdmRepository compilationAdmRepository;
    private final CompilationMapper compilationMapper;
    private final EventAdmRepository eventAdmRepository;

    @Override
    public CompilationDto create(NewCompilationDto newCompilationDto) {

        Compilation compilation = Compilation.builder()
                .events(eventAdmRepository.findByIdIn(newCompilationDto.getEvents()))
                .pinned(newCompilationDto.getPinned())
                .title(newCompilationDto.getTitle())
                .build();

        log.debug("Compilation has been created: {}", compilation);
        return compilationMapper.mapToDto(compilationAdmRepository.save(compilation));
    }

    @Override
    public CompilationDto update(UpdateCompilationRequest updateCompilationRequest, Integer compId) {

        Compilation existedCompilation = compilationAdmRepository.findById(compId)
                .orElseThrow(() -> {
                    log.error("Compilation with ID {} has not been found", compId);
                    return new IdNotFoundException("There is no Compilation with ID: " + compId);
                });

        Compilation compilation = Compilation.builder()
                .id(existedCompilation.getId())
                .events(eventAdmRepository.findByIdIn(updateCompilationRequest.getEvents()))
                .pinned(updateCompilationRequest.getPinned())
                .title(updateCompilationRequest.getTitle())
                .build();

        log.debug("Category has been updated: {}", compilation);
        return compilationMapper.mapToDto(compilationAdmRepository.save(compilation));
    }

    @Override
    public void deleteById(Integer compId) {

    }
}
