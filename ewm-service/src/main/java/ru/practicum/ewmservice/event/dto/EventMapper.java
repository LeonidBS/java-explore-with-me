package ru.practicum.ewmservice.event.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewmservice.category.dto.CategoryMapper;
import ru.practicum.ewmservice.event.model.Event;
import ru.practicum.ewmservice.event.model.State;
import ru.practicum.ewmservice.participation.model.ParticipationRequestStatus;
import ru.practicum.ewmservice.participation.repository.ParticipationRepository;
import ru.practicum.ewmservice.user.dto.UserMapper;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EventMapper {
    private final ParticipationRepository participationRepository;
    private final CategoryMapper categoryMapper;

    public EventFullDto mapToDto(Event entity) {

        return new EventFullDto(
                entity.getId(),
                entity.getAnnotation(),
                categoryMapper.mapToDto(entity.getCategory()),
                participationRepository.findParticipationCountByEventIdAndStatus(entity.getId(),
                        ParticipationRequestStatus.CONFIRMED),
                entity.getCreatedOn(),
                entity.getDescription(),
                entity.getEventDate(),
                UserMapper.mapToUserShortDto(entity.getInitiator()),
                entity.getLocation(),
                entity.getPaid(),
                entity.getParticipantLimit(),
                entity.getPublishedOn(),
                entity.getRequestModeration(),
                entity.getState(),
                entity.getTitle(),
                entity.getViews()
        );
    }

    public List<EventFullDto> mapListToDto(List<Event> events) {
        List<EventFullDto> eventsFullDto = new ArrayList<>();
        for (Event event : events) {
            eventsFullDto.add(mapToDto(event));
        }
        return eventsFullDto;
    }

    public EventShortDto mapToShortDto(Event entity) {

        return new EventShortDto(
                entity.getId(),
                entity.getAnnotation(),
                categoryMapper.mapToDto(entity.getCategory()),
                participationRepository.findParticipationCountByEventIdAndStatus(entity.getId(),
                        ParticipationRequestStatus.CONFIRMED),
                entity.getDescription(),
                entity.getEventDate(),
                UserMapper.mapToUserShortDto(entity.getInitiator()),
                entity.getPaid(),
                entity.getTitle(),
                entity.getViews()
        );
    }

    public List<EventShortDto> mapListToShortDto(List<Event> events) {
        List<EventShortDto> eventsShortDto = new ArrayList<>();
        for (Event event : events) {
            eventsShortDto.add(mapToShortDto(event));
        }
        return eventsShortDto;
    }
}


