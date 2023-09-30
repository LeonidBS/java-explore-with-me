package ru.practicum.ewmservice.event.prvt.service;

import ru.practicum.ewmservice.event.dto.*;
import ru.practicum.ewmservice.participation.dto.ParticipationDto;

import java.util.List;

public interface EventPrivateService {
    List<EventShortDto> findByInitiatorId(Integer userId, Integer from, Integer size, String sort);

    EventFullDto create(NewEventDto newEventDto, Integer userId);

    EventFullDto findByIdAndInitiatorId(Integer userId, Integer eventId);

    EventFullDto updateEvent(UpdateEventUserRequest updateEventUserRequest, Integer userId, Integer eventId);

    List<ParticipationDto> findByEventIdAndEventInitiatorId(Integer userId, Integer eventId);

    EventRequestStatusUpdateResult updateParticipation(EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest, Integer userId, Integer eventId);
}
