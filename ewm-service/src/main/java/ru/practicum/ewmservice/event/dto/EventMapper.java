package ru.practicum.ewmservice.event.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewmservice.event.model.Event;
import ru.practicum.ewmservice.user.dto.UserMapper;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EventMapper {

    public static EventFullDto mapToDto(Event entity) {

        return new EventFullDto(
                entity.getId(),
                entity.getAnnotation(),
                null,
                0,
                entity.getCreatedOn(),
                entity.getDescription(),
                entity.getEventDate(),
                entity.getInitiator() != null ?
                        UserMapper.mapToUserShortDto(entity.getInitiator()) : null,
                new LocationDto(entity.getLocation().getLat(), entity.getLocation().getLon()),
                entity.getPaid(),
                entity.getParticipantLimit(),
                entity.getPublishedOn(),
                entity.getRequestModeration(),
                entity.getState(),
                entity.getTitle(),
                0,
                null,
                0
        );
    }

    public static EventFullPublicDto mapToPublicDto(Event entity) {

        return new EventFullPublicDto(
                entity.getId(),
                entity.getAnnotation(),
                null,
                0,
                entity.getCreatedOn(),
                entity.getDescription(),
                entity.getEventDate(),
                entity.getInitiator() != null ?
                        UserMapper.mapToUserShortForPublicDto(entity.getInitiator()) : null,
                new LocationDto(entity.getLocation().getLat(), entity.getLocation().getLon()),
                entity.getPaid(),
                entity.getParticipantLimit(),
                entity.getPublishedOn(),
                entity.getRequestModeration(),
                entity.getState(),
                entity.getTitle(),
                0,
                null,
                0
        );
    }

    public static List<EventFullDto> mapListToDto(List<Event> events) {
        List<EventFullDto> eventsFullDto = new ArrayList<>();
        for (Event event : events) {
            eventsFullDto.add(mapToDto(event));
        }
        return eventsFullDto;
    }

    public static EventShortDto mapToShortDto(Event entity) {

        return new EventShortDto(
                entity.getId(),
                entity.getAnnotation(),
                null,
                null,
                entity.getDescription(),
                entity.getEventDate(),
                entity.getInitiator() != null ?
                        UserMapper.mapToUserShortDto(entity.getInitiator()) : null,
                entity.getPaid(),
                entity.getTitle(),
                0,
                null,
                null
        );
    }

    public static EventShortPublicDto mapToShortPublicDto(Event entity) {

        return new EventShortPublicDto(
                entity.getId(),
                entity.getAnnotation(),
                null,
                null,
                entity.getDescription(),
                entity.getEventDate(),
                entity.getInitiator() != null ?
                        UserMapper.mapToUserShortForPublicDto(entity.getInitiator()) : null,
                entity.getPaid(),
                entity.getTitle(),
                0,
                null,
                0
        );
    }

    public static List<EventFullDto> mapListToFullDto(List<Event> events) {
        List<EventFullDto> eventsFullDto = new ArrayList<>();
        for (Event event : events) {
            eventsFullDto.add(mapToDto(event));
        }
        return eventsFullDto;
    }

    public static List<EventShortDto> mapListToShortDto(List<Event> events) {
        List<EventShortDto> eventsShortDto = new ArrayList<>();
        for (Event event : events) {
            eventsShortDto.add(mapToShortDto(event));
        }
        return eventsShortDto;
    }

    public static List<EventShortPublicDto> mapListToShortPublicDto(List<Event> events) {
        List<EventShortPublicDto> eventsShortDto = new ArrayList<>();
        for (Event event : events) {
            eventsShortDto.add(mapToShortPublicDto(event));
        }
        return eventsShortDto;
    }
}


