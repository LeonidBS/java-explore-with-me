package ru.practicum.ewmservice.participation.prvt.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmservice.compilation.dto.CompilationDto;
import ru.practicum.ewmservice.participation.dto.ParticipationDto;

import java.util.List;

@Transactional(readOnly = true)
public interface ParticipationPrivateService {
    List<ParticipationDto> findByRequesterId(Integer userId);

    ParticipationDto create(Integer userId, Integer eventId);

    ParticipationDto update(Integer userId, Integer requestId);
}
