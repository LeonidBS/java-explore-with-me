package ru.practicum.ewmservice.compilation.adm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmservice.compilation.repository.CompilationRepository;
import ru.practicum.ewmservice.compilation.dto.CompilationDto;
import ru.practicum.ewmservice.compilation.dto.CompilationMapper;
import ru.practicum.ewmservice.compilation.dto.NewCompilationDto;
import ru.practicum.ewmservice.compilation.dto.UpdateCompilationRequest;
import ru.practicum.ewmservice.compilation.model.Compilation;
import ru.practicum.ewmservice.event.repository.EventRepository;
import ru.practicum.ewmservice.exception.IdNotFoundException;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CompilationAdmServiceImpl implements CompilationAdmService {
    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;
    private final EventRepository eventRepository;

    @Override
    public CompilationDto create(NewCompilationDto newCompilationDto) {

        Compilation compilation = Compilation.builder()
                .events(eventRepository.findByIdIn(newCompilationDto.getEvents()))
                .pinned(newCompilationDto.getPinned())
                .title(newCompilationDto.getTitle())
                .build();

        log.debug("Compilation has been created: {}", compilation);
        return compilationMapper.mapToDto(compilationRepository.save(compilation));
    }

    @Override
    public CompilationDto update(UpdateCompilationRequest updateCompilationRequest, Integer compId) {

        Compilation existedCompilation = compilationRepository.findById(compId)
                .orElseThrow(() -> {
                    log.error("Compilation with ID {} has not been found", compId);
                    return new IdNotFoundException("There is no Compilation with ID: " + compId);
                });

        Compilation compilation = Compilation.builder()
                .id(existedCompilation.getId())
                .events(eventRepository.findByIdIn(updateCompilationRequest.getEvents()))
                .pinned(updateCompilationRequest.getPinned())
                .title(updateCompilationRequest.getTitle())
                .build();

        log.debug("Category has been updated: {}", compilation);
        return compilationMapper.mapToDto(compilationRepository.save(compilation));
    }

    @Override
    public void deleteById(Integer compId) {

    }
}
