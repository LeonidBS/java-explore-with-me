package ru.practicum.ewmservice.compilation.adm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmservice.compilation.dto.CompilationDto;
import ru.practicum.ewmservice.compilation.dto.CompilationMapper;
import ru.practicum.ewmservice.compilation.dto.NewCompilationDto;
import ru.practicum.ewmservice.compilation.dto.UpdateCompilationRequest;
import ru.practicum.ewmservice.compilation.model.Compilation;
import ru.practicum.ewmservice.compilation.repository.CompilationRepository;
import ru.practicum.ewmservice.event.repository.EventRepository;
import ru.practicum.ewmservice.exception.IdNotFoundException;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CompilationAdmServiceImpl implements CompilationAdmService {
    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;
    private final EventRepository eventRepository;

    @Override
    public CompilationDto saveCompilation(NewCompilationDto newCompilationDto) {

        Compilation compilation = Compilation.builder()
                .events(newCompilationDto.getEvents() != null ?
                        eventRepository.findByIdIn(newCompilationDto.getEvents()) : null)
                .pinned(newCompilationDto.getPinned() != null ?
                        newCompilationDto.getPinned() : false)
                .title(newCompilationDto.getTitle())
                .build();

        log.debug("Compilation has been created: {}", compilation);

        return compilationMapper.mapToDto(compilationRepository.save(compilation));
    }

    @Override
    public CompilationDto update(UpdateCompilationRequest updateCompilationRequest, Integer compId) {

        Compilation existedCompilation = compilationRepository.findById(compId)
                .orElseThrow(() -> {
                    log.error("Compilation with id={}} was not found", compId);
                    return new IdNotFoundException(String.format("Compilation with id=%d was not found", compId));
                });

        Compilation compilation = Compilation.builder()
                .id(existedCompilation.getId())
                .events(updateCompilationRequest.getEvents() != null ?
                        eventRepository.findByIdIn(updateCompilationRequest.getEvents()) :
                        existedCompilation.getEvents())
                .pinned(updateCompilationRequest.getPinned() != null ?
                        updateCompilationRequest.getPinned() : existedCompilation.getPinned())
                .title(updateCompilationRequest.getTitle() != null ?
                        updateCompilationRequest.getTitle() : existedCompilation.getTitle())
                .build();

        log.debug("Category has been updated: {}", compilation);
        return compilationMapper.mapToDto(compilationRepository.save(compilation));
    }

    @Override
    public void deleteById(Integer compId) {
        compilationRepository.deleteById(compId);
    }
}
