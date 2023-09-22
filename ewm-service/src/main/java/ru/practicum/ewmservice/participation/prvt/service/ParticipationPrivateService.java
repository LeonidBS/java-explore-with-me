package ru.practicum.ewmservice.participation.prvt.service;

import ru.practicum.ewmservice.participation.dto.ParticipationDto;

import java.util.List;

public interface ParticipationPrivateService {
    List<ParticipationDto> findByRequesterId(Integer userId);

    ParticipationDto saveParticipation(Integer userId, Integer eventId);

    ParticipationDto update(Integer userId, Integer requestId);
}
